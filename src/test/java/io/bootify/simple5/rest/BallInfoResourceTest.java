package io.bootify.simple5.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.bootify.simple5.config.BaseIT;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;


public class BallInfoResourceTest extends BaseIT {

    @Test
    @Sql("/data/ballInfoData.sql")
    void getAllBallInfos_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, firstConfigToken(ROLE_USER))
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/ballInfos")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(2))
                    .body("_embedded.ballInfoDTOList.get(0).ballId", Matchers.equalTo(1000))
                    .body("_links.self.href", Matchers.endsWith("/api/ballInfos?page=0&size=20&sort=ballId,asc"));
    }

    @Test
    @Sql("/data/ballInfoData.sql")
    void getAllBallInfos_filtered() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, firstConfigToken(ROLE_USER))
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/ballInfos?filter=1001")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(1))
                    .body("_embedded.ballInfoDTOList.get(0).ballId", Matchers.equalTo(1001));
    }

    @Test
    void getAllBallInfos_unauthorized() {
        RestAssured
                .given()
                    .redirects().follow(false)
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/ballInfos")
                .then()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .body("code", Matchers.equalTo("UNAUTHORIZED"));
    }

    @Test
    @Sql("/data/ballInfoData.sql")
    void getBallInfo_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, firstConfigToken(ROLE_USER))
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/ballInfos/1000")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("ballName", Matchers.equalTo("Viverra suspendisse."))
                    .body("_links.self.href", Matchers.endsWith("/api/ballInfos/1000"));
    }

    @Test
    void getBallInfo_notFound() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, firstConfigToken(ROLE_USER))
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/ballInfos/1666")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    void createBallInfo_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, firstConfigToken(ROLE_USER))
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/ballInfoDTORequest.json"))
                .when()
                    .post("/api/ballInfos")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, ballInfoRepository.count());
    }

    @Test
    void createBallInfo_missingField() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, firstConfigToken(ROLE_USER))
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/ballInfoDTORequest_missingField.json"))
                .when()
                    .post("/api/ballInfos")
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                    .body("fieldErrors.get(0).property", Matchers.equalTo("ballName"))
                    .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"));
    }

    @Test
    @Sql("/data/ballInfoData.sql")
    void updateBallInfo_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, firstConfigToken(ROLE_USER))
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/ballInfoDTORequest.json"))
                .when()
                    .put("/api/ballInfos/1000")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("_links.self.href", Matchers.endsWith("/api/ballInfos/1000"));
        assertEquals("Quis nostrud exerci.", ballInfoRepository.findById(((long)1000)).orElseThrow().getBallName());
        assertEquals(2, ballInfoRepository.count());
    }

    @Test
    @Sql("/data/ballInfoData.sql")
    void deleteBallInfo_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, firstConfigToken(ROLE_USER))
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/ballInfos/1000")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, ballInfoRepository.count());
    }

}
