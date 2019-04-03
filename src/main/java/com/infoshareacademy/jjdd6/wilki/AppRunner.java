package com.infoshareacademy.jjdd6.wilki;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class AppRunner {
    public void run() {

//        String appName = Properties.getPropertyValue(Properties.APPLICATION_NAME);

        String dateFormat = Properties.getPropertyValue(Properties.DATE_FORMAT);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Objects.requireNonNull(dateFormat));

        Date localDate = new Date();

//        String localDate = simpleDateFormat.format(LocalDate.now());

        String date = simpleDateFormat.format(localDate);

        System.out.println(date);
    }
}