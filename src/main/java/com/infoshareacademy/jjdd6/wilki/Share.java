package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;

import static javax.print.attribute.standard.MediaSizeName.B;

public class Share {
    private String ticker;
    private String fullCompanyName;
    private BigDecimal currentPrice;
    private BigDecimal takeProfitPrice;
    private BigDecimal stopLossPrice;
    private Integer sharesAmount;
    private BigDecimal avgBuyPrice;
    private BigDecimal buyPrice;
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
        Share share = new Share("KGH", 100,BigDecimal.valueOf(103.75));
        


    }

    public Share(String ticker, Integer sharesAmount, BigDecimal buyPrice, BigDecimal takeProfitPrice, BigDecimal stopLossPrice) {
        this.ticker = ticker;
        this.takeProfitPrice = takeProfitPrice;
        this.stopLossPrice = stopLossPrice;
        this.sharesAmount = sharesAmount;
        this.buyPrice = buyPrice;
    }

    public Share(String ticker, Integer sharesAmount, BigDecimal buyPrice) {
        this.ticker = ticker;
        this.buyPrice = buyPrice;
        this.sharesAmount = sharesAmount;
    }


}
