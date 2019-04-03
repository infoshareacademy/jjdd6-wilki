package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.util.LinkedList;

public class Share {
    private String ticker;
    private String fullCompanyName;
    private BigDecimal currentPrice;
    private BigDecimal takeProfitPrice;
    private BigDecimal stopLossPrice;
    private Integer sharesTotalAmount;
    private BigDecimal avgBuyPrice;
    private Double currentPE;
    private Double targetPE;
    private Double riskRewardRatio;
    private Long volume;
    private LinkedList<Transaction> transactionLinkedList = new LinkedList<Transaction>();

    public Share(String ticker) {
        this.ticker = ticker;
    }

    // targetPE = takeProfitPrice / (currentPE / currentPrice)
    // currentPrice = cena zamkniecia z pliku CSV
    // riskRewardRatio = (avgBuyPrice - stopLossPrice) / (takeProfitPrice - stopLossPrice)
    // Lista z aktualnym stanem do wyliczenia avgBuyPrice i sharesAmount. Przy kupnie do listy dodany bedzie nowy wiersz, a sprzedaz bedzie zgodnie z metoda FIFO
    // Lista z historia transakcji do podgladu dla uzytkownika

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Double getAvgBuyPrice() {
        return this.transactionLinkedList.stream()
                .mapToDouble((o) -> o.getBuyPrice().doubleValue())
                .average().getAsDouble();
    }

    public void addShares(Share share, int amount, double price) {
        share.transactionLinkedList.add(new Transaction(amount, BigDecimal.valueOf(price)));

    }

    public BigDecimal removeShares(Share share, int amount, double price) {

        BigDecimal profit = BigDecimal.valueOf(0);

        while (amount > share.transactionLinkedList.get(0).getAmount()) {
            amount -= share.transactionLinkedList.get(0).getAmount();
            profit = profit.add(BigDecimal.valueOf(share.transactionLinkedList.get(0).getAmount()).multiply((BigDecimal.valueOf(price).subtract(share.transactionLinkedList.get(0).getBuyPrice()))));
            share.transactionLinkedList.remove(0);
        }

        if (amount <= share.transactionLinkedList.get(0).getAmount()) {
            profit = profit.add(BigDecimal.valueOf(amount).multiply((BigDecimal.valueOf(price).subtract(share.transactionLinkedList.get(0).getBuyPrice()))));
            share.transactionLinkedList.get(0).setAmount(share.transactionLinkedList.get(0).getAmount() - amount);

            if (share.transactionLinkedList.get(0).getAmount() == 0) {
                share.transactionLinkedList.remove(0);}
        }
        return profit;

        }

    public Integer getSharesTotalAmount() {
        return this.transactionLinkedList.stream()
                .mapToInt(Transaction::getAmount)
                .sum();

    }









}
