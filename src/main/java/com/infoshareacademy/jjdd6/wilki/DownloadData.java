package com.infoshareacademy.jjdd6.wilki;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class DownloadData {

    public static void getCurrentCSV(String ticker) {
        if (new LoadData().validateTicker(ticker.toUpperCase())) {
            System.out.println("INFO: Downloading current " + ticker + " data...");
            download(ticker.toLowerCase() + ".csv", "https://stooq.pl/q/l/?s=" + ticker.toLowerCase() + "&f=sd2t2ohlcv&h&e=csv");
            download(ticker.toLowerCase() + "_pe.csv", "https://stooq.pl/q/l/?s=" + ticker.toLowerCase() + "_pe&f=sd2t2ohlc&h&e=csv");
        }
        else {
            System.out.println("ERROR: Ticker not valid");
        }
    }

    private static void download(String filename, String url) {
        try (
                BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
                FileOutputStream fileOS = new FileOutputStream("./data/" + filename)) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            System.out.println("ERROR: No connection with remote database");
        }
    }

    public static void updateWalletData(Wallet wallet) {
        System.out.println("INFO: Updating wallet...");

        wallet.getShares().stream()
                .map(Share::getTicker)
                .forEach(DownloadData::getCurrentCSV);

        wallet.getShares().stream()
                .forEach(Share::pullExternalData);
        System.out.println("INFO: Wallet data updated");
    }
}
