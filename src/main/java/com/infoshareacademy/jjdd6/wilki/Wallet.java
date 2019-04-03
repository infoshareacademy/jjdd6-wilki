package com.infoshareacademy.jjdd6.wilki;

import java.util.LinkedHashMap;
import java.util.Map;

public class Wallet {
    private Map<String, Share> wallet = new LinkedHashMap<>();
    private String ticker;

    public void addToWallet (Share share) {
        wallet.put(share.getTicker(), share);
    }

    public void removeFromWallet (Share share) {
        wallet.remove(share.getTicker(), share);
    }

    public Wallet(Map<String, Share> wallet, String ticker) {
        this.wallet = wallet;
        this.ticker = ticker;
    }
}
