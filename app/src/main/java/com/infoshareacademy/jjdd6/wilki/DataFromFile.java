package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class DataFromFile {
    private String symbol;
    private LocalDate date;
    private LocalTime time;
    private BigDecimal openingPrice;
    private BigDecimal highestPrice;
    private BigDecimal lowestPrice;
    private BigDecimal closingPrice;
    private Long volume;
    private Long turnover;
    private String change;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public BigDecimal getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(BigDecimal openingPrice) {
        this.openingPrice = openingPrice;
    }

    public BigDecimal getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(BigDecimal highestPrice) {
        this.highestPrice = highestPrice;
    }

    public BigDecimal getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(BigDecimal lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public BigDecimal getClosingPrice() {
        return closingPrice;
    }

    public void setClosingPrice(BigDecimal closingPrice) {
        this.closingPrice = closingPrice;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Long getTurnover() {
        return turnover;
    }

    public void setTurnover(Long turnover) {
        this.turnover = turnover;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    @Override
    public String toString() {
        return "DataFromFile{" +
                "symbol='" + symbol + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", openingPrice=" + openingPrice +
                ", highestPrice=" + highestPrice +
                ", lowestPrice=" + lowestPrice +
                ", closingPrice=" + closingPrice +
                ", volume=" + volume +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataFromFile that = (DataFromFile) o;
        return Objects.equals(symbol, that.symbol) &&
                Objects.equals(date, that.date) &&
                Objects.equals(time, that.time) &&
                Objects.equals(openingPrice, that.openingPrice) &&
                Objects.equals(highestPrice, that.highestPrice) &&
                Objects.equals(lowestPrice, that.lowestPrice) &&
                Objects.equals(closingPrice, that.closingPrice) &&
                Objects.equals(volume, that.volume) &&
                Objects.equals(turnover, that.turnover) &&
                Objects.equals(change, that.change);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, date, time, openingPrice, highestPrice, lowestPrice, closingPrice, volume, turnover, change);
    }
}

