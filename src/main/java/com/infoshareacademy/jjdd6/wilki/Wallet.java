package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

public class Wallet extends SaveData {

    private LinkedList<Share> shares = new LinkedList<>();
    private BigDecimal baseCash = BigDecimal.ZERO;
    private BigDecimal cashFromProfits = BigDecimal.ZERO;

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

        return this.getSharesCurrentWorth().add(getFreeCash());
    }

    public BigDecimal getSharesCurrentWorth() {

        return this.shares.stream()
                .map(Share::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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

    public BigDecimal getCashFromProfits() {
        return cashFromProfits;
    }

    public void addToCashFromProfits(BigDecimal profit) {
        this.cashFromProfits = this.cashFromProfits.add(profit);
    }

    public BigDecimal getFreeCash() {

        return getCashFromProfits().add(this.shares.stream()
                .map(Share::getTotalProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add).add(getBaseCash()).subtract(getBaseWorth()));
    }

    public BigDecimal getBaseCash() {

        return baseCash;
    }

    public void setBaseCash(BigDecimal baseCash) {

        this.baseCash = baseCash;
    }

    public Double getROE() {

        return (getCurrentWorth()
                .divide(getBaseCash(), RoundingMode.HALF_UP).doubleValue()) - 1.0000;
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
        return result;
    }

    public void sellShare (String ticker, int amount, double price) {
        this.addToCashFromProfits(this.getShares().stream()
                .filter((o) -> o.getTicker().contains(ticker.toUpperCase()))
                .findFirst()
                .get()
                .sell(amount, price));

        for (int i = 0; i < getShares().size(); i++) {
                if (getShares().get(i).getSharesTotalAmount() == 0) {
                    getShares().remove(i);
                }
            }
        }

        public void buyShare (String ticker, int amount, double price) {
            Share result = scanWalletForShare(ticker.toUpperCase());
            if (result == null) {
                result = new Share(ticker.toUpperCase());
                this.getShares().add(result);
            }
            result.buy(amount, price);
        }
}

