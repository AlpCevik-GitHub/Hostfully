package com.hostfully.tests;

import com.hostfully.pojo.Booking;
import com.hostfully.pojo.Property;
import com.hostfully.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.json.Json;

import java.util.Arrays;
import java.util.Collections;

import static io.restassured.RestAssured.given;

public class ErrorHandlingTests extends TestBase{

    @DisplayName("Test Bad Request")
    @Test
    public void testBadRequest() {
        //send a post request with missing parameters
        //verify status code and content type

        Booking booking = new Booking();
        booking.setId("1567d5fe-c59c-4a9d-ac86-74473090534a");
        booking.setEndDate(Arrays.asList(2025,6,11));
        booking.setStatus("SCHEDULED");
        Booking.Guest guest = new Booking.Guest();
        guest.setFirstName("John");
        guest.setLastName("Doe");
        guest.setDateOfBirth(Arrays.asList(1990, 1, 1));
        booking.setGuest(guest);


        Response response = given()
                .spec(userRequestSpec)
                .body(booking)
                .contentType(ContentType.JSON)
                .post("/bookings")
                .then()
                .statusCode(400)
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        Assertions.assertEquals("Validation Error",jsonPath.getString("title"));
        Assertions.assertEquals("Validation failed",jsonPath.getString("detail"));
        Assertions.assertEquals(400,jsonPath.getInt("status"));

        System.out.println("defaultMessage = " + jsonPath.get("errors.defaultMessage").toString());

        response.prettyPrint();

    }
    @DisplayName("Test Unauthorized Request")
    @Test
    public void testUnauthorizedRequest() {
        //send a get request without token
        //verify status code and content type

        Response response = given()
                .when()
                .get("/bookings")
                .then()
                .statusCode(401)
                .and()
                .contentType("application/json")
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        Assertions.assertEquals("Unauthorized",jsonPath.getString("error"));
        Assertions.assertEquals("Error while authenticating your access",jsonPath.getString("message"));

        response.prettyPrint();
    }
    @DisplayName("Test Forbidden Request")
    @Test
    public void testInvalidCredentials() {
        //send a get request with invalid token
        //verify status code and content type

        Response response = given()
                .accept(ContentType.JSON)
                .auth().preemptive().basic("invalidName", "invalidPassword")
                .when()
                .delete("/bookings");

        JsonPath jsonPath = response.jsonPath();
        Assertions.assertEquals(401, response.statusCode());

        Assertions.assertEquals("Bad credentials",jsonPath.getString("exception"));
        Assertions.assertEquals("Error while authenticating your access",jsonPath.getString("message"));
        Assertions.assertEquals("Unauthorized",jsonPath.getString("error"));
        jsonPath.prettyPrint();
    }
    @DisplayName("Test Not Found")
    @Test
    public void testNotFound() {
        //send a get request with invalid url
        Response response = given()
                .spec(userRequestSpec)
                .when()
                .get("/invalid-endpoint")
                .then()
                .statusCode(404)
                .extract().response();

        Assertions.assertEquals("Not Found",response.path("error"));

        response.prettyPrint();
    }
    @DisplayName("Test Method Not Allowed")
    @Test
    public void testUnsupportedMediaType() {
        //send a post request with invalid body
        Response response = given()
                .spec(userRequestSpec)
                .contentType("text/plain")
                .body("Invalid body")
                .when()
                .post("/bookings")
                .then()
                .statusCode(415)
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        Assertions.assertEquals("Unsupported Media Type",jsonPath.getString("title"));
        Assertions.assertEquals("Content-Type 'text/plain;charset=ISO-8859-1' is not supported.",jsonPath.getString("detail"));
        Assertions.assertEquals(415,jsonPath.getInt("status"));

        response.prettyPrint();
    }

    @DisplayName("Test Internal Server Error")
    @Test
    public void testInternalServerError() {
        //send a post request with error in the body
        Property property = new Property();
        property.setId("844bf8ad-a434-4e9a-9a6e-f6cdc5a719ed");
        property.setAlias("Property for America");
        property.setCountryCode(null);
        property.setCreatedAt(Arrays.asList(2025, 1, 21, 19, 41, 33, 240453000));

        Response response = given()
                .spec(userRequestSpec)
                .body(property)
                .contentType(ContentType.JSON)
                .when()
                .post("/properties")
                .then()
                .statusCode(500)
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        Assertions.assertEquals("Internal Server Error",jsonPath.getString("error"));
        Assertions.assertEquals(500,jsonPath.getInt("status"));

        response.prettyPrint();
    }




}
