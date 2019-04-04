package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;

public class Transaction {
    private Integer amount;
    private BigDecimal price;
    private BigDecimal profit;

    public Transaction(Integer amount, BigDecimal price) {
        this.amount = amount;
        this.price = price;
    }

    public Transaction(Integer amount, BigDecimal price, BigDecimal profit) {
        this.amount = amount;
        this.price = price;
        this.profit = profit;
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
}
