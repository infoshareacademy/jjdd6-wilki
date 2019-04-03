package com.infoshareacademy.jjdd6.wilki;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static javax.swing.plaf.basic.BasicHTML.propertyKey;

public class AppProperties {

    public static final String DATE_FORMAT = "dateFormat";
    public static final String CONFIG_PROPERTIES_FILENAME = "config.properties";

    private Properties testProperties = null;

    public void getProperties(String property) throws IOException {
    if (testProperties == null) {
            InputStream inputStream = new FileInputStream(CONFIG_PROPERTIES_FILENAME);
            testProperties = new Properties();
            testProperties.load(inputStream);
            inputStream.close();
            }
        testProperties.getProperty(propertyKey);

    }}

//    private String getPropertyValue(String propertyKey) {
//        java.util.Properties properties = new java.util.Properties();
//
//        //try-with-resources
//        try (InputStream input = new FileInputStream(CONFIG_PROPERTIES_FILENAME)) {
//
//            properties.load(input);
//            properties.values();
//            validate(values);
//
//            String value = properties.getProperty(propertyKey);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            System.out.println("Error while loading \"Illegal pattern character 'x'\"");
//        }
//        return null;
//    }


