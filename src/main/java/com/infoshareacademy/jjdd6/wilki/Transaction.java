package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;

public class Transaction {
    private Integer amount;
    private BigDecimal buyPrice;
    private BigDecimal sellPrice;

    public Transaction(Integer amount, BigDecimal buyPrice) {
        this.amount = amount;
        this.buyPrice = buyPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }
}
