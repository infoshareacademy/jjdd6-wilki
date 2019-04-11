package com.infoshareacademy.jjdd6.wilki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.infoshareacademy.jjdd6.wilki.TransactionType.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction implements Serializable {
    public static BigDecimal transactionFee = new BigDecimal(0.0039);

    private Integer amount;
    private BigDecimal price;
    private BigDecimal profit;
    private LocalDate date;
    private BigDecimal transactionFeeValue;
    private TransactionType type;

    public Transaction() {
    }

    public Transaction(Integer amount, BigDecimal price) {

        this.date = LocalDate.now();
        if (amount > 0) {
            this.type = BUY;
        } else {
            this.type = SELL;
        }
        this.amount = amount;
        this.price = price;
        this.transactionFeeValue = BigDecimal.valueOf(amount).multiply(price).multiply(transactionFee);
        this.profit = BigDecimal.ZERO;
    }

    public Transaction(Integer amount, BigDecimal price, BigDecimal profit) {

        this.date = LocalDate.now();
        this.amount = amount;
        this.price = price;
        this.transactionFeeValue = BigDecimal.valueOf(amount).multiply(price).multiply(transactionFee);
        this.profit = profit;
    }

    public BigDecimal getTransactionFee() {

        return transactionFee;
    }


    public Integer getAmount() {

        return amount;
    }

    public void setAmount(Integer amount) {

        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {

        this.price = price;
    }

    public BigDecimal getProfit() {

        return profit;
    }

    public void setProfit(BigDecimal profit) {

        this.profit = profit;
    }

    public BigDecimal getTransactionFeeValue() {
        return transactionFeeValue;
    }

    public void setTransactionFee(BigDecimal transactionFee) {
        this.transactionFee = transactionFee;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTransactionFeeValue(BigDecimal transactionFeeValue) {
        this.transactionFeeValue = transactionFeeValue;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Transaction{");
        sb.append("amount=").append(amount);
        sb.append(", price=").append(price);
        sb.append(", profit=").append(profit);
        sb.append(", date=").append(date);
        sb.append(", transactionFeeValue=").append(transactionFeeValue);
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }
}
