package com.infoshareacademy.jjdd6.wilki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;

public class AppRunner {

    private static Logger logger = LoggerFactory.getLogger(AppRunner.class);

    public void run() {

        String actualDate = AppProperties.getDateFormat().format(LocalDate.now());

        System.out.println("=====================");
        System.out.println("Today is: " + actualDate);
        System.out.println("=====================");

        TextInterface textInterface = new TextInterface();
        textInterface.drawMainMenu();
    }
}

//    LoadData loadData = new LoadData();
//    File folder = new File("./data");
//    loadData.listFilesForFolder(folder);