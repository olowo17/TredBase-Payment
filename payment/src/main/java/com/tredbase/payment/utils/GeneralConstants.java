package com.tredbase.payment.utils;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static java.time.format.DateTimeFormatter.ofPattern;

public class GeneralConstants {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final long EXPIRATION_TIME = 18000_000;
    public static final String HEADER_STRING = "Authorization";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = ofPattern(DATETIME_FORMAT, Locale.US);
    public static final LocalDateTimeSerializer LOCAL_DATE_TIME_SERIALIZER = new LocalDateTimeSerializer(LOCAL_DATE_TIME_FORMATTER);
    public static final BigDecimal PROCESSING_FEE_DISCOUNTED_RATE = BigDecimal.valueOf(0.035);
    public static final BigDecimal PROCESSING_FEE_DEFAULT_RATE = BigDecimal.valueOf(0.040);

}