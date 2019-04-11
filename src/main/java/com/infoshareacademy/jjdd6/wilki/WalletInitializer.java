package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;

public class WalletInitializer {

    public Wallet init() {

        Wallet wallet = new Wallet();
        WalletToXML walletToXML = new WalletToXML();
        wallet = walletToXML.loadFromXml();
        Transaction.transactionFee = BigDecimal.valueOf(0.0039);

        wallet.setBaseCash(BigDecimal.valueOf(200000));
        wallet.buyShare("KGH", 254, 500);
        wallet.buyShare("PKN", 164, 500);

        return wallet;
    }
}
