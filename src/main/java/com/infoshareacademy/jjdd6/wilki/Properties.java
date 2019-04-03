package com.infoshareacademy.jjdd6.wilki;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Properties {

    public static final String APPLICATION_NAME = "ApplicationName";
    public static final String DATE_FORMAT = "dateFormat";
    public static final String CONFIG_PROPERTIES_FILENAME = "config.properties";

    public static String getPropertyValue(String propertyKey) {
        java.util.Properties properties = new java.util.Properties();

        //try-with-resources
        try (InputStream input = new FileInputStream(CONFIG_PROPERTIES_FILENAME)) {

            properties.load(input);

            return properties.getProperty(propertyKey);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}