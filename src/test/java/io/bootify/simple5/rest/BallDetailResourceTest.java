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


public class BallDetailResourceTest extends BaseIT {

    @Test
    @Sql("/data/ballDetailData.sql")
    void getAllBallDetails_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, firstConfigToken(ROLE_USER))
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/ballDetails")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("_embedded.ballDetailDTOList.size()", Matchers.equalTo(2))
                    .body("_embedded.ballDetailDTOList.get(0).id", Matchers.equalTo(1100))
                    .body("_links.self.href", Matchers.endsWith("/api/ballDetails"));
    }

    @Test
    @Sql("/data/ballDetailData.sql")
    void getBallDetail_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, firstConfigToken(ROLE_USER))
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/ballDetails/1100")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("color", Matchers.equalTo("Ut wisi enim."))
                    .body("_links.self.href", Matchers.endsWith("/api/ballDetails/1100"));
    }

    @Test
    void getBallDetail_notFound() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, firstConfigToken(ROLE_USER))
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/ballDetails/1766")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    void createBallDetail_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, firstConfigToken(ROLE_USER))
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/ballDetailDTORequest.json"))
                .when()
                    .post("/api/ballDetails")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, ballDetailRepository.count());
    }

    @Test
    void createBallDetail_missingField() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, firstConfigToken(ROLE_USER))
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/ballDetailDTORequest_missingField.json"))
                .when()
                    .post("/api/ballDetails")
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                    .body("fieldErrors.get(0).property", Matchers.equalTo("color"))
                    .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"));
    }

    @Test
    @Sql("/data/ballDetailData.sql")
    void updateBallDetail_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, firstConfigToken(ROLE_USER))
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/ballDetailDTORequest.json"))
                .when()
                    .put("/api/ballDetails/1100")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("_links.self.href", Matchers.endsWith("/api/ballDetails/1100"));
        assertEquals("Viverra suspendisse.", ballDetailRepository.findById(((long)1100)).orElseThrow().getColor());
        assertEquals(2, ballDetailRepository.count());
    }

    @Test
    @Sql("/data/ballDetailData.sql")
    void deleteBallDetail_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, firstConfigToken(ROLE_USER))
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/ballDetails/1100")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, ballDetailRepository.count());
    }

}
