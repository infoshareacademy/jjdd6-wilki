package com.infoshareacademy.jjdd6.wilki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Wallet implements Serializable {

    private LinkedList<Share> shares = new LinkedList<>();
    private BigDecimal baseCash = BigDecimal.ZERO;
    private BigDecimal cashFromProfits = BigDecimal.ZERO;
    private List<Transaction> walletHistory = new ArrayList<>();

    public Wallet() {

    }

    public LinkedList<Share> getShares() {

        return shares;
    }


    public BigDecimal getBaseWorth() {

        return this.shares.stream()
                .map(Share::getBaseValue)
                .reduce(BigDecimal.ZERO, (a, e) -> a.add(e))
                .setScale(2, RoundingMode.HALF_UP);

    }

    public BigDecimal getCurrentWorth() {

        return this.getSharesCurrentWorth().add(getFreeCash()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSharesCurrentWorth() {

        return this.shares.stream()
                .map(Share::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getStopLossWorth() {

        return this.shares.stream()
                .map(Share::getStopLossValue)
                .reduce(BigDecimal.ZERO, (a, e) -> a.add(e))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTakeProfitWorth() {

        return this.shares.stream()
                .map(Share::getTakeProfitValue)
                .reduce(BigDecimal.ZERO, (a, e) -> a.add(e))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getCashFromProfits() {
        return cashFromProfits.setScale(2, RoundingMode.HALF_UP);
    }

    public void addToCashFromProfits(BigDecimal profit) {
        this.cashFromProfits = this.cashFromProfits.add(profit).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getFreeCash() {

        return getCashFromProfits().add(this.shares.stream()
                .map(Share::getTotalProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(getBaseCash())
                .subtract(getBaseWorth())).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getBaseCash() {

        return baseCash.setScale(2, RoundingMode.HALF_UP);
    }

    public void setBaseCash(BigDecimal baseCash) {

        this.baseCash = baseCash.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getROE() {

        return (getCurrentWorth().divide(getBaseCash())).subtract(BigDecimal.ONE).multiply(BigDecimal.valueOf(100.00)).setScale(2, RoundingMode.HALF_UP);
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
                .reduce(BigDecimal.ZERO, (a, e) -> a.add(e))
                .setScale(2, RoundingMode.HALF_UP);
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
        Share result = sellShareCommon(ticker, amount, price);
        walletHistory.add(new Transaction(ticker, result.getFullCompanyName(), LocalDate.now(), -amount, BigDecimal.valueOf(price).setScale(4, RoundingMode.HALF_UP), BigDecimal.ZERO));

    }

    private Share sellShareCommon(String ticker, int amount, double price) {
        Share result = this.getShares().stream()
                .filter((o) -> o.getTicker().contains(ticker.toUpperCase()))
                .findFirst()
                .get();

        BigDecimal profit = result.sell(amount, price);
        this.addToCashFromProfits(profit);

        for (int i = 0; i < getShares().size(); i++) {
            if (getShares().get(i).getSharesTotalAmount() == 0) {
                getShares().remove(i);
            }
        }
        return result;
    }

    public void sellShare(String ticker, int amount, double price, LocalDate date) {
        Share result = sellShareCommon(ticker, amount, price);
        walletHistory.add(new Transaction(ticker, result.getFullCompanyName(), date, -amount, BigDecimal.valueOf(price).setScale(4, RoundingMode.HALF_UP), BigDecimal.ZERO));

    }

    public void buyShare(String ticker, int amount, double price) {
        Share result = buyShareCommon(ticker, amount, price);
        walletHistory.add(new Transaction(ticker, result.getFullCompanyName(), LocalDate.now(), amount, BigDecimal.valueOf(price).setScale(4, RoundingMode.HALF_UP), BigDecimal.ZERO));
    }

    private Share buyShareCommon(String ticker, int amount, double price) {
        Share result = scanWalletForShare(ticker.toUpperCase());
        result.buy(amount, price);

        if (this.getShares().stream()
                .filter((o) -> o.getTicker().contains(ticker.toUpperCase()))
                .count() == 0) {
            this.getShares().add(result);
        }
        DownloadData.updateWalletData(this);
        return result;
    }

    public void buyShare(String ticker, int amount, double price, LocalDate date) {
        Share result = buyShareCommon(ticker, amount, price);
        walletHistory.add(new Transaction(ticker, result.getFullCompanyName(), date, amount, BigDecimal.valueOf(price).setScale(4, RoundingMode.HALF_UP), BigDecimal.ZERO));
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

    public List<Transaction> getWalletHistory() {
        return walletHistory;
    }

    public void setWalletHistory(List<Transaction> walletHistory) {
        this.walletHistory = walletHistory;
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