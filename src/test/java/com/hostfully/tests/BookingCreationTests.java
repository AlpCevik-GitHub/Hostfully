package com.hostfully.tests;

import com.hostfully.pojo.Booking;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Arrays;
import java.util.Collections;

import static io.restassured.RestAssured.given;

public class BookingCreationTests extends TestBase{






    @ParameterizedTest
    @CsvFileSource(resources = "/parameterizedCsvFile.csv", numLinesToSkip = 1)
    public void bookingWithValidProperty(String id){

       given().contentType(ContentType.JSON)
               .spec(userRequestSpec)
               .when()
               .get("/bookings");




        Booking booking = new Booking();
        booking.setId("1567d5fe-c59c-4a9d-ac86-74473090534a");
        booking.setStartDate(Arrays.asList(2025,6,13));
        booking.setEndDate(Arrays.asList(2025,6,13));
        booking.setStatus("SCHEDULED");

        Booking.Guest guest = new Booking.Guest();
        guest.setFirstName("John");
        guest.setLastName("Doe");
        guest.setDateOfBirth(Arrays.asList(1990, 1, 1));
        booking.setGuest(guest);


        System.out.println("booking.toString() = " + booking.toString());

        Response postResponse = given()
                .spec(userRequestSpec)
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .post("/bookings");


        JsonPath jsonPath = postResponse.jsonPath();

        Assertions.assertEquals(201, postResponse.statusCode());
        Assertions.assertNotNull(jsonPath.getString("id"));
        Assertions.assertEquals("SCHEDULED", jsonPath.getString("status"));
        Assertions.assertEquals("John", jsonPath.getString("guest.firstName"));
        Assertions.assertEquals("Doe", jsonPath.getString("guest.lastName"));

        jsonPath.prettyPrint();

    }

    @Test
    public void bookingWithSameValidProperty(){


        Booking booking = new Booking();
        booking.setId("1567d5fe-c59c-4a9d-ac86-74473090534a");
        booking.setStartDate(Arrays.asList(2025,6,11));
        booking.setEndDate(Arrays.asList(2025,6,11));
        booking.setStatus("SCHEDULED");

        Booking.Guest guest = new Booking.Guest();
        guest.setFirstName("John");
        guest.setLastName("Doe");
        guest.setDateOfBirth(Arrays.asList(1990, 1, 1));
        booking.setGuest(guest);


        System.out.println("booking.toString() = " + booking.toString());

        Response postResponse = given()
                .spec(userRequestSpec)
                .contentType("application/json")
                .body(booking)
                .when()
                .post("/bookings");

        JsonPath postResponsePath = postResponse.jsonPath();

        Assertions.assertEquals(422, postResponse.statusCode());

        Assertions.assertEquals(422, postResponsePath.getInt("status"));
        Assertions.assertEquals("Supplied booking is not valid", postResponsePath.getString("detail"));
        Assertions.assertEquals("BOOKING_DATES_UNAVAILABLE", postResponsePath.getString("BOOKING_DATES_UNAVAILABLE"));

    }

//    @Test
//    public void bookingOverlappingSameProperty(){
//
//
//        //Booking firstBooking = createBooking(new Integer[]{2025,6,5}, new Integer[]{2025,6,5});
//        Booking firstBooking = new Booking();
//        firstBooking.setId("1567d5fe-c59c-4a9d-ac86-74473090534a");
//        firstBooking.setStartDate(Arrays.asList(2025,6,10));
//        firstBooking.setEndDate(Arrays.asList(2025,6,10));
//        firstBooking.setStatus("SCHEDULED");
//
//        Booking.Guest guest = new Booking.Guest();
//        guest.setFirstName("John");
//        guest.setLastName("Doe");
//        guest.setDateOfBirth(Arrays.asList(1990, 1, 1));
//        firstBooking.setGuest(guest);
//        firstBooking.setPropertyId("1567d5fe-c59c-4a9d-ac86-74473090534a");
//        Response firstBookingResponse = given()
//                .spec(userRequestSpec)
//                .contentType("application/json")
//                .body(firstBooking)
//                .when()
//                .post("/bookings");
//
//        JsonPath firstResponsePath = firstBookingResponse.jsonPath();
//
//        Assertions.assertEquals(422, firstBookingResponse.statusCode());
////        Assertions.assertNotNull(firstResponsePath.getString("id"));
////        Assertions.assertEquals("SCHEDULED", firstResponsePath.getString("status"));
////        Assertions.assertEquals("John", firstResponsePath.getString("guest.firstName"));
////        Assertions.assertEquals("Doe", firstResponsePath.getString("guest.lastName"));
//
//        firstResponsePath.prettyPrint();
//
//        //Booking secondBooking = createBooking(new Integer[]{2025,6,5}, new Integer[]{2025,6,5});
//        Booking secondBooking = new Booking();
//        secondBooking.setId("1567d5fe-c59c-4a9d-ac86-74473090534a");
//        secondBooking.setStartDate(Arrays.asList(2025,6,10));
//        secondBooking.setEndDate(Arrays.asList(2025,6,10));
//        secondBooking.setStatus("SCHEDULED");
//
//        Booking.Guest guest1 = new Booking.Guest();
//        guest.setFirstName("John");
//        guest.setLastName("Doe");
//        guest.setDateOfBirth(Arrays.asList(1990, 1, 1));
//        firstBooking.setGuest(guest1);
//        firstBooking.setPropertyId("1567d5fe-c59c-4a9d-ac86-74473090534a");
//        Response secondBookingResponse = given()
//                .spec(userRequestSpec)
//                .contentType("application/json")
//                .body(secondBooking)
//                .when()
//                .post("/bookings");
//
//        JsonPath secondResponsePath = secondBookingResponse.jsonPath();
//
//        Assertions.assertEquals(422, secondBookingResponse.statusCode());
//        Assertions.assertNotNull(secondResponsePath.getString("id"));
//        Assertions.assertEquals(422, secondResponsePath.getInt("status"));
//        Assertions.assertEquals("Supplied booking is not valid", secondResponsePath.getString("detail"));
//        Assertions.assertEquals("BOOKING_DATES_UNAVAILABLE", secondResponsePath.getString("BOOKING_DATES_UNAVAILABLE"));
//
//        secondBookingResponse.prettyPrint();
//
//
//
//    }

    private Booking createBooking(Integer[] startDate, Integer[] endDate) {
        Booking booking = new Booking();
        booking.setId("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        booking.setStartDate(Arrays.asList(2025,6,3));
        booking.setEndDate(Arrays.asList(2025,6,3));
        booking.setStatus("SCHEDULED");

        Booking.Guest guest = new Booking.Guest();
        guest.setFirstName("John");
        guest.setLastName("Doe");
        guest.setDateOfBirth(Arrays.asList(1990,1,1));
        booking.setGuest(guest);


        return booking;
    }

}
