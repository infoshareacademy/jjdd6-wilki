package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;

public class Share {
    private String ticker;
    private String fullCompanyName;
    private BigDecimal currentPrice;
    private BigDecimal takeProfitPrice;
    private BigDecimal stopLossPrice;
    private Integer sharesAmount;
    private BigDecimal avgBuyPrice;
    private Double currentPE;
    private Double targetPE;
    private Double riskRewardRatio;
    private Long volume;


    // targetPE = takeProfitPrice / (currentPE / currentPrice)
    // currentPrice = cena zamkniecia z pliku CSV
    // riskRewardRatio = (avgBuyPrice - stopLossPrice) / (takeProfitPrice - stopLossPrice)
    // Lista z aktualnym stanem do wyliczenia avgBuyPrice i sharesAmount. Przy kupnie do listy dodany bedzie nowy wiersz, a sprzedaz bedzie zgodnie z metoda FIFO
    // Lista z historia transakcji do podgladu dla uzytkownika

    public void run() {


    }

    public Share(String ticker, BigDecimal takeProfitPrice, BigDecimal stopLossPrice, Integer sharesAmount) {
        this.ticker = ticker;
        this.takeProfitPrice = takeProfitPrice;
        this.stopLossPrice = stopLossPrice;
        this.sharesAmount = sharesAmount;
    }


    public Share(String ticker, Integer sharesAmount) {
        this.ticker = ticker;
        this.sharesAmount = sharesAmount;
    }
}
