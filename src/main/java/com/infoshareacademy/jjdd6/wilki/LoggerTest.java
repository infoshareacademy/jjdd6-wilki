package com.infoshareacademy.jjdd6.wilki;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerTest {

    private final static Logger logger = Logger.getLogger(Logger.getLogger("").getClass().getName());
    private static FileHandler fileHandler = null;

    public static void init() {
        try {
            fileHandler = new FileHandler("loggerFile.log", false);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        Logger logger = Logger.getLogger("");
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setLevel(Level.CONFIG);
    }

    public static void main(String[] args) {
        LoggerTest.init();

        logger.log(Level.INFO, "message 1");
        logger.log(Level.SEVERE, "message 2");
        logger.log(Level.FINE, "message 3");

        LoggerTest2.thing();

    }
}
