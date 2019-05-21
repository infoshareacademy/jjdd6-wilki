package com.infoshareacademy.jjdd6.service;

import com.infoshareacademy.jjdd6.dao.StatsDao;
import com.infoshareacademy.jjdd6.wilki.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;

@RequestScoped
@Transactional
public class
StatsService {

    private static Logger logger = LoggerFactory.getLogger(StatsService.class);

    @Inject
    private StatsDao statsDao;

    @Inject
    private DownloadCurrentData downloadCurrentData;

    public List<String[]> getMostBoughtStocks() {
        return statsDao.getMostBoughtStocks().stream().limit(10L).collect(Collectors.toList());
    }

    public List<String[]> getMostSoldStocks() {
        return statsDao.getMostSoldStocks().stream().limit(10L).collect(Collectors.toList());
    }

    public List<String[]> getMostProfitableStocks(User user){
        List<String[]> output = new ArrayList<>();
        user.getWallet().getShares().forEach(o -> {
            if (o.getTotalProfit().compareTo(BigDecimal.ZERO) > 0) {

                String[] mapping = new String[2];
                mapping[0] = o.getTicker();
                mapping[1] = o.getTotalProfit().setScale(2, RoundingMode.HALF_UP).toString();
                output.add(mapping);
            }
        });
        return output.stream().limit(10L).collect(Collectors.toList());
    }

    public List<String[]> getSharesWithLosses(User user){
        List<String[]> output = new ArrayList<>();
        user.getWallet().getShares().forEach(o -> {
            if (o.getTotalProfit().compareTo(BigDecimal.ZERO) < 0) {

                String[] mapping = new String[2];
                mapping[0] = o.getTicker();
                mapping[1] = o.getTotalProfit().setScale(2, RoundingMode.HALF_UP).toString();
                output.add(mapping);
            }
        });
        return output.stream().limit(10L).collect(Collectors.toList());
    }

    public List<String[]> getMostTradedOnWse() {

        List<String[]> output = new ArrayList<>();
        try {
            List<DataFromFile> statsWSE = downloadCurrentData.getMostTradedVolume();
            for (int i = 25; i < statsWSE.size(); i++) {
                String[] mapping = new String[2];
                mapping[1] = statsWSE.get(i).getVolume().toString();
                mapping[0] = statsWSE.get(i).getSymbol();
                output.add(mapping);
            }
        } catch (MalformedURLException e) {
            logger.info("Error while downloading WSE stats");
        }
        return output;
    }

    public Map<String, String> getMostProfitableShare(Wallet wallet) {
        Optional<Share> result = wallet.walletToDisplay().stream().max(Comparator.comparing(Share::getCurrentReturn));
        Map<String, String> output = new HashMap<>();
        mapResult(result, output);
        return output;
    }

    private void mapResult(Optional<Share> result, Map<String, String> output) {
        if (result.isPresent()) {
            output.put("ticker", result.get().getTicker());
            output.put("profit", result.get().getTotalProfit().setScale(2, RoundingMode.HALF_UP).toString());
            output.put("return", result.get().getCurrentReturn().setScale(2, RoundingMode.HALF_UP).toString() + "%");
        } else {
            output.put("ticker", "No shares");
            output.put("profit", "");
            output.put("return", "");
        }
    }

    public Map<String, String> getLeastProfitableShare(Wallet wallet) {
        Optional<Share> result = wallet.walletToDisplay().stream().min(Comparator.comparing(Share::getCurrentReturn));
        Map<String, String> output = new HashMap<>();
        mapResult(result, output);
        return output;
    }

}