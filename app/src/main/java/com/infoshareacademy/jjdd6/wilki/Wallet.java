package com.infoshareacademy.jjdd6.wilki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "WALLET")
@NamedQueries({@NamedQuery(
        name = "Wallet.findAll",
        query = "SELECT w FROM Wallet w")
})
public class Wallet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToMany
    @JoinTable(name = "WALLET_TO_SHARE",
            joinColumns = @JoinColumn(name = "wallet_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "share_id", referencedColumnName = "id"))
    private List<Share> shares = new LinkedList<>();

    @Column(name = "base_cash")
    @NotNull
    private BigDecimal baseCash = BigDecimal.ZERO;

    @Column(name = "cash_from_profits")
    @NotNull
    private BigDecimal cashFromProfits = BigDecimal.ZERO;

    public Wallet() {

    }

    public Wallet(List<Share> shares, @NotNull BigDecimal baseCash, @NotNull BigDecimal cashFromProfits) {
        this.shares = shares;
        this.baseCash = baseCash;
        this.cashFromProfits = cashFromProfits;
    }

    public List<Share> getShares() {

        return shares;
    }


    public BigDecimal getBaseWorth() {

        return this.shares.stream()
                .map(Share::getBaseValue)
                .reduce(BigDecimal.ZERO, (a, e) -> a.add(e))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getCurrentWorth() {

        return this.getSharesCurrentWorth().add(getFreeCash()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSharesCurrentWorth() {

        return walletToDisplay().stream()
                .map(Share::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getStopLossWorth() {

        return this.shares.stream()
                .map(Share::getStopLossValue)
                .reduce(BigDecimal.ZERO, (a, e) -> a.add(e))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTakeProfitWorth() {

        return this.shares.stream()
                .map(Share::getTakeProfitValue)
                .reduce(BigDecimal.ZERO, (a, e) -> a.add(e))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getCashFromProfits() {
        return cashFromProfits.setScale(2, RoundingMode.HALF_UP);
    }

    public void addToCashFromProfits(BigDecimal profit) {
        this.cashFromProfits = this.cashFromProfits.add(profit).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getFreeCash() {

        return getCashFromProfits()
                .add(getBaseCash())
                .subtract(getBaseWorth()).setScale(2, RoundingMode.HALF_UP)
                .subtract(getTotalBuyFees().setScale(2, RoundingMode.HALF_UP));
    }

    public BigDecimal getBaseCash() {

        return baseCash.setScale(2, RoundingMode.HALF_UP);
    }

    public void setBaseCash(BigDecimal baseCash) {

        this.baseCash = baseCash.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getROE() {
        try {
            return (getCurrentWorth().divide(getBaseCash(), RoundingMode.HALF_UP)).subtract(BigDecimal.ONE).multiply(BigDecimal.valueOf(100.00)).setScale(2, RoundingMode.HALF_UP);
        } catch (ArithmeticException e) {
            return BigDecimal.ZERO;
        }

    }

    public void increaseBaseCash(BigDecimal amount) {

        setBaseCash(getBaseCash().add(amount));
    }

    public void reduceBaseCash(BigDecimal amount) {

        setBaseCash(getBaseCash().subtract(amount));

    }

    public BigDecimal getTotalBuyFees() {

        return this.shares.stream()
                .map(Share::getFeeAmount)
                .reduce(BigDecimal.ZERO, (a, e) -> a.add(e))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public Share scanWalletForShare(String ticker) {
        return this.getShares().stream()
                .filter((o) -> o.getTicker().contains(ticker.toUpperCase()))
                .findFirst()
                .orElse(new Share(ticker.toUpperCase()));
    }

    public boolean checkIfShareIsPresent(String ticker) {
        return this.getShares().stream()
                .filter(o -> o.getSharesTotalAmount() > 0)
                .filter((o) -> o.getTicker().contains(ticker.toUpperCase()))
                .count() == 1;
    }

    public void sellShare(String ticker, int amount, double price) {
        Share result = sellShareCommon(ticker, amount, price);

    }

    private Share sellShareCommon(String ticker, int amount, double price) {
        Share result = this.getShares().stream()
                .filter((o) -> o.getTicker().contains(ticker.toUpperCase()))
                .findFirst()
                .get();

        BigDecimal profit = result.sell(amount, price);
        this.addToCashFromProfits(profit);

        return result;
    }

    public void sellShare(String ticker, int amount, double price, LocalDate date) {
        Share result = sellShareCommon(ticker, amount, price);

    }

    public void buyShare(String ticker, int amount, double price) {
        Share result = buyShareCommon(ticker, amount, price);
    }

    private Share buyShareCommon(String ticker, int amount, double price) {
        Share result = scanWalletForShare(ticker.toUpperCase());
        result.buy(amount, price);

        if (this.getShares().stream()
                .filter((o) -> o.getTicker().contains(ticker.toUpperCase()))
                .count() == 0) {
            this.getShares().add(result);
        }
        DownloadCurrentData.updateWalletData(this);
        return result;
    }

    public void buyShare(String ticker, int amount, double price, LocalDate date) {
        Share result = buyShareCommon(ticker, amount, price);
    }

    public void setShares(List<Share> shares) {
        this.shares = shares;
    }

    public void setCashFromProfits(BigDecimal cashFromProfits) {
        this.cashFromProfits = cashFromProfits;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean checkIfEnoughCash(int amount, double price) {
        return amount * price <= getFreeCash().doubleValue();
    }

    public List<Share> walletToDisplay(Wallet wallet) {
        return wallet.getShares().stream()
                .filter(o -> o.getSharesTotalAmount() > 0)
                .collect(Collectors.toList());
    }

    public List<Share> walletToDisplay() {
        return this.getShares().stream()
                .filter(o -> o.getSharesTotalAmount() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                "shares=" + shares +
                ", baseCash=" + baseCash +
                ", cashFromProfits=" + cashFromProfits +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(shares, wallet.shares) &&
                Objects.equals(baseCash, wallet.baseCash) &&
                Objects.equals(cashFromProfits, wallet.cashFromProfits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shares, baseCash, cashFromProfits);
    }
}