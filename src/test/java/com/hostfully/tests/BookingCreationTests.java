package com.hostfully.tests;

import com.hostfully.pojo.Booking;
import com.hostfully.utilities.HostfullyUtil;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
@import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;

import static io.restassured.RestAssured.given;

public class BookingCreationTests extends TestBase {

    @DisplayName("Create Booking with Valid Property")
    @ParameterizedTest
    @CsvFileSource(resources = "/parameterizedCsvFile.csv", numLinesToSkip = 1)
    public void bookingWithValidProperty(String id) {

        Response response = given().contentType(ContentType.JSON)
                .spec(userRequestSpec)
                .when()
                .get("/bookings");

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
                .post("/bookings");


        JsonPath jsonPath = postResponse.jsonPath();

        Assertions.assertEquals(201, postResponse.statusCode());
        Assertions.assertNotNull(jsonPath.getString("id"));
        Assertions.assertEquals("SCHEDULED", jsonPath.getString("status"));
        Assertions.assertEquals("John", jsonPath.getString("guest.firstName"));
        Assertions.assertEquals("Doe", jsonPath.getString("guest.lastName"));

        jsonPath.prettyPrint();



    }



    private boolean isDateOverlap(List<Integer> startDate1, List<Integer> endDate1, List<Integer> startDate2, List<Integer> endDate2) {

        LocalDate start1 = toValidDate(startDate1);
        LocalDate end1 = toValidDate(endDate1);
        LocalDate start2 = toValidDate(startDate2);
        LocalDate end2 = toValidDate(endDate2);


        return !(end1.isBefore(start2) || end2.isBefore(start1));
    }
    private LocalDate toValidDate(List<Integer> date) {
        try {
            if (date.get(1) == 2 && date.get(2) >= 29) {

                if (!isLeapYear(date.get(0))) {
                    System.out.println("Invalid Date: " + date);
                    return LocalDate.of(date.get(0), 2, 28);
                }
            }
            return LocalDate.of(date.get(0), date.get(1), date.get(2));
        } catch (DateTimeException e) {

            System.out.println("Invalid Date: " + date);
            return LocalDate.of(date.get(0), date.get(1), 1);
        }
    }
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
    }

    private List<Integer> toValidDateList(List<Integer> date) {
        try {

            if (date.get(1) == 2 && date.get(2) >= 29) {
                if (!isLeapYear(date.get(0))) {

                    System.out.println("Invalid Date: " + date);
                    return Arrays.asList(date.get(0), 2, 28);
                }
            }


            return Arrays.asList(date.get(0), date.get(1), date.get(2));

        } catch (DateTimeException e) {

            System.out.println("Invalid Date: " + date);
            return Arrays.asList(date.get(0), date.get(1), 1);
        }
    }






}
