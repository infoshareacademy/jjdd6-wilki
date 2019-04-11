package com.infoshareacademy.jjdd6.wilki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Share implements Serializable {

    private String ticker;
    private String fullCompanyName;
    private BigDecimal currentPrice;
    private BigDecimal takeProfitPrice = BigDecimal.valueOf(0);
    private BigDecimal stopLossPrice = BigDecimal.valueOf(0);
    private Double currentPE;
    private Long volume;
    private LinkedList<Transaction> transactionLinkedList = new LinkedList<>();
    private List<Transaction> transactionHistory = new ArrayList<>();
    private BigDecimal highestPrice;
    private BigDecimal lowestPrice;
    private LocalDate dataDate;
    private LocalTime dataTime;
    private int delay;

    public Share() {
    }

    public Share(String ticker) {

        this.ticker = ticker;
    }

    public Double getRiskRewardRatio() {
        if ((getTakeProfitPrice().subtract(getStopLossPrice())).doubleValue() <= 0) {
            return 0.0;
        } else {
            return (getAvgBuyPrice().doubleValue() - getStopLossPrice().doubleValue())
                    / (getTakeProfitPrice().doubleValue() - getStopLossPrice().doubleValue());
        }
    }

    public String getTicker() {

        return ticker.toUpperCase();
    }

    public Double getTargetPE() {

        return getTakeProfitPrice().doubleValue() / (getCurrentPE() / getCurrentPrice().doubleValue());
    }

    public Double getCurrentPE() {

        return currentPE;
    }

    public void setTakeProfitPrice(BigDecimal takeProfitPrice) {

        this.takeProfitPrice = takeProfitPrice;
    }

    public void setStopLossPrice(BigDecimal stopLossPrice) {

        this.stopLossPrice = stopLossPrice;
    }

    public void pullExternalData() {
        List<DataFromFile> data = new LoadData()
                .loadToList(getTicker().toLowerCase() + ".csv");
        this.volume = data.get(0).getVolume();
        this.currentPrice = data.get(0).getClosingPrice();
        this.highestPrice = data.get(0).getHighestPrice();
        this.lowestPrice = data.get(0).getLowestPrice();
        this.dataTime = data.get(0).getTime();
        this.dataDate = data.get(0).getDate();
        this.currentPE = new LoadData()
                .loadToList(getTicker().toLowerCase() + "_pe.csv")
                .get(0)
                .getClosingPrice()
                .doubleValue();
        this.fullCompanyName = new LoadData().loadAndScanTickers(getTicker());
        this.delay = calculateDelay(data.get(0).getTime());
    }

    public String getFullCompanyName() {
        return fullCompanyName;
    }

    public Long getVolume() {
        return volume;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
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

    public BigDecimal getAvgBuyPrice() {
        try {
            return BigDecimal.valueOf(this.transactionLinkedList.stream()
                    .mapToDouble((o) -> o.getPrice().doubleValue() * o.getAmount().doubleValue())
                    .sum())
                    .divide(BigDecimal.valueOf(getSharesTotalAmount()), RoundingMode.HALF_UP);
        } catch (ArithmeticException e) {
            return BigDecimal.ZERO;
        }
    }

    public void buy(Integer amount, double price) {
        this.transactionLinkedList.add(new Transaction(amount, BigDecimal.valueOf(price)));
        this.transactionHistory.add(new Transaction(amount, BigDecimal.valueOf(price)));
    }

    public BigDecimal sell(int amount, double price) {

        BigDecimal profit = BigDecimal.valueOf(0);
        int tempAmount = amount;

        while (amount > this.transactionLinkedList.get(0).getAmount()) {
            amount -= this.transactionLinkedList.get(0).getAmount();

            profit = profit
                    .add(BigDecimal.valueOf(this.transactionLinkedList.get(0).getAmount())
                            .multiply((BigDecimal.valueOf(price)
                                    .subtract(this.transactionLinkedList.get(0).getPrice()))));

            this.transactionLinkedList.remove(0);
        }

        profit = profit
                .add(BigDecimal.valueOf(amount)
                        .multiply((BigDecimal.valueOf(price)
                                .subtract(this.transactionLinkedList.get(0).getPrice()))));

        this.transactionLinkedList.get(0)
                .setAmount(this.transactionLinkedList.get(0).getAmount() - amount);

        if (this.transactionLinkedList.get(0).getAmount() == 0) {
            this.transactionLinkedList.remove(0);
        }

        this.transactionHistory.add(new Transaction(-tempAmount, BigDecimal.valueOf(price), profit));
        return profit;
    }

    public BigDecimal getTotalProfit() {

        try {
            return transactionHistory.stream()
                    .map(Transaction::getProfit)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (NullPointerException e) {
            return BigDecimal.ZERO;
        }
    }

    public Integer getSharesTotalAmount() {

        return this.transactionLinkedList.stream()
                .mapToInt(Transaction::getAmount)
                .sum();
    }

    public BigDecimal getFeeAmount() {

        return transactionHistory.stream()
                .map(Transaction::getTransactionFeeValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int calculateDelay(LocalTime time) {
        return (int) Duration.between(time, LocalTime.now()).toMinutes();
    }

    public LinkedList<Transaction> getTransactionLinkedList() {
        return transactionLinkedList;
    }

    public void setTransactionLinkedList(LinkedList<Transaction> transactionLinkedList) {
        this.transactionLinkedList = transactionLinkedList;
    }

    public void setTransactionHistory(List<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Share{");
        sb.append("ticker='").append(ticker).append('\'');
        sb.append(", fullCompanyName='").append(fullCompanyName).append('\'');
        sb.append(", currentPrice=").append(currentPrice);
        sb.append(", takeProfitPrice=").append(takeProfitPrice);
        sb.append(", stopLossPrice=").append(stopLossPrice);
        sb.append(", currentPE=").append(currentPE);
        sb.append(", volume=").append(volume);
        sb.append(", transactionLinkedList=").append(transactionLinkedList);
        sb.append(", transactionHistory=").append(transactionHistory);
        sb.append(", highestPrice=").append(highestPrice);
        sb.append(", lowestPrice=").append(lowestPrice);
        sb.append(", dataDate=").append(dataDate);
        sb.append(", dataTime=").append(dataTime);
        sb.append(", delay=").append(delay);
        sb.append('}');
        return sb.toString();
    }
}
