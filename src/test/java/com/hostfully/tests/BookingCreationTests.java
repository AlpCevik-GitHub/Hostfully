package com.hostfully.tests;

import com.hostfully.pojo.Booking;
import com.hostfully.utilities.HostfullyUtil;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;

import static com.hostfully.utilities.HostfullyUtil.isDateOverlap;
import static com.hostfully.utilities.HostfullyUtil.toValidDateList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

public class BookingCreationTests extends TestBase {

    @DisplayName("Create Booking with Valid Property")
    @ParameterizedTest
    @CsvFileSource(resources = "/parameterizedCsvFile.csv", numLinesToSkip = 1)
    @Order(1)
    public void bookingWithValidProperty(String id) {

        Response response = given().contentType(ContentType.JSON)
                .spec(userRequestSpec)
                .when()
                .get("/bookings");

        if (!response.getBody().asString().isEmpty()) {
            boolean propertyExists = false;
            Booking[] allBookings = response.as(Booking[].class);

            Map<String, Map<String, List<Integer>>> propertyDates = new HashMap<>();
            for (Booking booking : allBookings) {
                if (booking.getPropertyId().equals(id)) {
                    List<Integer> startDate = new ArrayList<>();
                    startDate.addAll(booking.getStartDate());


                    List<Integer> endDate = new ArrayList<>();
                    endDate.addAll(booking.getEndDate());

                    Map<String, List<Integer>> dates = new HashMap<>();
                    dates.put("startDate", startDate);
                    dates.put("endDate", endDate);

                    propertyDates.put(booking.getPropertyId(), dates);
                    propertyExists = true;

                }
            }

            Booking booking = new Booking();
            List<Integer> startDate = HostfullyUtil.generateRandomStartDate();
            List<Integer> endDate = Arrays.asList(startDate.get(0), startDate.get(1), startDate.get(2) + 2);


            if (propertyExists) {
                if (propertyDates.containsKey(id)) {
                    Map<String, List<Integer>> existingDates = propertyDates.get(id);
                    List<Integer> existingStartDate = existingDates.get("startDate");
                    List<Integer> existingEndDate = existingDates.get("endDate");


                    boolean datesOverlap = isDateOverlap(existingStartDate, existingEndDate, startDate, endDate);


                    if (datesOverlap) {
                        boolean isValid = false;


                        while (!isValid) {
                            startDate = HostfullyUtil.generateRandomStartDate();
                            endDate = Arrays.asList(startDate.get(0), startDate.get(1), startDate.get(2) + 2);



                            isValid = !isDateOverlap(existingStartDate, existingEndDate, startDate, endDate);
                        }

                    }
                }
            }

            //Serialization
            booking.setStartDate(toValidDateList(startDate));
            booking.setEndDate(toValidDateList(endDate));
            booking.setStatus("SCHEDULED");
            booking.setPropertyId(id);
            Booking.Guest guest = new Booking.Guest();
            guest.setFirstName("John");
            guest.setLastName("Doe");
            guest.setDateOfBirth(Arrays.asList(1990, 1, 1));
            booking.setGuest(guest);


            Response postResponse = given()
                    .spec(userRequestSpec)
                    .contentType(ContentType.JSON)
                    .body(booking)
                    .when()
                    .post("/bookings")
                    .then()
                    .body(matchesJsonSchemaInClasspath("bookingSchemaValidation.json"))
                    .extract().response();;


            JsonPath jsonPath = postResponse.jsonPath();

            Assertions.assertEquals(201, postResponse.statusCode());
            Assertions.assertNotNull(jsonPath.getString("id"));
            Assertions.assertEquals("SCHEDULED", jsonPath.getString("status"));
            Assertions.assertEquals("John", jsonPath.getString("guest.firstName"));
            Assertions.assertEquals("Doe", jsonPath.getString("guest.lastName"));

            jsonPath.prettyPrint();
        } else {
            Booking booking = new Booking();
            List<Integer> startDate = HostfullyUtil.generateRandomStartDate();
            List<Integer> endDate = Arrays.asList(startDate.get(0), startDate.get(1), startDate.get(2) + 2);
            booking.setStartDate(toValidDateList(startDate));
            booking.setEndDate(toValidDateList(endDate));
            booking.setStatus("SCHEDULED");
            booking.setPropertyId(id);
            Booking.Guest guest = new Booking.Guest();
            guest.setFirstName("John");
            guest.setLastName("Doe");
            guest.setDateOfBirth(Arrays.asList(1990, 1, 1));
            booking.setGuest(guest);


            Response postResponse = given()
                    .spec(userRequestSpec)
                    .contentType(ContentType.JSON)
                    .body(booking)
                    .when()
                    .post("/bookings")
                    .then()
                    .body(matchesJsonSchemaInClasspath("bookingSchemaValidation.json"))
                    .extract().response();


            JsonPath jsonPath = postResponse.jsonPath();

            Assertions.assertEquals(201, postResponse.statusCode());
            Assertions.assertNotNull(jsonPath.getString("id"));
            Assertions.assertEquals("SCHEDULED", jsonPath.getString("status"));
            Assertions.assertEquals("John", jsonPath.getString("guest.firstName"));
            Assertions.assertEquals("Doe", jsonPath.getString("guest.lastName"));

            jsonPath.prettyPrint();
        }


    }


