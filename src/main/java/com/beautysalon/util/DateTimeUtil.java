package com.beautysalon.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtil {
    public static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final ZoneId SALON_ZONE = ZoneId.of("Asia/Colombo");

    public static String formatDate(LocalDate date) {
        return date == null ? "" : date.format(DATE_FMT);
    }

    public static String formatTime(LocalTime time) {
        return time == null ? "" : time.format(TIME_FMT);
    }

    public static String formatDateTime(LocalDateTime dt) {
        return dt == null ? "" : dt.format(DATETIME_FMT);
    }

    public static LocalDate parseDate(String s) {
        if (s == null || s.isBlank()) return null;
        try { return LocalDate.parse(s.trim(), DATE_FMT); }
        catch (DateTimeParseException e) { return null; }
    }

    public static LocalTime parseTime(String s) {
        if (s == null || s.isBlank()) return null;
        try { return LocalTime.parse(s.trim(), TIME_FMT); }
        catch (DateTimeParseException e) { return null; }
    }

    public static LocalDateTime parseDateTime(String s) {
        if (s == null || s.isBlank()) return null;
        try { return LocalDateTime.parse(s.trim(), DATETIME_FMT); }
        catch (DateTimeParseException e) { return null; }
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(SALON_ZONE);
    }

    public static LocalDate today() {
        return LocalDate.now(SALON_ZONE);
    }
}
