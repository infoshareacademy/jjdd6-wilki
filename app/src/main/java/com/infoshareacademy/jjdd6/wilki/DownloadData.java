package com.infoshareacademy.jjdd6.wilki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class DownloadData {

    private static Logger logger = LoggerFactory.getLogger(AppRunner.class);
    public static final String DEFAULT_DOWNLOAD_LOCATION = "/tmp/";


    public static void getCurrentCSV(String ticker) {
        if (new LoadData().validateTicker(ticker.toUpperCase())) {
            logger.info("Downloading current " + ticker + " data...");
            download(ticker.toLowerCase() + ".csv", "https://stooq.pl/q/l/?s=" + ticker.toLowerCase() + "&f=sd2t2ohlcv&h&e=csv");
            download(ticker.toLowerCase() + "_pe.csv", "https://stooq.pl/q/l/?s=" + ticker.toLowerCase() + "_pe&f=sd2t2ohlc&h&e=csv");
        } else {
            logger.error("Ticker " + ticker.toUpperCase() + " not valid");
        }
    }

    private static void download(String filename, String url) {
        String path = DEFAULT_DOWNLOAD_LOCATION + filename;

        try {
                BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
                FileOutputStream fileOS = new FileOutputStream(path);
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            logger.error("No connection with remote database");
        }
    }

    public static void updateWalletData(Wallet wallet) {
        wallet.getShares().stream()
                .map(Share::getTicker)
                .forEach(DownloadData::getCurrentCSV);

        wallet.getShares().stream()
                .forEach(Share::pullExternalData);
        logger.info("Wallet updated");
    }
}
