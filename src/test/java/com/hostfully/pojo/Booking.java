package com.hostfully.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Booking {

    @JsonProperty("id")
    private String id;
    @JsonProperty("startDate")
    private List<Integer> startDate;
    @JsonProperty("endDate")
    private List<Integer> endDate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("guest")
    private Guest guest;
    @JsonProperty("propertyId")
    private String propertyId;

    private static List<String> listPropertyId = new ArrayList<>();

    public static void getListPropertyId() {
        Booking.listPropertyId.forEach(System.out::println);
    }
    public static void setListPropertyId(String propertyId) {
        Booking.listPropertyId.add(propertyId);
    }

    // Nested Guest
    @Setter
    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Guest {

        @JsonProperty("firstName")
        private String firstName;
        @JsonProperty("lastName")
        private String lastName;
        @JsonProperty("dateOfBirth")
        private List<Integer> dateOfBirth;

    }
}
