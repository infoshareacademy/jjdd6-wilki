package com.infoshareacademy.jjdd6.wilki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class AppProperties {

    private static Logger logger = LoggerFactory.getLogger(AppProperties.class);

    private static final String DATE_FORMAT = "dateFormat";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String CONFIG_PROPERTIES_FILENAME = "config.properties";
    private static final String CONFIG_PROPERTIES_PATH = Thread.currentThread().getContextClassLoader().getResource(CONFIG_PROPERTIES_FILENAME).getPath();

    private static DateTimeFormatter dateTimeFormatter;

    static {

        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(CONFIG_PROPERTIES_PATH));
            String dateFormat = properties.getProperty(DATE_FORMAT);
            dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
            logger.info("Configuration file loaded properly.");
        } catch (Exception e) {
            System.err.println("Error while loading configuration: " + e.getMessage());
            logger.error("Error while loading configuration: " + e.getMessage());
            dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
            System.err.println("Using default value: " + DEFAULT_DATE_FORMAT);
            logger.warn("Default DateTimeFormatter created.");
        }
    }

    private AppProperties() {
    }

    public static DateTimeFormatter getDateFormat() {
        return dateTimeFormatter;
    }
}