package com.infoshareacademy.jjdd6.service;

import com.infoshareacademy.jjdd6.dao.StatsDao;
import com.infoshareacademy.jjdd6.wilki.DataFromFile;
import com.infoshareacademy.jjdd6.wilki.DownloadCurrentData;
import com.infoshareacademy.jjdd6.wilki.Share;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.*;

public class StatsService {

    private static Logger logger = LoggerFactory.getLogger(StatsService.class);


    @Inject
    StatsDao statsDao;

    @Inject
    DownloadCurrentData downloadCurrentData;

    public List<String> getMostBoughtStocks() {
        return statsDao.getMostBoughtStocks();

    }

    public List<String> getMostSoldStocks() {
        return statsDao.getMostSoldStocks();
    }

    public List<String> getMostTradedOnWse() {

        List<DataFromFile> statsWSE = null;
        List<String> output = null;
        try {
            statsWSE = downloadCurrentData.getMostTradedVolume();
            for (int i = 25; i < statsWSE.size(); i++) {
                output.add(statsWSE.get(i).getSymbol() + " ("
                        + statsWSE.get(i).getVolume() + ") ["
                        + statsWSE.get(i).getChange() + "]");
            }
        } catch (MalformedURLException e) {
            logger.info("Error while downloading WSE stats");
        }
        return output;
    }

    public Map<String, String> getMostProfitableShare(Wallet wallet) {
        Optional<Share> result = wallet.getShares().stream().max(Comparator.comparing(Share::getTotalProfit));
        Map<String, String> output = new HashMap<>();
        if (result.isPresent()) {
            output.put("ticker", result.get().getTicker());
            output.put("profit", result.get().getTotalProfit().setScale(2, RoundingMode.HALF_UP).toString());
            output.put("return", result.get().getCurrentReturn().setScale(2, RoundingMode.HALF_UP).toString());
        }
        return output;
    }

    public Map<String, String> getLeastProfitableShare(Wallet wallet) {
        Optional<Share> result = wallet.getShares().stream().min(Comparator.comparing(Share::getTotalProfit));
        Map<String, String> output = new HashMap<>();
        if (result.isPresent()) {
            output.put("ticker", result.get().getTicker());
            output.put("profit", result.get().getTotalProfit().setScale(2, RoundingMode.HALF_UP).toString());
            output.put("return", result.get().getCurrentReturn().setScale(2, RoundingMode.HALF_UP).toString());
        }
        return output;
    }

}