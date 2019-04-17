package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class DataFromFile {
    private String symbol;
    private LocalDate date;
    private LocalTime time;
    private BigDecimal openingPrice;
    private BigDecimal highestPrice;
    private BigDecimal lowestPrice;
    private BigDecimal closingPrice;
    private Long volume;

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
}

