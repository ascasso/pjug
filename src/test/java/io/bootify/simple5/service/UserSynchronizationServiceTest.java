package io.bootify.simple5.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.bootify.simple5.config.BaseIT;
import io.bootify.simple5.domain.UserInfo;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;


public class UserSynchronizationServiceTest extends BaseIT {

    @Test
    public void userCreatedAfterLogin() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, firstConfigToken(ROLE_ANY))
                    .accept(ContentType.JSON)
                .when()
                    .get("/");
        ;
        final UserInfo userInfo = userInfoRepository.findByCredentials("43bdac2a-5a4e-3b4d-ae00-44fa7a2c1569");
        assertNotNull(userInfo);
        assertEquals("roleany@invalid.bootify.io", userInfo.getEmail());
        assertEquals("Bob", userInfo.getResetString());
        assertEquals("Lazar", userInfo.getUserRole());
    }

}
