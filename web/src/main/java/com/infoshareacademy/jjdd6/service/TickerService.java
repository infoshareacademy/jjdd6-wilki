package com.infoshareacademy.jjdd6.service;

import com.infoshareacademy.jjdd6.dao.TickerDao;
import com.infoshareacademy.jjdd6.wilki.Ticker;
import com.infoshareacademy.jjdd6.wilki.DownloadCurrentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Transactional
@RequestScoped
public class TickerService {

    private static Logger logger = LoggerFactory.getLogger(TickerService.class);

    @Inject
    private TickerDao tickerDao;

    @Inject
    private DownloadCurrentData downloadCurrentData;

    private void downloadTickers() {
        try {
            URL tickersURL = new URL("https://raw.githubusercontent.com/stockwallet/tickers/master/tickers.csv");
            logger.info("Loading tickers from " + tickersURL);
            List<String[]> dataLoaded = downloadCurrentData.readFromURL(tickersURL);
            dataLoaded.stream()
                    .map((o) -> new Ticker(o[0], o[1]))
                    .forEach(tickerDao::save);
        } catch (MalformedURLException e) {
            logger.error("Error while reading tickers");
        }
    }

    public void checkDataInDatabase() {
        if (tickerDao.getCount() == 0L) downloadTickers();
    }

    public String scanTickers(String ticker) {
        checkDataInDatabase();
        return tickerDao.findById(ticker).getFullName();
    }


    public Boolean validateTicker(String ticker) {
        checkDataInDatabase();
        return !tickerDao.findById(ticker).getFullName().isEmpty();
    }
}
