package com.infoshareacademy.jjdd6.wilki;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class Loggers {

    private static FileHandler fileHandler = null;

    public static void init() {
        try {
            fileHandler = new FileHandler("loggerFile.log", false);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger("");
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
    }
}