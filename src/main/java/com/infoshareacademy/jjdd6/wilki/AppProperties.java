package com.infoshareacademy.jjdd6.wilki;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class AppProperties {

    private static final String DATE_FORMAT = "dateFormat";
    private static final String CONFIG_PROPERTIES_FILENAME = "config.properties";

    public String getProperties() {

        Properties properties = new Properties();
        InputStream input = null;

        String dateFormat = null;
        try {
            input = new FileInputStream(CONFIG_PROPERTIES_FILENAME);

            properties.load(input);

            dateFormat = properties.getProperty(DATE_FORMAT);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dateFormat;
    }

    public void checkDate() {
        AppProperties appProperties = new AppProperties();
        try {
            new SimpleDateFormat(appProperties.getProperties());
        } catch (IllegalArgumentException e) {
            System.out.println("Error while loading " +
                    "\"Illegal pattern character 'x'\" " +
                    "(dateFormat in config.properties)");
        }
    }
}