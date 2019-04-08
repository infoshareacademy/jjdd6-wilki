package com.infoshareacademy.jjdd6.wilki;

import java.time.LocalDate;

public class AppRunner {
    public void run() {

        String s = AppProperties.getDateFormat().format(LocalDate.now());
        System.out.println(s);

        LocalDate date1 = LocalDate.of(2019, 5, 15);
        String date = date1.format(AppProperties.getDateFormat());
        System.out.println(date);

        TextInterface textInterface = new TextInterface();
        textInterface.drawMenu();
        textInterface.chooseOption();

    }

}



