package com.hostfully.tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class PropertyRetrievalTests extends TestBase{

    @Test
    public void testWithValidID(){

        String propertyId = "844bf8ad-a434-4e9a-9a6e-f6cdc5a719ed";

        Response response = given()
                .spec(userRequestSpec)
                .pathParam("id", propertyId)
                .when()
                .get("/properties/{id}");

        JsonPath jsonPath = response.jsonPath();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(propertyId, jsonPath.getString("id"));
        Assertions.assertEquals("Property for America", jsonPath.getString("alias"));

        response.prettyPeek();



    }

    @Test
    public void testWithValidID1(){

        String propertyId = "48e8f5a2-7cd9-4da6-96e8-9c01b12a9068";

        Response response = given()
                .spec(userRequestSpec)
                .pathParam("id", propertyId)
                .when()
                .get("/properties/{id}");

        JsonPath jsonPath = response.jsonPath();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(propertyId, jsonPath.getString("id"));
        Assertions.assertEquals("Modern Apartment Downtown", jsonPath.getString("alias"));

        response.prettyPeek();



    }

    @Test
    public void testWithInvalidID(){

        String propertyId = "844bf8ad-a434-4e9a-9a6e-f6cdc5a719ef";

        Response response = given()
                .spec(userRequestSpec)
                .pathParam("id", propertyId)
                .when()
                .get("/properties/{id}");

        JsonPath jsonPath = response.jsonPath();

        Assertions.assertEquals(204, response.statusCode());

        response.prettyPeek();



    }


}
