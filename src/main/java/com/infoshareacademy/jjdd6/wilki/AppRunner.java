package com.infoshareacademy.jjdd6.wilki;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppRunner {

    private final static Logger logger = Logger.getLogger(Logger.getLogger("").getClass().getName());

    public void run() {

        Loggers.init();
        logger.log(Level.INFO, "LoggerFile initialized properly.");

        String s = AppProperties.getDateFormat().format(LocalDate.now());
        System.out.println(s);

        LocalDate date1 = LocalDate.of(2019, 5, 15);
        String date = date1.format(AppProperties.getDateFormat());
        System.out.println(date);
        logger.log(Level.INFO, "Property formatted date printed.");

    }
}