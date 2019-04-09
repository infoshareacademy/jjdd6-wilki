package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.util.LinkedList;

public class Wallet extends SaveData {

    private LinkedList<Share> shares = new LinkedList<>();
    private BigDecimal baseCash;

    public Wallet() {

    }

    public LinkedList<Share> getShares() {

        return shares;
    }


    public BigDecimal getBaseWorth() {

        return this.shares.stream()
                .map(Share::getBaseValue)
                .reduce(BigDecimal.ZERO, (a, e) -> a.add(e));

    }

    public BigDecimal getCurrentWorth() {

        return this.shares.stream()
                .map(Share::getCurrentValue)
                .reduce(BigDecimal.ZERO, (a, e) -> a.add(e)).add(getFreeCash());
    }

    public BigDecimal getSharesCurrentWorth() {

        return this.shares.stream()
                .map(Share::getCurrentValue)
                .reduce(BigDecimal.ZERO, (a, e) -> a.add(e));
    }

    public BigDecimal getStopLossWorth() {

        return this.shares.stream()
                .map(Share::getStopLossValue)
                .reduce(BigDecimal.ZERO, (a, e) -> a.add(e));
    }

    public BigDecimal getTakeProfitWorth() {

        return this.shares.stream()
                .map(Share::getTakeProfitValue)
                .reduce(BigDecimal.ZERO, (a, e) -> a.add(e));
    }

    public BigDecimal getFreeCash() {

        return getBaseWorth().add(this.shares.stream()
                .map(Share::getTotalProfit)
                .reduce(BigDecimal.ZERO, (a, e) -> a.add(e).subtract(getBaseCash())));
    }

    public BigDecimal getBaseCash() {

        return baseCash;
    }

    public void setBaseCash(BigDecimal baseCash) {

        this.baseCash = baseCash;
    }

    public Double getROE() {

        return (getCurrentWorth()
                .add(getFreeCash()))
                .divide(getBaseCash()).doubleValue() - 1;
    }

    public void increaseBaseCash(BigDecimal amount) {

        setBaseCash(getBaseCash().add(amount));
    }

    public void decreaseBaseCash(BigDecimal amount) {

        setBaseCash(getBaseCash().subtract(amount));

    }

    public BigDecimal getTotalBuyFees() {

        return this.shares.stream()
                .map(Share::getFeeAmount)
                .reduce(BigDecimal.ZERO, (a, e) -> a.add(e));
    }

    public Share scanWalletForShare(String ticker) {
        Share result = this.getShares().stream()
                .filter((o) -> o.getTicker().contains(ticker.toUpperCase()))
                .findFirst()
                .orElse(null);
        if (result == null) {
            result = new Share(ticker.toUpperCase());
            this.getShares().add(result);
        }
        return result;
    }

    public void sellShare (String ticker, int amount, double price) {
        Share result = this.getShares().stream()
                .filter((o) -> o.getTicker().contains(ticker.toUpperCase()))
                .findFirst()
                .orElse(null);
        result.sellShares(amount, price);
        if (result.getSharesTotalAmount() == 0) {
            this.getShares().stream()
                    .filter((o) -> o.getTicker().contains(ticker.toUpperCase()))
                    .findFirst().
            getShares().

        }

    }

}

