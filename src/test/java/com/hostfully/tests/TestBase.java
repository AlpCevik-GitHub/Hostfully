package com.hostfully.tests;

import com.hostfully.utilities.ConfigurationReader;
import com.hostfully.utilities.HostfullyUtil;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.reset;
import static org.hamcrest.Matchers.lessThan;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBase {

    public static RequestSpecification userRequestSpec;
    public static RequestSpecification adminRequestSpec;
    public static ResponseSpecification responseSpec;
    public static ResponseSpecification responseSpecBadRequest;

    @BeforeAll
    public static void init() throws IOException {
      //save baseurl inside this variable.

        baseURI = ConfigurationReader.getProperty("base_url");
        System.out.println("ConfigurationReader.getProperty(\"user_name\") = " + ConfigurationReader.getProperty("user_name"));
        System.out.println("ConfigurationReader.getProperty(\"user_password\") = " + ConfigurationReader.getProperty("user_password"));
        userRequestSpec = given()
                .accept(ContentType.JSON)
                .auth().preemptive().basic(ConfigurationReader.getProperty("user_name"), ConfigurationReader.getProperty("user_password"))
                .log().all();
        adminRequestSpec = given()
                .accept(ContentType.JSON)
                .auth().basic(ConfigurationReader.getProperty("admin_name"), ConfigurationReader.getProperty("admin_password"))
                .log().all();
        responseSpec = expect()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .logDetail(LogDetail.ALL)
                .time(lessThan(5000L));
        responseSpecBadRequest = expect()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .logDetail(LogDetail.ALL)
                .time(lessThan(5000L));





    }

    @AfterAll
    public static void tearDown() {
        //reset
        reset();
    }


    public static String getTokenByRole(String role){

        String username,password;

        switch (role) {
            case "admin":
                username = ConfigurationReader.getProperty("admin_name");
                password = ConfigurationReader.getProperty("admin_password");
                break;
            case "user":
                username = ConfigurationReader.getProperty("user_name");
                password = ConfigurationReader.getProperty("user_password");
                break;
            default:
                throw new RuntimeException("Invalid role!");
        }

        String accessToken =
                given()
                .accept(ContentType.JSON)
                .queryParams("username", username, "password", password)
                .when().get("/")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("accessToken");

     return "Bearer " + accessToken;

    }

}
