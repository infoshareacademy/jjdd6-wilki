package com.infoshareacademy.jjdd6.service;

import com.infoshareacademy.jjdd6.properties.WebAppProperties;
import com.infoshareacademy.jjdd6.wilki.DataFromFile;
import com.infoshareacademy.jjdd6.wilki.DownloadCurrentData;
import com.infoshareacademy.jjdd6.wilki.Ticker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class DownloaderService {

    private static Logger logger = LoggerFactory.getLogger(DownloaderService.class);


    @Inject
    private WebAppProperties webAppProperties;

    @Inject
    private TickerService tickerService;

    @Inject
    private DownloadCurrentData downloadCurrentData;

    public void downloadAllFiles() {
        List<Ticker> tickerList = tickerService.getAll();
        tickerList.stream().map(o -> o.getTickerName()).forEach(o -> {
            try {
                logger.info("Saving " + o + " to database");
                downloader(o);
                logger.info("Save successful");
            } catch (Exception e) {
                logger.error("Error while downloading historical data for " + o);
            }
        });
    }

    public List<DataFromFile> getHistoricalData(String ticker, LocalDate fromDate, LocalDate toDate) throws MalformedURLException {
        logger.info("Downloading and parsing historical data for " + ticker.toUpperCase());
        try {
            downloader(ticker);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        List<DataFromFile> output = downloadCurrentData.parseHistory(new URL("file://" + webAppProperties.getSetting("HISTORICAL_DATA_LOCATION") + "/" + ticker.toLowerCase() + "_d.csv"))
                .stream()
                .filter(o -> o.getDate().isAfter(fromDate))
                .filter(o -> o.getDate().isBefore(toDate))
                .collect(Collectors.toList());
        if (output.size() == 0) {
            logger.error("Parsed file is empty");
        }
        return output;
    }

    public void downloader(String ticker) throws IOException {
        String filename = webAppProperties.getSetting("HISTORICAL_DATA_LOCATION") + "/" + ticker.toLowerCase() + "_d.csv";
        LocalDate fileDate = LocalDate.MIN;
        if (new File(filename).exists()) {
            fileDate = LocalDate.ofInstant(Files.getLastModifiedTime(Path.of(filename)).toInstant(), ZoneId.systemDefault());
        }
        if (!fileDate.equals(LocalDate.now())) {

            String url = "https://stooq.com/q/d/l/?s=" + ticker.toLowerCase() + "&i=d";
            downloadFile(url, filename);
        } else {
            logger.info("Historical data for " + ticker.toUpperCase() + " is actual as of " + fileDate + ", skipping download");
        }
    }

    private void downloadFile(String downloadUrl, String saveAsFileName) {
        try (InputStream in = new URL(downloadUrl).openStream()) {
            Files.copy(in, Paths.get(saveAsFileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Download error" + e);
        }

    }
}
