package com.infoshareacademy.jjdd6.wilki;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "SHARE")
public class Share implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(Share.class);

    @Id
    @Column(name = "ticker")
    private String ticker;

    @Column(name = "full_company_name")
    @NotNull
    private String fullCompanyName;

    @Column(name = "current_price")
    @NotNull
    private BigDecimal currentPrice;

    @Column(name = "take_profit_price")
    @NotNull
    private BigDecimal takeProfitPrice = BigDecimal.valueOf(0);

    @Column
    @NotNull
    private BigDecimal stopLossPrice = BigDecimal.valueOf(0);

    @Column(name = "current_PE")
    @NotNull
    private Double currentPE;

    @Column(name = "volume")
    @NotNull
    private Long volume;

    @Column(name = "transaction_id")
    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private LinkedList<Transaction> transactionLinkedList = new LinkedList<>();

    @Column(name = "transaction_history")
    private List<Transaction> transactionHistory = new ArrayList<>();

    @Column(name = "highest_price")
    @NotNull
    private BigDecimal highestPrice;

    @Column(name = "lowest_price")
    @NotNull
    private BigDecimal lowestPrice;

    @Column(name = "data_date")
    @NotNull
    private LocalDate dataDate;

    @Column(name = "data_time")
    @NotNull
    private LocalTime dataTime;
    private int delay;

    public Share() {
    }

    public Share(String ticker) {

        this.ticker = ticker;
    }

    public Double getRiskRewardRatio() {
        if ((getTakeProfitPrice().subtract(getStopLossPrice())).setScale(4, RoundingMode.HALF_UP).doubleValue() <= 0) {
            return 0.0;
        } else {
            return (getAvgBuyPrice().setScale(4, RoundingMode.HALF_UP).doubleValue() - getStopLossPrice().setScale(4, RoundingMode.HALF_UP).doubleValue())
                    / (getTakeProfitPrice().setScale(4, RoundingMode.HALF_UP).doubleValue() - getStopLossPrice().setScale(4, RoundingMode.HALF_UP).doubleValue());
        }
    }

    public String getTicker() {

        return ticker.toUpperCase();
    }

    public Double getTargetPE() {

        return getTakeProfitPrice().setScale(4, RoundingMode.HALF_UP).doubleValue() / (getCurrentPE() / getCurrentPrice().setScale(4, RoundingMode.HALF_UP).doubleValue());
    }

    public Double getCurrentPE() {

        return currentPE;
    }

    public void setTakeProfitPrice(BigDecimal takeProfitPrice) {

        this.takeProfitPrice = takeProfitPrice.setScale(4, RoundingMode.HALF_UP);
    }

    public void setStopLossPrice(BigDecimal stopLossPrice) {

        this.stopLossPrice = stopLossPrice.setScale(4, RoundingMode.HALF_UP);
    }

    public void pullExternalData() {

        try {
            List<DataFromFile> data = new LoadData()
                    .loadToList(getTicker().toLowerCase() + ".csv");
            this.volume = data.get(0).getVolume();
            this.currentPrice = data.get(0).getClosingPrice().setScale(4, RoundingMode.HALF_UP);
            this.highestPrice = data.get(0).getHighestPrice().setScale(4, RoundingMode.HALF_UP);
            this.lowestPrice = data.get(0).getLowestPrice().setScale(4, RoundingMode.HALF_UP);
            this.dataTime = data.get(0).getTime();
            this.dataDate = data.get(0).getDate();

        } catch (Exception e) {

            logger.error(this.getTicker().toLowerCase() + ".csv not found (possible connection problems)");
            this.volume = 0L;
            this.currentPrice = BigDecimal.ZERO;
            this.highestPrice = BigDecimal.ZERO;
            this.lowestPrice = BigDecimal.ZERO;
            this.dataTime = LocalTime.MIDNIGHT;
            this.dataDate = LocalDate.MIN;
        }

        this.delay = calculateDelay(this.dataTime);

        try {
            this.currentPE = new LoadData()
                    .loadToList(getTicker().toLowerCase() + "_pe.csv")
                    .get(0)
                    .getClosingPrice()
                    .doubleValue();
        } catch (Exception e) {
            logger.error(this.getTicker().toLowerCase() + "_pe.csv not found (possible connection problems)");
            this.currentPE = 0.0;
        }

        this.fullCompanyName = new LoadData().loadAndScanTickers(getTicker());

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

        return takeProfitPrice.setScale(4, RoundingMode.HALF_UP);
    }

    public BigDecimal getTakeProfitValue() {

        return getTakeProfitPrice().multiply(BigDecimal.valueOf(getSharesTotalAmount())).setScale(4, RoundingMode.HALF_UP);
    }

    public BigDecimal getStopLossPrice() {

        return stopLossPrice.setScale(4, RoundingMode.HALF_UP);
    }

    public BigDecimal getStopLossValue() {

        return getStopLossPrice().multiply(BigDecimal.valueOf(getSharesTotalAmount())).setScale(4, RoundingMode.HALF_UP);
    }

    public BigDecimal getBaseValue() {

        return getAvgBuyPrice().multiply(BigDecimal.valueOf(getSharesTotalAmount())).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getCurrentValue() {

        return getCurrentPrice().multiply(BigDecimal.valueOf(getSharesTotalAmount())).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getCurrentPrice() {

        return currentPrice.setScale(4, RoundingMode.HALF_UP);
    }

    public BigDecimal getAvgBuyPrice() {
        try {
            return this.transactionLinkedList.stream()
                    .map(o -> o.getPrice().setScale(4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(o.getAmount())).setScale(4, RoundingMode.HALF_UP))
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(getSharesTotalAmount()), RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP);
        } catch (ArithmeticException e) {
            return BigDecimal.ZERO;
        }
    }

    public void buy(Integer amount, double price) {
        this.transactionLinkedList.add(new Transaction(amount, BigDecimal.valueOf(price).setScale(4, RoundingMode.HALF_UP)));
        this.transactionHistory.add(new Transaction(amount, BigDecimal.valueOf(price).setScale(4, RoundingMode.HALF_UP)));
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Share share = (Share) o;
        return delay == share.delay &&
                Objects.equals(ticker, share.ticker) &&
                Objects.equals(fullCompanyName, share.fullCompanyName) &&
                Objects.equals(currentPrice, share.currentPrice) &&
                Objects.equals(takeProfitPrice, share.takeProfitPrice) &&
                Objects.equals(stopLossPrice, share.stopLossPrice) &&
                Objects.equals(currentPE, share.currentPE) &&
                Objects.equals(volume, share.volume) &&
                Objects.equals(transactionLinkedList, share.transactionLinkedList) &&
                Objects.equals(transactionHistory, share.transactionHistory) &&
                Objects.equals(highestPrice, share.highestPrice) &&
                Objects.equals(lowestPrice, share.lowestPrice) &&
                Objects.equals(dataDate, share.dataDate) &&
                Objects.equals(dataTime, share.dataTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, fullCompanyName, currentPrice, takeProfitPrice, stopLossPrice, currentPE, volume, transactionLinkedList, transactionHistory, highestPrice, lowestPrice, dataDate, dataTime, delay);
    }
}