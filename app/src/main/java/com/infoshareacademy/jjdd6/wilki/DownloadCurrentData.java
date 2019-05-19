package com.infoshareacademy.jjdd6.wilki;

import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestScoped
public class DownloadCurrentData {

    private static Logger logger = LoggerFactory.getLogger(DownloadCurrentData.class);

    public List<DataFromFile> getMostTradedVolume() throws MalformedURLException {

        logger.info("Downloading stats");
        return parse(new URL("https://stooq.pl/q/l/?s=_stat_plws_tr_+_stat_plws_nt_&f=sd2t2ohlcm2vr&h&e=csv"));
    }

    public List<DataFromFile> get(String ticker) throws MalformedURLException {

        logger.info("Downloading current " + ticker + " data...");
        return parse(new URL("https://stooq.pl/q/l/?s=" + ticker.toLowerCase() + "&f=sd2t2ohlcv&h&e=csv"));
    }

    public List<DataFromFile> getPE(String ticker) throws MalformedURLException {

        logger.info("Downloading current " + ticker + " data...");
        return parse(new URL("https://stooq.pl/q/l/?s=" + ticker.toLowerCase() + "_pe&f=sd2t2ohlc&h&e=csv"));
    }

    public List<DataFromFile> parse(URL file) {
        List<String[]> dataLoaded = readFromURL(file);
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
                    if (!file.getFile().contains("_pe") && !file.getFile().contains("stat")) {
                        dataFromFile.setVolume(Long.parseLong(a[7]));
                    }
                    if (file.getFile().contains("stat")) {
                        dataFromFile.setChange(a[7]);
                        dataFromFile.setVolume(Long.parseLong(a[8]));
                        dataFromFile.setTurnover(Long.parseLong(a[9]));
                    }

                    return dataFromFile;
                }).
                        collect(Collectors.toList());
    }

    public List<DataFromFile> parseHistory(URL file) {
        logger.info("Parse history from " + file.toString());
        List<String[]> dataLoaded = readFromURL(file);
        logger.info("Read " + dataLoaded.size() + " entries, parsing...");
        return dataLoaded.stream()
                .skip(1)
                .map(a -> {
                    DataFromFile dataFromFile = new DataFromFile();
                    dataFromFile.setDate(LocalDate.parse(a[0]));
                    dataFromFile.setOpeningPrice(new BigDecimal((a[1])));
                    dataFromFile.setHighestPrice(new BigDecimal((a[2])));
                    dataFromFile.setLowestPrice(new BigDecimal((a[3])));
                    dataFromFile.setClosingPrice(new BigDecimal(a[4]));
                    dataFromFile.setVolume(Long.parseLong(a[5]));
                    return dataFromFile;
                }).
                        collect(Collectors.toList());
    }

    public List readFromURL(URL file) {

        List<String[]> allData = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(file.openStream()));
            allData = new CSVReader(in).readAll();
            logger.info("Read " + allData.size() + " entries from " + file.toString());
        } catch (IOException e) {
            logger.error("Unable to read from URL " + file);
        }
        return allData;
    }

    public String loadAndScanTickers(String ticker) {

        try {
            URL tickersURL = new URL("https://raw.githubusercontent.com/stockwallet/tickers/master/tickers.csv");
            logger.info("Loading tickers from " + tickersURL);
            List<String[]> dataLoaded = new DownloadCurrentData().readFromURL(tickersURL);
            Map<String, String> tickersMap = dataLoaded.stream()
                    .collect(Collectors.toMap(l -> l[0], l -> l[1]));
            return tickersMap.get(ticker);
        } catch (MalformedURLException e) {
            logger.error("Error while reading tickers");
        }
        return null;
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

    public static void updateWalletData(Wallet wallet) {
        wallet.getShares().stream()
                .forEach(Share::pullExternalData);
        logger.info("Wallet updated");
    }
}