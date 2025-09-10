package com.szd.boxgo.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    public static final String OFFSET_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String LOCAL_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String LOCALZ_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DATEZ_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static LocalDateTime stringToLocal(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LOCAL_PATTERN);
        return LocalDateTime.parse(date, formatter);
    }

    public static String offsetToString(OffsetDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(OFFSET_PATTERN);
        return formatter.format(dateTime);
    }

    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATEZ_PATTERN);

        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sdf.format(date);
    }

    public static String localToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LOCAL_PATTERN);
        return formatter.format(dateTime);
    }

    public static String localzToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LOCALZ_PATTERN);
        return formatter.format(dateTime);
    }

    public static LocalDateTime offsetToLocal(OffsetDateTime dateTime) {
        return dateTime
                .atZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDateTime toLocalDate(OffsetDateTime utcTs, String zoneId) {
        return utcTs.atZoneSameInstant(ZoneOffset.UTC)
                .withZoneSameInstant(ZoneId.of(zoneId))
                .toLocalDateTime();
    }

    public static OffsetDateTime local2359ToUtc(LocalDate localDate, String zoneId) {
        ZonedDateTime zdt = ZonedDateTime.of(localDate, LocalTime.of(23,59), ZoneId.of(zoneId));
        return zdt.withZoneSameInstant(ZoneOffset.UTC).toOffsetDateTime();
    }
}
