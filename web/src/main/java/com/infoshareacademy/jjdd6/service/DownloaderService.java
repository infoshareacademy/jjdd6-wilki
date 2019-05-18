package com.infoshareacademy.jjdd6.service;

import com.infoshareacademy.jjdd6.properties.WebAppProperties;
import com.infoshareacademy.jjdd6.wilki.DataFromFile;
import com.infoshareacademy.jjdd6.wilki.DownloadCurrentData;
import com.infoshareacademy.jjdd6.wilki.Ticker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class DownloaderService {

    private static Logger logger = LoggerFactory.getLogger(DownloaderService.class);


    @Inject
    private WebAppProperties webAppProperties;

    @Inject
    TickerService tickerService;

    @Inject
    DownloadCurrentData downloadCurrentData;

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

    public List<DataFromFile> getHistoricalData(String ticker, LocalDate fromdate, LocalDate toDate) throws MalformedURLException {
        logger.info("Downloading and parsing historical data for " + ticker);
        try {
            downloader(ticker);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return downloadCurrentData.parseHistory(new URL("file://" + webAppProperties.getSaveDir("HISTORICAL_DATA_LOCATION") + "/" + ticker.toLowerCase() + "_d.csv"))
                .stream()
                .filter(o -> o.getDate().isAfter(fromdate))
                .filter(o -> o.getDate().isBefore(toDate))
                .collect(Collectors.toList());
    }

    public void downloader(String ticker) throws IOException {
        String filename = webAppProperties.getSaveDir("HISTORICAL_DATA_LOCATION") + "/" + ticker.toLowerCase() + "_d.csv";
        String url = "https://stooq.com/q/d/l/?s=" + ticker.toLowerCase() + "&i=d";
        try {
            downloadFileWithResume(url, filename);
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private long transferDataAndGetBytesDownloaded(URLConnection downloadFileConnection, File outputFile) throws IOException {

        long bytesDownloaded = 0;
        try (InputStream is = downloadFileConnection.getInputStream(); OutputStream os = new FileOutputStream(outputFile, true)) {

            byte[] buffer = new byte[1024];

            int bytesCount;
            while ((bytesCount = is.read(buffer)) > 0) {
                os.write(buffer, 0, bytesCount);
                bytesDownloaded += bytesCount;
            }
        }
        return bytesDownloaded;
    }

    private long downloadFileWithResume(String downloadUrl, String saveAsFileName) throws IOException, URISyntaxException {
        File outputFile = new File(saveAsFileName);

        URLConnection downloadFileConnection = addFileResumeFunctionality(downloadUrl, outputFile);
        return transferDataAndGetBytesDownloaded(downloadFileConnection, outputFile);
    }

    private URLConnection addFileResumeFunctionality(String downloadUrl, File outputFile) throws IOException, URISyntaxException, ProtocolException, ProtocolException {
        long existingFileSize = 0L;
        URLConnection downloadFileConnection = new URI(downloadUrl).toURL()
                .openConnection();

        if (outputFile.exists() && downloadFileConnection instanceof HttpURLConnection) {
            HttpURLConnection httpFileConnection = (HttpURLConnection) downloadFileConnection;

            HttpURLConnection tmpFileConn = (HttpURLConnection) new URI(downloadUrl).toURL()
                    .openConnection();
            tmpFileConn.setRequestMethod("HEAD");
            long fileLength = tmpFileConn.getContentLengthLong();
            existingFileSize = outputFile.length();

            if (existingFileSize < fileLength) {
                httpFileConnection.setRequestProperty("Range", "bytes=" + existingFileSize + "-" + fileLength);
            } else {
                throw new IOException("File Download already completed.");
            }
        }
        return downloadFileConnection;
    }
}
