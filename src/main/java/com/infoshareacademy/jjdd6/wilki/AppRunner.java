package com.infoshareacademy.jjdd6.wilki;

import java.io.File;
import java.nio.file.Path;

public class AppRunner {
    public void run(String[] args) {
//        if (args.length != 1) {
//            System.out.println("Prosze podac sciezke do danych");
//            System.exit(1);
//        }
//
//        String path = args[0];
//        System.out.println("Podana sciezka to: " + path);

        LoadData loadData = new LoadData();
        File folder = new File("./data");
        loadData.listFilesForFolder(folder);
        //loadData.loadToList("/data/wig20.csv");

    }
}
