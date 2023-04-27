package com.dk0124.respos.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class TimeUtil {

    public static LocalDateTime longToLocalDateTime(Long millis){
        Instant instant = Instant.ofEpochMilli(millis);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Long localDateTimeToLong(LocalDateTime time){
        return time.toEpochSecond(ZoneOffset.UTC) * 1000;
    }
}
