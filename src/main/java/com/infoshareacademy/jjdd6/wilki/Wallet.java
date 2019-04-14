package com.infoshareacademy.jjdd6.wilki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Wallet implements Serializable {

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
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(getBaseCash())
                .subtract(getBaseWorth()));
    }

    public BigDecimal getBaseCash() {

        return baseCash;
    }

    public void setBaseCash(BigDecimal baseCash) {

        this.baseCash = baseCash;
    }

    public Double getROE() {

        return getCurrentWorth().doubleValue() / getBaseCash().doubleValue() - 1.0000;
    }

    public void increaseBaseCash(BigDecimal amount) {

        setBaseCash(getBaseCash().add(amount));
    }

    public void reduceBaseCash(BigDecimal amount) {

        setBaseCash(getBaseCash().subtract(amount));

    }

    public BigDecimal getTotalBuyFees() {

        return this.shares.stream()
                .map(Share::getFeeAmount)
                .reduce(BigDecimal.ZERO, (a, e) -> a.add(e));
    }

    public Share scanWalletForShare(String ticker) {
        return this.getShares().stream()
                .filter((o) -> o.getTicker().contains(ticker.toUpperCase()))
                .findFirst()
                .orElse(new Share(ticker.toUpperCase()));
    }

    public boolean checkIfShareIsPresent(String ticker) {
        return this.getShares().stream()
                .filter((o) -> o.getTicker().contains(ticker.toUpperCase()))
                .count() == 1;
    }

    public void sellShare(String ticker, int amount, double price) {
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
        System.out.println("SELL: " + ticker.toUpperCase() + " amount: " + amount + " price: " + price);
    }

    public void buyShare(String ticker, int amount, double price) {
        if (checkIfEnoughCash(amount, price)) {
            Share result = scanWalletForShare(ticker.toUpperCase());
            result.buy(amount, price);
            System.out.println("BUY: " + ticker.toUpperCase() + " amount: " + amount + " price: " + price);

            if (this.getShares().stream()
                    .filter((o) -> o.getTicker().contains(ticker.toUpperCase()))
                    .count() == 0) {
                this.getShares().add(result);
            }
            DownloadData.updateWalletData(this);


        } else {
            System.out.println("Not enough cash!");
        }
    }

    public void setShares(LinkedList<Share> shares) {
        this.shares = shares;
    }

    public void setCashFromProfits(BigDecimal cashFromProfits) {
        this.cashFromProfits = cashFromProfits;
    }

    public boolean checkIfEnoughCash(int amount, double price) {
        return amount * price <= getFreeCash().doubleValue();
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "shares=" + shares +
                ", baseCash=" + baseCash +
                ", cashFromProfits=" + cashFromProfits +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(shares, wallet.shares) &&
                Objects.equals(baseCash, wallet.baseCash) &&
                Objects.equals(cashFromProfits, wallet.cashFromProfits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shares, baseCash, cashFromProfits);
    }
}