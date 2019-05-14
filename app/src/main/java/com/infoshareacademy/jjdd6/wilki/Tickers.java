package com.infoshareacademy.jjdd6.wilki;

import javax.persistence.*;

@Entity
@Table(name = "TICKERS")
@NamedQueries({@NamedQuery(
        name = "Tickers.findAll",
        query = "SELECT t FROM Tickers t"),
        @NamedQuery(
                name = "Tickers.getCount",
                query = "SELECT COUNT(*) FROM Tickers t")
})
public class Tickers {

    @Id
    @Column(name = "ticker", length = 4)
    private String tickerName;

    @Column(name = "full_name")
    private String fullName;

    public Tickers() {
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

    public Tickers(String tickerName, String fullName) {
        this.tickerName = tickerName;
        this.fullName = fullName;
    }
}