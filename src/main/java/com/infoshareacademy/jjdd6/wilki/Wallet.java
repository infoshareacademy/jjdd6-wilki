package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.util.LinkedList;

public class Wallet {
    private LinkedList<Share> myWallet = new LinkedList<>();
//    private BigDecimal freeCash;
    private BigDecimal baseCash;
//    private BigDecimal baseWorth;
//    private BigDecimal currentWorth;
//    private BigDecimal stopLossWorth;
//    private BigDecimal takeProfitWorth;

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
                .reduce(BigDecimal.ZERO,(a,e) -> a.add(e)).add(getFreeCash());
    }

    public BigDecimal getSharesCurrentWorth() {
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
        return getBaseWorth().add(this.myWallet.stream()
                .map(Share::getTotalProfit)
                .reduce(BigDecimal.ZERO,(a,e) -> a.add(e).subtract(getBaseCash())));
    }

    public BigDecimal getBaseCash() {
        return baseCash;
    }

    public void setBaseCash(BigDecimal baseCash) {
        this.baseCash = baseCash;
    }

    public Double getROE() {
        return (getCurrentWorth().add(getFreeCash())).divide(getBaseCash()).doubleValue() - 1;
    }

    public void increaseBaseCash (BigDecimal amount) {
        setBaseCash(getBaseCash().add(amount));
    }

    public void decreaseBaseCash (BigDecimal amount) {
        setBaseCash(getBaseCash().subtract(amount));

    }


}

