package com.infoshareacademy.jjdd6.wilki;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerTest2 {

    private final static Logger logger = Logger.getLogger(Logger.getLogger("").getClass().getName());

    public static void thing(){
        logger.log(Level.WARNING,"something to log");
    }
}
