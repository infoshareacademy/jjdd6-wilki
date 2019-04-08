package com.infoshareacademy.jjdd6.wilki;

import java.io.File;

public class AppRunner {
    public void run() {


        LoadData loadData = new LoadData();
        File folder = new File("./data");
        loadData.listFilesForFolder(folder);


    }
}
