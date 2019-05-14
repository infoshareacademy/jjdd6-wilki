package com.infoshareacademy.jjdd6.view;

import javax.persistence.*;

@Entity
@Table(name = "TICKERS")
@NamedQueries({@NamedQuery(
        name = "Tickers.findAll",
        query = "SELECT t FROM Tickers t")})
public class Tickers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticker")
    String tickerName;

    @Column(name = "full_name")
    String fullName;

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
}