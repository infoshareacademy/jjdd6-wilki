package com.infoshareacademy.jjdd6.wilki;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LoadData {

    private List read(String file) {

        List<String[]> allData = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(file);
            CSVReader csvReader = new CSVReader(fileReader);
            allData = csvReader.readAll();
        } catch (IOException e) {
            System.err.println();
        }
        return allData;
    }

    public List<File> listFilesForFolder(File folder) {
        folder = new File("./data");
        File[] arrayOfFiles = folder.listFiles();

        if (arrayOfFiles != null) {
            for (File listOfFile : arrayOfFiles) {
                if (listOfFile.isFile()) {
                    System.out.println(listOfFile.getName());
                } else if (listOfFile.isDirectory()) {
                    System.out.println("Directory " + listOfFile.getName());
                }
            }
        } else {
            System.out.println("There are no files in given directory");
        }
        return List.of(arrayOfFiles);
    }

    public String chooseFile(){
        System.out.println("Choose file: ");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        return s;
    }

    public List<DataFromFile> loadToList() {

        String path = "./data/" + chooseFile();
        LoadData loadData = new LoadData();
        List<String[]> dataLoaded = loadData.read(path);
        return dataLoaded.stream()
                .skip(1)
                .map(a -> {
                    DataFromFile dataFromFile = new DataFromFile();
                    dataFromFile.setSymbol(a[0]);
                    dataFromFile.setDate(LocalDate.parse(a[1]));
                    dataFromFile.setTime(LocalTime.parse(a[2]));
                    dataFromFile.setOpeningPrice(new BigDecimal(a[3]));
                    dataFromFile.setHighestPrice(new BigDecimal(a[4]));
                    dataFromFile.setLowestPrice(new BigDecimal(a[5]));
                    dataFromFile.setClosingPrice(new BigDecimal(a[6]));
                    dataFromFile.setVolume(Long.parseLong(a[7]));
                    return dataFromFile;
                }).collect(Collectors.toList());

    }
}
