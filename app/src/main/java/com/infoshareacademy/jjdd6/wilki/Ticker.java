package com.infoshareacademy.jjdd6.wilki;

import javax.persistence.*;

@Entity
@Table(name = "TICKERS")
@NamedQueries({@NamedQuery(
        name = "Tickers.findAll",
        query = "SELECT t FROM Ticker t"),
        @NamedQuery(
                name = "Tickers.getCount",
                query = "SELECT COUNT(*) FROM Ticker t")
})
public class Ticker {

    @Id
    @Column(name = "ticker", length = 4)
    private String tickerName;

    @Column(name = "full_name")
    private String fullName;

    public Ticker() {
    }

    public String getTickerName() {
        return tickerName;
    }

    public void setTickerName(String tickerName) {
        this.tickerName = tickerName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Ticker(String tickerName, String fullName) {
        this.tickerName = tickerName;
        this.fullName = fullName;
    }
}