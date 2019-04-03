package com.infoshareacademy.jjdd6.wilki;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadData {
    public List read(String file) throws IOException {
//        String filename = Properties.getValue(wig_20);
        String userDir = System.getProperty("user.dir");
//        Path path = Paths.get(userDir,"Downloads",filename);
//       List<String> lines = Files.readAllLines(path);
        List<String[]> allData = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(file);
            CSVReader csvReader = new CSVReader(fileReader);
            allData = csvReader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allData;
    }

    public static void main(String[] args) {
        try {
            LoadData loadData = new LoadData();
            List dataLoaded = loadData.read("/home/ewelina/Downloads/wig20.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
