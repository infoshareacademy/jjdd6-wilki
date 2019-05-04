package com.infoshareacademy.jjdd6.dao;

import com.infoshareacademy.jjdd6.wilki.Share;
import com.infoshareacademy.jjdd6.wilki.Wallet;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class BuySharesDao {

    @Inject
    ShareDao shareDao;

    public void buyShare(Wallet wallet, String ticker, int amount, double price) {
        Share share = buyShareCommon(ticker, amount, price);
        share.getWallets().add(wallet);
        shareDao.save(share);
    }

    private Share buyShareCommon(String ticker, Integer amount, double price) {
        Share share = scanWalletForShare(ticker.toUpperCase());
        share.buy(amount, price);

        return share;
    }

    private Share scanWalletForShare(String ticker) {
        List<Share> shareList = new ArrayList<>(shareDao.findByTicker(ticker.toUpperCase()));
        if (shareList.isEmpty()) {
            return new Share(ticker);
        }

        return (Share) shareList.stream().
                filter(o -> o.getTicker().contains(ticker));
    }
}
