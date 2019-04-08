package com.infoshareacademy.jjdd6.wilki;

import java.time.LocalDate;

import java.io.File;

public class AppRunner {
    public void run() {


        LoadData loadData = new LoadData();
        File folder = new File("./data");
        loadData.listFilesForFolder(folder);


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
