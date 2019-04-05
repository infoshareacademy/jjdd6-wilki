package com.infoshareacademy.jjdd6.wilki;

import java.io.FileInputStream;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppProperties {

    private final static Logger logger = Logger.getLogger(Logger.getLogger("").getClass().getName());

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
            logger.log(Level.INFO, "Configuration file loading properly.");
        } catch (Exception e) {
            System.err.println("Error while loading configuration: " + e.getMessage());
            logger.log(Level.WARNING, "Error while loading configuration: " + e.getMessage());
            dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
            System.err.println("Using default value: " + DEFAULT_DATE_FORMAT);
            logger.log(Level.INFO, "Default DateTimeFormatter created.");
        }
    }

    private AppProperties() {

    }

    public static DateTimeFormatter getDateFormat() {
        return dateTimeFormatter;
    }

}