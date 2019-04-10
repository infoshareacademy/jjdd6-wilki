package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.infoshareacademy.jjdd6.wilki.TransactionType.*;

public class Transaction {
    public static BigDecimal transactionFee;

    private Integer amount;
    private BigDecimal price;
    private BigDecimal profit;
    private LocalDate date;
    private BigDecimal transactionFeeValue;
    private TransactionType type;



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

    public void setPrice(BigDecimal buyPrice) {

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
}
