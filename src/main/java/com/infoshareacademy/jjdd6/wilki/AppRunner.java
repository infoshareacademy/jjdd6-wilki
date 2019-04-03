package com.infoshareacademy.jjdd6.wilki;

import java.io.IOException;

public class AppRunner {
    public void run() throws IOException {

        AppProperties appProperties = new AppProperties();
        appProperties.getProperties(AppProperties.DATE_FORMAT);
    }

//        String appName = AppProperties.getPropertyValue(AppProperties.APPLICATION_NAME);

//        String dateFormat = AppProperties.getPropertyValue(AppProperties.DATE_FORMAT);
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Objects.requireNonNull(dateFormat));
////
//        Date localDate = new Date();
////
////        String localDate = simpleDateFormat.format(LocalDate.now());
////
//        String date = simpleDateFormat.format(localDate);
////
//        System.out.println(date);
//    }
}