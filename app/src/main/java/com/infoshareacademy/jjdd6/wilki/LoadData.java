package com.infoshareacademy.jjdd6.wilki;

import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LoadData {

    private static Logger logger = LoggerFactory.getLogger(LoadData.class);

    private List read(String file) {

        List<String[]> allData = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(file);
            CSVReader csvReader = new CSVReader(fileReader);
            allData = csvReader.readAll();
        } catch (IOException e) {
            logger.error("Unable to read file " + file);
        }
        return allData;
    }

    public List<DataFromFile> loadToList(String filename) {

        String path = DownloadData.DEFAULT_DOWNLOAD_LOCATION + filename;
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
                    if (!filename.contains("_pe.csv")) {
                        dataFromFile.setVolume(Long.parseLong(a[7]));
                    }
                    return dataFromFile;
                }).
                        collect(Collectors.toList());
    }


    public String loadAndScanTickers(String ticker) {

        LoadData loadData = new LoadData();
        List<String[]> dataLoaded = loadData.read(Thread.currentThread().getContextClassLoader().getResource("tickers.csv").getPath());
        Map<String, String> tickersMap = dataLoaded.stream()
                .collect(Collectors.toMap(l -> l[0], l -> l[1]));
        return tickersMap.get(ticker);
    }

    public boolean validateTicker(String ticker) {
        try {
            if (loadAndScanTickers(ticker.toUpperCase()) != null) {
                return true;
            }
        } catch (Exception e) {
        }
        logger.error("Ticker " + ticker.toUpperCase() + " not valid");
        return false;
    }
}