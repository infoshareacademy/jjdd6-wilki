package com.infoshareacademy.jjdd6.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import java.util.Properties;

@RequestScoped
public class WebAppProperties {

    private static final Logger logger = LoggerFactory.getLogger(WebAppProperties.class);

    private Properties setupFacebookLogon() {

        String CONFIG_PROPERTIES_FILENAME = "facebooklogon.properties";
        return loadProperties(CONFIG_PROPERTIES_FILENAME);
    }

    private Properties saveDir() {

        String CONFIG_PROPERTIES_FILENAME = "directory.properties";
        return loadProperties(CONFIG_PROPERTIES_FILENAME);
    }

    private Properties loadProperties(String CONFIG_PROPERTIES_FILENAME) {
        String propertyFilePath = Thread.currentThread().getContextClassLoader().getResource(CONFIG_PROPERTIES_FILENAME).getPath();

        try {
            Properties properties = new Properties();
            logger.info("Loading config: " + getClass().getClassLoader().getResource(CONFIG_PROPERTIES_FILENAME).getPath());
            properties.load(getClass().getClassLoader().getResourceAsStream(CONFIG_PROPERTIES_FILENAME));
            return properties;
        } catch (IOException e) {
            logger.error("Error while loading Facebook logon config: " + e.getMessage());
            return null;
        }
    }

    public String getProperty(String property) {
        return setupFacebookLogon().getProperty(property);
    }

    public String getSaveDir(String property) {
        return saveDir().getProperty(property);
    }

}
