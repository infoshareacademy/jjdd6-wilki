package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;

import static com.infoshareacademy.jjdd6.wilki.WalletToXML.SERIALIZED_FILE_NAME;

public class WalletInitializer {

    public Wallet init() {

        Wallet wallet;
        WalletToXML walletToXML = new WalletToXML();
        wallet = walletToXML.loadFromXml(SERIALIZED_FILE_NAME);
        Transaction.transactionFee = BigDecimal.valueOf(0.0039);

        return wallet;
    }
}