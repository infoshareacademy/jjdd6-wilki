package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Share {
    private String ticker;
    private String fullCompanyName; // imported?
    private BigDecimal currentPrice; // imported
    private BigDecimal takeProfitPrice;
    private BigDecimal stopLossPrice;
//    private Integer sharesTotalAmount;
//    private BigDecimal avgBuyPrice;
//    private BigDecimal currentValue;
    private Double currentPE; // imported
//    private Double targetPE;
//    private Double riskRewardRatio;
    private Long volume; // imported
    private LinkedList<Transaction> transactionLinkedList = new LinkedList<>();
    private List<Transaction> transactionHistory = new ArrayList<>();

    public Share(String ticker) {
        this.ticker = ticker;
    }

    // targetPE = takeProfitPrice / (currentPE / currentPrice)
    // currentPrice = cena zamkniecia z pliku CSV
    // riskRewardRatio = (avgBuyPrice - stopLossPrice) / (takeProfitPrice - stopLossPrice)
    // Lista z aktualnym stanem do wyliczenia avgBuyPrice i sharesAmount. Przy kupnie do listy dodany bedzie nowy wiersz, a sprzedaz bedzie zgodnie z metoda FIFO
    // Lista z historia transakcji do podgladu dla uzytkownika


    public Double getRiskRewardRatio() {
        return (getAvgBuyPrice().subtract(getStopLossPrice())).divide((getTakeProfitPrice().subtract(getStopLossPrice()))).doubleValue();
    }

    public String getTicker() {
        return ticker;
    }

    public Double getTargetPE() {
        return getTakeProfitPrice().doubleValue() / (getCurrentPE() / getCurrentPrice().doubleValue());
    }

    public Double getCurrentPE() {
        return currentPE;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setTakeProfitPrice(BigDecimal takeProfitPrice) {
        this.takeProfitPrice = takeProfitPrice;
    }

    public void setStopLossPrice(BigDecimal stopLossPrice) {
        this.stopLossPrice = stopLossPrice;
    }

    public void setCurrentPE(Double currentPE) {
        this.currentPE = currentPE;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public BigDecimal getTakeProfitPrice() {
        return takeProfitPrice;
    }

    public BigDecimal getTakeProfitValue() {
        return getTakeProfitPrice().multiply(BigDecimal.valueOf(getSharesTotalAmount()));
    }

    public BigDecimal getStopLossPrice() {
        return stopLossPrice;
    }

    public BigDecimal getStopLossValue() {
        return getStopLossPrice().multiply(BigDecimal.valueOf(getSharesTotalAmount()));
    }

    public BigDecimal getBaseValue() {
        return getAvgBuyPrice().multiply(BigDecimal.valueOf(getSharesTotalAmount()));
    }

    public BigDecimal getCurrentValue() {
        return getCurrentPrice().multiply(BigDecimal.valueOf(getSharesTotalAmount()));
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public BigDecimal getAvgBuyPrice() {
        return BigDecimal.valueOf(this.transactionLinkedList.stream()
                .mapToDouble((o) -> o.getPrice().doubleValue() * o.getAmount().doubleValue())
                .sum())
                .divide(BigDecimal.valueOf(getSharesTotalAmount()));
    }

    public void buyShares(Share share, int amount, double price) {
        share.transactionLinkedList.add(new Transaction(amount, BigDecimal.valueOf(price)));
        share.transactionHistory.add(new Transaction(amount, BigDecimal.valueOf(price)));
    }

    public BigDecimal sellShares(Share share, int amount, double price) {

        BigDecimal profit = BigDecimal.valueOf(0);
        int tempAmount = amount;


        while (amount > share.transactionLinkedList.get(0).getAmount()) {
            amount -= share.transactionLinkedList.get(0).getAmount();
            profit = profit.add(BigDecimal.valueOf(share.transactionLinkedList.get(0).getAmount()).multiply((BigDecimal.valueOf(price).subtract(share.transactionLinkedList.get(0).getPrice()))));
            share.transactionLinkedList.remove(0);
        }

        if (amount <= share.transactionLinkedList.get(0).getAmount()) {
            profit = profit.add(BigDecimal.valueOf(amount).multiply((BigDecimal.valueOf(price).subtract(share.transactionLinkedList.get(0).getPrice()))));
            share.transactionLinkedList.get(0).setAmount(share.transactionLinkedList.get(0).getAmount() - amount);

            if (share.transactionLinkedList.get(0).getAmount() == 0) {
                share.transactionLinkedList.remove(0);}
        }

        share.transactionHistory.add(new Transaction(-tempAmount,BigDecimal.valueOf(price),profit));
        return profit;

    }

    public BigDecimal getTotalProfit() {
        return transactionHistory.stream()
                .map(Transaction::getProfit)
                .reduce(BigDecimal.ZERO,(a,e) -> a.add(e));
    }

    public Integer getSharesTotalAmount() {
        return this.transactionLinkedList.stream()
                .mapToInt(Transaction::getAmount)
                .sum();

    }









}
