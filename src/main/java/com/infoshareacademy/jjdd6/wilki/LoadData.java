package com.infoshareacademy.jjdd6.wilki;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LoadData {
    public List read(String file) {

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

    public List<DataFromFile> loadToList(String[] args) {
        if (args.length != 1) {
            System.out.println("Prosze podac sciezke do danych");
            System.exit(1);
        }

        String path = args[0];
        System.out.println("Podana sciezka to: " + path);

//        try {
            LoadData loadData = new LoadData();
            List<String[]> dataLoaded = loadData.read(path);
            List<DataFromFile> collect = dataLoaded.stream()
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
            return collect;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
