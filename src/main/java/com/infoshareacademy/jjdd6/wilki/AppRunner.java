package com.infoshareacademy.jjdd6.wilki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;

public class AppRunner {

    private static Logger logger = LoggerFactory.getLogger(AppRunner.class.getName());

    public void run() {

        LoadData loadData = new LoadData();
        File folder = new File("./data");
        loadData.listFilesForFolder(folder);

        System.out.println("---");

        String s = AppProperties.getDateFormat().format(LocalDate.now());
        System.out.println(s);
        LocalDate date1 = LocalDate.of(2019, 5, 15);
        String date = date1.format(AppProperties.getDateFormat());
        System.out.println(date);
        logger.info("Property formatted date printed.");

        System.out.println("---");

        TextInterface textInterface = new TextInterface();
        textInterface.drawMenu();
        textInterface.chooseOption();

    }
}