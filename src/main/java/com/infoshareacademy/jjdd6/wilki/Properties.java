package com.infoshareacademy.jjdd6.wilki;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class Properties {

    public String getPropertiesValues() throws IOException {

        java.util.Properties properties = new java.util.Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("config.properties");

            properties.load(input);

            Date time = new Date(System.currentTimeMillis());

            // get the property value and print it out
            String name = properties.getProperty("ApplicationName");
            String output = "\nWelcome to " + name;

            System.out.println(output + "\nApplication ran on " + time);

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
        return null;
    }
}