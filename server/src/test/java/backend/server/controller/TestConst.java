package backend.server.controller;

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
}
