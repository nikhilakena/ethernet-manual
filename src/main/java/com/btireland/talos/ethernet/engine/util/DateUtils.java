package com.btireland.talos.ethernet.engine.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    static final String SLASH_DAY_FIRST_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    static final String SLASH_DAY_FIRST_DATE_FORMAT = "dd/MM/yyyy";

    public static LocalDateTime btStringToDateTime(String date) {
        if(date == null || date.equals("")){
            return null;
        }
        DateTimeFormatter btFormatter = DateTimeFormatter.ofPattern(SLASH_DAY_FIRST_DATE_TIME_FORMAT);
        return LocalDateTime.parse(date, btFormatter);
    }

    public static LocalDate btStringToDate(String date) {
        if(date == null || date.equals("")){
            return null;
        }
        DateTimeFormatter btFormatter = DateTimeFormatter.ofPattern(SLASH_DAY_FIRST_DATE_FORMAT);
        return LocalDate.parse(date, btFormatter);
    }

    public static String btDateTimeToString(LocalDateTime date) {
        if(date == null ){
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(SLASH_DAY_FIRST_DATE_TIME_FORMAT));
    }

    public static String isoDateTimeToString(LocalDateTime date) {
        if(date == null ){
            return null;
        }
        return date.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public static LocalDateTime isoStringToDateTime(String date) {
        if(date == null || date.equals("")){
            return null;
        }
       return LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    }

    public static String btDateToString(LocalDate date) {
        if(date == null ){
            return null;
        }
        DateTimeFormatter btFormatter = DateTimeFormatter.ofPattern(SLASH_DAY_FIRST_DATE_FORMAT);
        return date.format(btFormatter);
    }

    public static String btDateTimeToStringWithPattern(LocalDateTime date,String pattern) {
        if(date == null ){
            return null;
        }
        DateTimeFormatter btFormatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(btFormatter);
    }

}
