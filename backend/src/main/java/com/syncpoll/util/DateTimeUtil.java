package com.syncpoll.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    private static final DateTimeFormatter DISPLAY_FORMAT = 
        DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
    
    private static final DateTimeFormatter ISO_FORMAT = 
        DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static String formatForDisplay(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DISPLAY_FORMAT) : null;
    }

    public static String formatISO(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(ISO_FORMAT) : null;
    }
}
