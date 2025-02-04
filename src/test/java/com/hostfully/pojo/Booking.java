package com.hostfully.pojo;

import java.util.List;

public class Booking {

    private String id;
    private List<Integer> startDate;
    private List<Integer> endDate;
    private String status;
    private Guest guest;
    private String propertyId;

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", guest=" + (guest != null ? guest.toString() : "null") +
                ", propertyId='" + propertyId + '\'' +
                '}';
    }

    // Getter ve Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getStartDate() {
        return startDate;
    }

    public void setStartDate(List<Integer> startDate) {
        this.startDate = startDate;
    }

    public List<Integer> getEndDate() {
        return endDate;
    }

    public void setEndDate(List<Integer> endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    // Nested Guest
    public static class Guest {
        private String firstName;
        private String lastName;
        private List<Integer> dateOfBirth;

        // Getter ve Setter
        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public List<Integer> getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(List<Integer> dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        @Override
        public String toString() {
            return "Guest{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", dateOfBirth=" + dateOfBirth +
                    '}';
        }
    }
}
