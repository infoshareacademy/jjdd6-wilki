package com.infoshareacademy.jjdd6.wilki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static com.infoshareacademy.jjdd6.wilki.TransactionType.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "TRANSACTION")
public class Transaction implements Serializable {
    public static BigDecimal transactionFee = new BigDecimal(0.0039);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    @NotNull
    private Integer amount;

    @Column(name = "price")
    @NotNull
    private BigDecimal price;

    @Column(name = "profit")
    @NotNull
    private BigDecimal profit;

    @Column(name = "date")
    @NotNull
    private LocalDate date;

    @Column(name = "transaction_fee_value")
    @NotNull
    private BigDecimal transactionFeeValue;

    @Column(name = "transaction_type")
    @NotNull
    private TransactionType type;

    private String ticker;
    private String fullCompanyName;


    @ManyToOne
    @JoinColumn(name = "share_id")
    private Share share;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

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
        this.transactionFeeValue = BigDecimal.valueOf(Math.abs(amount)).multiply(price).multiply(transactionFee);
        this.profit = BigDecimal.ZERO;
    }

    public Transaction(Integer amount, BigDecimal price, BigDecimal profit) {

        this.date = LocalDate.now();
        if (amount > 0) {
            this.type = BUY;
        } else {
            this.type = SELL;
        }
        this.amount = amount;
        this.price = price;
        this.transactionFeeValue = BigDecimal.valueOf(Math.abs(amount)).multiply(price).multiply(transactionFee);
        this.profit = profit;
    }

    public Transaction(LocalDate date, Integer amount, BigDecimal price, BigDecimal profit) {

        this.date = date;
        if (amount > 0) {
            this.type = BUY;
        } else {
            this.type = SELL;
        }
        this.amount = amount;
        this.price = price;
        this.transactionFeeValue = BigDecimal.valueOf(Math.abs(amount)).multiply(price).multiply(transactionFee);
        this.profit = profit;
    }

    public Transaction(String ticker, String fullCompanyName, LocalDate date, Integer amount, BigDecimal price, BigDecimal profit) {

        this.ticker = ticker;
        this.fullCompanyName = fullCompanyName;
        this.date = date;
        if (amount > 0) {
            this.type = BUY;
        } else {
            this.type = SELL;
        }
        this.amount = amount;
        this.price = price;
        this.transactionFeeValue = BigDecimal.valueOf(Math.abs(amount)).multiply(price).multiply(transactionFee);
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Share getShare() {
        return share;
    }

    public void setShare(Share share) {
        this.share = share;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return amount.equals(that.amount) &&
                price.equals(that.price) &&
                profit.equals(that.profit) &&
                date.equals(that.date) &&
                transactionFeeValue.equals(that.transactionFeeValue) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, price, profit, date, transactionFeeValue, type);
    }
}