package com.infoshareacademy.jjdd6.properties;

import com.infoshareacademy.jjdd6.wilki.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@RequestScoped
public class WebAppProperties {

    private static final Logger logger = LoggerFactory.getLogger(WebAppProperties.class);

    private Properties setupFacebookLogon() {

        String CONFIG_PROPERTIES_FILENAME = "facebooklogon.properties";
        String propertyFilePath = Thread.currentThread().getContextClassLoader().getResource(CONFIG_PROPERTIES_FILENAME).getPath();
        logger.info(propertyFilePath);

        try {
            Properties properties = new Properties();
            logger.info("Loading config: " + getClass().getClassLoader().getResource(CONFIG_PROPERTIES_FILENAME).getPath());
            properties.load(getClass().getClassLoader().getResourceAsStream(CONFIG_PROPERTIES_FILENAME));
            logger.info("Facebook logon config loaded successfully");
            return properties;
        } catch (IOException e) {
            logger.error("Error while loading Facebook logon config: " + e.getMessage());
            return null;
        }
    }

    public String getProperty(String property) {
        return setupFacebookLogon().getProperty(property);
    }
}