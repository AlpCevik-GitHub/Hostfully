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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;


public class HostfullyUtil {

    public static String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "parameterizedCsvFile.csv").toString();


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

    public static void clearCSV() throws IOException {
        Files.write(Paths.get(path), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
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
        LocalDate randomStartDate = today.plusDays(random.nextInt(363) + 1);

        return Arrays.asList(
                randomStartDate.getYear(),
                randomStartDate.getMonthValue(),
                randomStartDate.getDayOfMonth()
        );
    }





}
