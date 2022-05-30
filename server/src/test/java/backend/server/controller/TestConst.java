package backend.server.controller;

import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TestConst {

    static String dateToString(LocalDate date) {
        return "[" + date.getYear() + "," + date.getMonthValue() + "," + date.getDayOfMonth() + "]";
    }

    static String dateTimeToString(LocalDateTime dateTime) {
        return "[" + dateTime.getYear() + "," + dateTime.getMonthValue() + "," + dateTime.getDayOfMonth() + "," +
                dateTime.getHour() + "," + dateTime.getMinute() + "," + dateTime.getSecond() + "," + dateTime.getNano() + "]";
    }

    static String timeToString(LocalTime time) {

        return "[" + time.getHour() + "," + time.getMinute() + "," + time.getSecond() + "," + time.getNano() + "]";
    }


    static MockMultipartFile getMockMultipartFile() throws IOException {

        String fileName = "name";
        String contentType = "png";
        String filePath = "src/test/java/imageFiles/ActivityStartImage.png";

        FileInputStream fileInputStream = new FileInputStream(new File(filePath));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }
}
