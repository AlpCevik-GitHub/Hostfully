package com.hostfully.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;


public class HostfullyUtil {

    public static String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "parameterizedCsvFile.csv").toString();
    public static String pathID = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "parameterizedBookingIdCSVFile.csv").toString();


    public static void appendToCSV(String filePath, String id,String name,String date) throws IOException {

        File file = new File(path);
        boolean fileExists = file.exists();
        boolean isEmpty = fileExists && Files.size(Paths.get(filePath)) == 0;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            if (!fileExists || isEmpty) {
                bw.write("id" + "," + "name" + "," + "date");
                bw.newLine();
            }

            bw.write(String.join(",", id, name,date));
            bw.newLine();
        }
    }

    public static void appendToCSVForID(String filePath, String id) throws IOException {

        File file = new File(filePath);
        boolean fileExists = file.exists();
        boolean isEmpty = fileExists && Files.size(Paths.get(filePath)) == 0;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            if (!fileExists || isEmpty) {
                bw.write("id");
                bw.newLine();
            }

            bw.write(id);
            bw.newLine();
        }
    }

    public static void clearCSV(String filePath) throws IOException {
        Files.write(Paths.get(filePath), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
    }


        public static void main(String[] args) {

            List<Integer> createdAtJson = List.of(2025, 1, 21, 19, 53, 51, 161969000);
            List<Integer> createdAtCsv = List.of(2025, 2, 4, 19, 7, 23, 870255000);
             String dateStr = "[2025,2,4,19,7,23,870255000]";
             dateStr = dateStr.replaceAll(" ","");
            List<Integer> dateList = Arrays.stream(dateStr.replaceAll("[\\[\\]]", "").split(","))
                    .map(Integer::parseInt)
                    .toList();
            System.out.println("dateList.toString() = " + dateList.toString());

            String str = "sasderkenar";
            String result = "";

            for (int i = str.length()-1; i >=0 ; i--) {

                result += str.charAt(i);

            }
            System.out.println("result = " + result);


        }


        public static boolean compareDates(List<Integer> date1, List<Integer> date2) {
            LocalDateTime dateTime1 = toLocalDateTime(date1);
            LocalDateTime dateTime2 = toLocalDateTime(date2);

            return dateTime1.equals(dateTime2);
            }



        public static LocalDateTime toLocalDateTime(List<Integer> dateList) {
            return LocalDateTime.of(dateList.get(0), dateList.get(1), dateList.get(2),
                            dateList.get(3), dateList.get(4), dateList.get(5))
                    .plusNanos(dateList.get(6));
        }

    public static String parseDateString(String dateStr) {

        List<Integer> dateList = Arrays.stream(dateStr.replaceAll("[\\[\\]\\s]", "").split(","))
                .map(Integer::parseInt)
                .toList();


        LocalDateTime dateTime = LocalDateTime.of(
                dateList.get(0), dateList.get(1), dateList.get(2),
                dateList.get(3), dateList.get(4), dateList.get(5), dateList.get(6)
        );

        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }

    public static List<Integer> generateRandomStartDate() {
        Random random = new Random();


        LocalDate today = LocalDate.now();
        int randomMonthOffset = random.nextInt(12) + 1;
        LocalDate randomMonthDate = today.plusMonths(randomMonthOffset);

        int year = randomMonthDate.getYear();
        int month = randomMonthDate.getMonthValue();

        int maxDayOfMonth = YearMonth.of(year, month).lengthOfMonth();
        int day = random.nextInt(maxDayOfMonth) + 1;

        return Arrays.asList(year, month, day);
    }

    public static boolean isDateOverlap(List<Integer> startDate1, List<Integer> endDate1, List<Integer> startDate2, List<Integer> endDate2) {

        LocalDate start1 = toValidDate(startDate1);
        LocalDate end1 = toValidDate(endDate1);
        LocalDate start2 = toValidDate(startDate2);
        LocalDate end2 = toValidDate(endDate2);


        return !(end1.isBefore(start2) || end2.isBefore(start1));
    }
    private static LocalDate toValidDate(List<Integer> date) {
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
    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
    }

    public static List<Integer> toValidDateList(List<Integer> date) {
        try {
            int year = date.get(0);
            int month = date.get(1);
            int day = date.get(2);


            int maxDays = getMaxDaysInMonth(year, month);

            if (date.get(1) == 2 && date.get(2) >= 29) {
                if (!isLeapYear(date.get(0))) {

                    System.out.println("Invalid Date: " + date);
                    return Arrays.asList(date.get(0), 2, 28);
                }
            }
            if (day <= maxDays) {
                return Arrays.asList(year, month, day);
            }

            while (day > maxDays) {
                day -= maxDays;
                month++;

                if (month > 12) {
                    month = 1;
                    year++;
                }

                maxDays = getMaxDaysInMonth(year, month);
            }

            return Arrays.asList(year, month, day);


        } catch (DateTimeException e) {

            System.out.println("Invalid Date: " + date);
            return Arrays.asList(date.get(0), date.get(1), 1);
        }
    }

    private static int getMaxDaysInMonth(int year, int month) {
        switch (month) {
            case 2:
                return isLeapYear(year) ? 29 : 28;
            case 4: case 6: case 9: case 11:
                return 30;
            default:
                return 31;
        }
    }



}
