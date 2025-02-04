package com.hostfully.tests;

import com.hostfully.pojo.Property;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;

public class PropertyCreationTests extends TestBase{


    @Test
    public void test(){
        Response response = given()
                .spec(userRequestSpec)
                .when()
                .get("/properties");

        System.out.println("response.body().asString() = " + response.body().asString());
    }
/**  Valid Input	        201 Created
     Missing Fields	        400 Bad Request
     Invalid Format	        400 Bad Request
     Unauthorized Access	401 Unauthorized
 */

    @Test
    public void createPropertyWithValidInput() {

        Property property = new Property();
        property.setAlias("Property for Africans");
        property.setCountryCode(null);
        property.setCreatedAt(Arrays.asList(2025, 1, 13, 19, 41, 33, 240453000));


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
//       Assertions.assertEquals("application/json", response.contentType());
//       Assertions.assertEquals("48e8f7a2-7cd9-4da6-96e8-9c01b12a9068", jsonPath.getString("id"));
//       Assertions.assertEquals("Property for American", jsonPath.getString("alias"));
//       Assertions.assertEquals(Arrays.asList(2025, 1, 21, 19, 41, 33, 240453000), jsonPath.getList("createdAt"));


       response.prettyPrint();

    }

    @Test
    public void createPropertyWithInvalidInput() {

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
                .post("/properties");

        JsonPath jsonPath = response.jsonPath();
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertEquals("application/problem+json", response.contentType());
        Assertions.assertEquals("Bad Request", jsonPath.getString("title"));
        Assertions.assertEquals("Failed to read request", jsonPath.getString("detail"));
        response.body().prettyPrint();


    }

    @Test
    public void createPropertyWithBadRequest() {

        Property property = new Property();
        property.setId("844bf8ad-a434-4e9a-9a6e-f6cdc5a719eg");
        property.setAlias("Property for American");
        property.setCountryCode(null);



        Response response = given()
                .spec(userRequestSpec)
                .body(property)
                .contentType(ContentType.JSON)
                .when()
                .post("/properties");

        JsonPath jsonPath = response.jsonPath();
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertEquals("Failed to read request", jsonPath.getString("detail"));
        response.body().prettyPrint();


    }

    @Test
    public void createPropertyWithInternalError() {

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

    @Test
    public void createPropertyWithUnauthorizedAccess() {

        Property property = new Property();
        property.setId("844bf8ad-a434-4e9a-9a6e-f6cdc5a719ed");
        property.setAlias("Property for America");
        property.setCountryCode(null);
        property.setCreatedAt(Arrays.asList(2025, 1, 21, 19, 41, 33, 240453000));


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
