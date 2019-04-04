package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.util.LinkedList;

public class Wallet {
    private LinkedList<Share> myWallet = new LinkedList<>();
    private BigDecimal freeCash;
    private BigDecimal baseCash;
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

    public BigDecimal getFreeCash() {
        return freeCash;
    }

    public void setFreeCash(BigDecimal freeCash) {
        this.freeCash = freeCash;
    }

    public BigDecimal getBaseCash() {
        return baseCash;
    }

    public void setBaseCash(BigDecimal baseCash) {
        this.baseCash = baseCash;
    }

    public Double getROE() {
        return getCurrentWorth().divide(getBaseWorth()).doubleValue() - 1;
    }
}

