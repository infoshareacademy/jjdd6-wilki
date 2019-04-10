package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.infoshareacademy.jjdd6.wilki.TransactionType.*;

public class Transaction {

    private Integer amount;
    private BigDecimal price;
    private BigDecimal profit;
    private LocalDate date;
    public static final BigDecimal TRANSACTION_FEE = BigDecimal.valueOf(0.0039);
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
        this.transactionFeeValue = BigDecimal.valueOf(amount).multiply(price).multiply(TRANSACTION_FEE);
        this.profit = BigDecimal.ZERO;
    }

    public Transaction(Integer amount, BigDecimal price, BigDecimal profit) {

        this.date = LocalDate.now();
        this.amount = amount;
        this.price = price;
        this.transactionFeeValue = BigDecimal.valueOf(amount).multiply(price).multiply(TRANSACTION_FEE);
        this.profit = profit;
    }

    public BigDecimal getTransactionFee() {

        return TRANSACTION_FEE;
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
}
