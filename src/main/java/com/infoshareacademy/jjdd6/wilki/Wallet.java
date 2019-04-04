package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.util.LinkedList;

public class Wallet {
    private LinkedList<Share> myWallet = new LinkedList<>();
    private BigDecimal cash;
    private BigDecimal baseWorth;
    private BigDecimal currentWorth;
    private BigDecimal stopLossWorth;
    private BigDecimal takeProfitWorth;



    public Wallet(LinkedList<Share> wallet) {
        this.myWallet = wallet;
    }

    public LinkedList<Share> getWallet() {
        return myWallet;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public BigDecimal getBaseWorth() {
        return this.myWallet.stream()
                .map(Share::getBaseValue)
                .reduce(BigDecimal.ZERO,(a,e) -> a.add(e));

    }

    public BigDecimal getCurrentWorth() {
        return this.myWallet.stream()
                .map(Share::getCurrentValue)
                .reduce(BigDecimal.ZERO,(a,e) -> a.add(e));
    }

    public BigDecimal getStopLossWorth() {
        return this.myWallet.stream()
                .map(Share::getStopLossValue)
                .reduce(BigDecimal.ZERO,(a,e) -> a.add(e));
    }

    public BigDecimal getTakeProfitWorth() {
        return this.myWallet.stream()
                .map(Share::getTakeProfitValue)
                .reduce(BigDecimal.ZERO,(a,e) -> a.add(e));
    }
}

