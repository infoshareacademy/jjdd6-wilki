package com.infoshareacademy.jjdd6.wilki;

import java.io.FileInputStream;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class AppProperties {

    private static final String DATE_FORMAT = "dateFormat";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String CONFIG_PROPERTIES_FILENAME = "config.properties";

    private static DateTimeFormatter dateTimeFormatter;

    static {

        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(CONFIG_PROPERTIES_FILENAME));
            String dateFormat = properties.getProperty(DATE_FORMAT);
            dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        } catch (Exception e) {
            System.err.println("Error while loading configuration: " + e.getMessage());
            System.err.println("Using default value: " + DEFAULT_DATE_FORMAT);
            dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        }
    }

    private AppProperties() {

    }

    public static DateTimeFormatter getDateFormat() {
        return dateTimeFormatter;
    }

}