    @DisplayName("Get Booking ID For CSV")
    @Test()
    @Order(2)
     public void getBookingID() throws IOException {

        HostfullyUtil.clearCSV(HostfullyUtil.pathID);
        Response response = given().contentType(ContentType.JSON)
                .spec(userRequestSpec)
                .when()
                .get("/bookings");

        if (!response.getBody().asString().isEmpty()) {
            List<String> allIds = response.jsonPath().getList("findAll { it.status == 'SCHEDULED' }.id", String.class);
            for (String bookingId : allIds) {
                HostfullyUtil.appendToCSVForID(HostfullyUtil.pathID, String.valueOf(bookingId));
            }

        }
    }
    @DisplayName("Create Booking with Invalid Property(Unprocessable Entity)")
    @Test
    @Order(3)
    public void createBookingWithInvalidProperty() {
        // Create parameterized test for creating booking with invalid property
        Response response = given()
                .spec(userRequestSpec)
                .contentType(ContentType.JSON)
                .when()
                .get("/bookings");

        Booking[] allBookings = response.as(Booking[].class);

        if (!response.getBody().asString().isEmpty()){

            for (Booking booking : allBookings){

                Booking booking1 = new Booking();

               if (booking.getStatus().equalsIgnoreCase("SCHEDULED")) {
                   booking1.setPropertyId(booking.getPropertyId());
                   booking1.setStartDate(booking.getStartDate());
                   booking1.setEndDate(booking.getEndDate());
                   booking1.setStatus(booking.getStatus());
                   booking1.setGuest(booking.getGuest());





                   Response response1 = given()
                           .spec(userRequestSpec)
                           .body(booking1)
                           .contentType(ContentType.JSON)
                           .when()
                           .post("/bookings")
                           .then()
                           .statusCode(422)
                           .body("status", is(422))
                           .body("title", is("Invalid Booking"))
                           .body("detail", is("Supplied booking is not valid"))
                           .body("instance", is("/bookings"))
                           .body("BOOKING_DATES_UNAVAILABLE", is("BOOKING_DATES_UNAVAILABLE"))
                           .extract().response();

                   JsonPath jsonPath = response1.jsonPath();

                   Assertions.assertEquals(422, response1.statusCode());
                   Assertions.assertEquals("Invalid Booking", jsonPath.getString("title"));
                   Assertions.assertEquals("Supplied booking is not valid", jsonPath.getString("detail"));
                   Assertions.assertEquals("/bookings", jsonPath.getString("instance"));
                   Assertions.assertEquals("BOOKING_DATES_UNAVAILABLE", jsonPath.getString("BOOKING_DATES_UNAVAILABLE"));

                   response1.prettyPrint();
               }
            }
        }
    }
    @DisplayName("Cancel Booking with Valid Property")
    @ParameterizedTest
    @CsvFileSource(resources = "/parameterizedBookingIdCSVFile.csv", numLinesToSkip = 1)
    @Order(4)
    public void cancelBookingWithValidProperty(String id) {

        Response response = given().contentType(ContentType.JSON)
                .spec(userRequestSpec)
                .when()
                .get("/bookings");

        if (!response.getBody().asString().isEmpty()) {

                    Response deleteResponse = given()
                            .spec(userRequestSpec)
                            .pathParam("id", id)
                            .when()
                            .patch("/bookings/{id}/cancel")
                            .then()
                            .statusCode(200)
                            .body("status", is("CANCELLED"))
                            .extract().response();

                    JsonPath jsonPath = deleteResponse.jsonPath();

                    Assertions.assertEquals(200, deleteResponse.statusCode());
                    Assertions.assertEquals("CANCELLED", jsonPath.getString("status"));



        }
    }



}
