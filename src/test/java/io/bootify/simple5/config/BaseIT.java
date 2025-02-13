package io.bootify.simple5.config;

import com.redis.testcontainers.RedisContainer;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.bootify.simple5.Simple5Application;
import io.bootify.simple5.repos.BallDetailRepository;
import io.bootify.simple5.repos.BallInfoRepository;
import io.bootify.simple5.repos.UserInfoRepository;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.config.SessionConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;


/**
 * Abstract base class to be extended by every IT test. Starts the Spring Boot context with a
 * Datasource connected to the Testcontainers Docker instance. The instance is reused for all tests,
 * with all data wiped out before each test.
 */
@SpringBootTest(
        classes = Simple5Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("it")
@Sql("/data/clearAll.sql")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public abstract class BaseIT {

    @ServiceConnection
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:17.2");
    private static final RedisContainer redisContainer = new RedisContainer("redis:7.4-alpine");
    private static final GenericContainer<?> mailpitContainer = new GenericContainer<>("axllent/mailpit:v1.21");
    public static String smtpHost;
    public static Integer smtpPort;
    public static String messagesUrl;
    private static final KeycloakContainer keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:26.0.4");
    public static final String ROLE_ANY = "roleAny@invalid.bootify.io";
    public static final String ROLE_USER = "roleUser@invalid.bootify.io";
    public static final String PASSWORD = "Bootify!";
    private static final HashMap<String, String> firstConfigTokens = new HashMap<>();

    static {
        postgreSQLContainer.withReuse(true)
                .start();
        redisContainer.withExposedPorts(6379)
                .withReuse(true)
                .start();
        mailpitContainer.withExposedPorts(1025, 8025)
                .waitingFor(Wait.forLogMessage(".*accessible via.*", 1))
                .withReuse(true)
                .start();
        smtpHost = mailpitContainer.getHost();
        smtpPort = mailpitContainer.getMappedPort(1025);
        messagesUrl = "http://" + smtpHost + ":" + mailpitContainer.getMappedPort(8025) + "/api/v1/messages";
        keycloakContainer.withRealmImportFile("keycloak-realm.json")
                .withReuse(true)
                .start();
    }

    @LocalServerPort
    public int serverPort;

    @Autowired
    public BallInfoRepository ballInfoRepository;

    @Autowired
    public BallDetailRepository ballDetailRepository;

    @Autowired
    public UserInfoRepository userInfoRepository;

    @PostConstruct
    public void initRestAssured() {
        RestAssured.port = serverPort;
        RestAssured.urlEncodingEnabled = false;
        RestAssured.config = RestAssured.config().sessionConfig(new SessionConfig().sessionIdName("SESSION"));
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @DynamicPropertySource
    public static void setDynamicProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.url", () -> redisContainer.getRedisURI());
        registry.add("spring.mail.host", () -> smtpHost);
        registry.add("spring.mail.port", () -> smtpPort);
        registry.add("spring.mail.properties.mail.smtp.auth", () -> false);
        registry.add("spring.mail.properties.mail.smtp.starttls.enable", () -> false);
        registry.add("spring.mail.properties.mail.smtp.starttls.required", () -> false);
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
                () -> keycloakContainer.getAuthServerUrl() + "/realms/realid/protocol/openid-connect/certs");
    }

    @BeforeEach
    public void beforeEach() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .delete(messagesUrl);
    }

    public String readResource(final String resourceName) {
        try {
            return StreamUtils.copyToString(getClass().getResourceAsStream(resourceName), StandardCharsets.UTF_8);
        } catch (final IOException io) {
            throw new UncheckedIOException(io);
        }
    }

    public void waitForMessages(final int total) {
        int loop = 0;
        while (loop++ < 25) {
            final Response messagesResponse = RestAssured
                    .given()
                        .accept(ContentType.JSON)
                    .when()
                        .get(messagesUrl);
            if (messagesResponse.jsonPath().getInt("total") == total) {
                return;
            }
            try {
                Thread.sleep(250);
            } catch (final InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        throw new RuntimeException("Could not find " + total + " messages in time.");
    }

    public String firstConfigToken(final String username) {
        String firstConfigToken = firstConfigTokens.get(username);
        if (firstConfigToken == null) {
            // get a fresh token
            final String tokenUrl = keycloakContainer.getAuthServerUrl() + "/realms/realid/protocol/openid-connect/token";
            final Map<String, Object> keycloakTokenResponse = RestAssured
                    .given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.URLENC)
                        .formParam("grant_type", "password")
                        .formParam("client_id", "id1234")
                        .formParam("client_secret", "F12343808F6E48B08C30F5B84D8C1F62")
                        .formParam("username", username)
                        .formParam("password", PASSWORD)
                    .when()
                        .post(tokenUrl)
                    .body().as(new TypeRef<>() {
                    });
            firstConfigToken = "Bearer " + keycloakTokenResponse.get("access_token");
            firstConfigTokens.put(username, firstConfigToken);
        }
        return firstConfigToken;
    }

}
