package com.hostfully.tests;

import com.hostfully.pojo.Property;
import com.hostfully.utilities.HostfullyUtil;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class PropertyRetrievalTests extends TestBase{

    @DisplayName("Test Property Retrieval with Valid ID")
    @ParameterizedTest
    @CsvFileSource(resources = "/parameterizedCsvFile.csv", numLinesToSkip = 1)
    public void testWithValidID(String id, String name, String date){

        Response response = given()
                .spec(userRequestSpec)
                .pathParam("id", id)
                .when()
                .get("/properties/{id}");

        Property property = response.as(Property.class);

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(id, property.getId());
        Assertions.assertEquals(name, property.getAlias());
        String parseDateString = HostfullyUtil.parseDateString(String.valueOf(property.getCreatedAt()));
        Assertions.assertEquals(date, parseDateString, "Date comparison failed");

        response.prettyPeek();



    }

    @DisplayName("Test Property Retrieval with Invalid ID")
    @ParameterizedTest
    @CsvFileSource(resources = "/parameterizedCsvFile.csv", numLinesToSkip = 1)
    public void testWithInvalidID(String id){

         id = id.substring(0, id.length()-2);

        Response response = given()
                .spec(userRequestSpec)
                .pathParam("id", id)
                .when()
                .get("/properties/{id}");

        JsonPath jsonPath = response.jsonPath();

        Assertions.assertEquals(204, response.statusCode());

        response.prettyPeek();



    }


}
