package com.hostfully.tests;

import com.hostfully.pojo.Booking;
import com.hostfully.pojo.Property;
import com.hostfully.utilities.GenerateFakeParameter;
import com.hostfully.utilities.HostfullyUtil;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PropertyCreationTests extends TestBase {

    /**
     * Valid Input	        201 Created
     * Missing Fields	    400 Bad Request
     * Invalid Format	    400 Bad Request
     * Unauthorized Access	401 Unauthorized
     */
    @DisplayName("Create Property with Valid Input")
    @ParameterizedTest
    @ValueSource(ints = 10)
    @Order(1)
    public void createPropertyWithValidInput(int value ) throws IOException {

        // Create parameterized test for creating property with valid input
        HostfullyUtil.clearCSV();
        for (int i = 0; i < value; i++) {
            String name = GenerateFakeParameter.generateName();

            Property property = new Property();
            property.setAlias(name);


            Response response = given()
                    .spec(userRequestSpec)
                    .body(property)
                    .contentType(ContentType.JSON)
                    .when()
                    .post("/properties")
                    .then()
                    .statusCode(201)
                    .extract().response();

            JsonPath jsonPath = response.jsonPath();

            Assertions.assertEquals(201, response.statusCode());
            Assertions.assertEquals("application/json", response.contentType());
            Assertions.assertEquals(name, jsonPath.getString("alias"));
            Assertions.assertNull(jsonPath.getString("countryCode"));
            Assertions.assertFalse(jsonPath.getString("id").isEmpty());

            response.prettyPrint();

            Property property1 = given()
                    .spec(userRequestSpec)
                    .when()
                    .get("/properties").jsonPath().getObject("find{it.alias == '" + name + "'}", Property.class);


            Booking.setListPropertyId(String.valueOf(property1.getId()));
            Booking.getListPropertyId();
            System.out.println("property1.getCreatedAt().toString() = " + property1.getCreatedAt().toString());

            String createdAt = HostfullyUtil.parseDateString(property1.getCreatedAt().toString());
            HostfullyUtil.appendToCSV(HostfullyUtil.path, String.valueOf(property1.getId()), String.valueOf(property1.getAlias()),createdAt);


        }
    }

    @DisplayName("Create Property with Missing Fields")
    @Test
    @Order(5)
    public void test() {
        Response response = given()
                .spec(userRequestSpec)
                .when()
                .get("/properties");

        System.out.println("response.body().asString() = " + response.body().asString());
    }
    @DisplayName("Create Property with Missing Fields")
    @ParameterizedTest
    @CsvFileSource(resources = "/parameterizedCsvFile.csv", numLinesToSkip = 1)
    @Order(2)
    public void createPropertyWithInvalidInput(String id) throws IOException {

        Property property = new Property();
        property.setId(id);

        Response response = given()
                .spec(userRequestSpec)
                .body(property+",")
                .contentType(ContentType.JSON)
                .when()
                .post("/properties");

        JsonPath jsonPath = response.jsonPath();
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertEquals("application/problem+json", response.contentType());
        Assertions.assertEquals("Bad Request", jsonPath.getString("title"));
        Assertions.assertEquals("Failed to read request", jsonPath.getString("detail"));
        response.body().prettyPrint();




    }
    @DisplayName("Create Property with Invalid Format")
    @ParameterizedTest
    @CsvFileSource(resources = "/parameterizedCsvFile.csv", numLinesToSkip = 1)
    @Order(3)
    public void checkPropertyWithValidationError(String id) throws IOException {

        Property property = new Property();

        property.setId(id);


        Response response = given()
                .spec(userRequestSpec)
                .body(property)
                .contentType(ContentType.JSON)
                .when()
                .post("/properties");

        JsonPath jsonPath = response.jsonPath();
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertEquals("application/problem+json", response.contentType());
        Assertions.assertEquals("Validation failed", jsonPath.getString("detail"));
        Assertions.assertEquals("Validation Error", jsonPath.getString("title"));
        response.body().prettyPrint();


    }
    @DisplayName("Create Property with Internal Error")
    @Test
    @Order(3)
    public void checkPropertyWithInternalError() {

        Property property = new Property();

        property.setAlias("Property for American");


        Response response = given()
                .spec(userRequestSpec)
                .body(property)
                .contentType(ContentType.JSON)
                .when()
                .post("/properties");

        JsonPath jsonPath = response.jsonPath();
        Assertions.assertEquals(500, response.statusCode());
        Assertions.assertEquals("application/json", response.contentType());
        Assertions.assertEquals("Internal Server Error", jsonPath.getString("error"));
        response.body().prettyPrint();


    }
    @DisplayName("Create Property with Unauthorized Access")
    @Test
    @Order(4)
    public void checkPropertyWithUnauthorizedAccess() {

        Property property = new Property();

        property.setAlias("Property for America");
        property.setCountryCode(null);


        Response response = given()
                .body(property)
                .contentType(ContentType.JSON)
                .when()
                .post("/properties");

        JsonPath jsonPath = response.jsonPath();
        Assertions.assertEquals(401, response.statusCode());
        Assertions.assertEquals("application/json", response.contentType());
        Assertions.assertEquals("Error while authenticating your access", jsonPath.getString("message"));
        Assertions.assertEquals("Unauthorized", jsonPath.getString("error"));
        response.body().prettyPrint();


    }




}
