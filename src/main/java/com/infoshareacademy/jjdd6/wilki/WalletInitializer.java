package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;

public class WalletInitializer {

    public Wallet init() {

        Wallet wallet;
        WalletToXML walletToXML = new WalletToXML();
        wallet = walletToXML.loadFromXml();
        Transaction.transactionFee = BigDecimal.valueOf(0.0039);
        wallet.setBaseCash(BigDecimal.valueOf(1000000000));

        return wallet;
    }
}
