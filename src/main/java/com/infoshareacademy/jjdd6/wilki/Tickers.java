package com.infoshareacademy.jjdd6.wilki;

public enum Tickers {
    ALR("Alior Bank"),
    CDR("CD Projekt"),
    CCC("CCC"),
    CPS("Cyfrowy Polsat"),
    DNP("Dino Polska"),
    JSW("JSW"),
    LPP("LPP"),
    KGH("KGHM"),
    PKN("PKN Orlen");

    private String fullCompanyName;

    Tickers(String fullCompanyName) {
        this.fullCompanyName = fullCompanyName;
    }

    public String getFullCompanyName() {
        return fullCompanyName;
    }
}
