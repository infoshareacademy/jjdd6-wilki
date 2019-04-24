package com.infoshareacademy.jjdd6.wilki;

import javax.ejb.Stateless;
import java.math.BigDecimal;

@Stateless
public class WalletInitializer {

    public Wallet init() {
        Wallet wallet;
        WalletToXML walletToXML = new WalletToXML();
        wallet = walletToXML.loadFromXml();
        Transaction.transactionFee = BigDecimal.valueOf(0.0039);

        return wallet;
    }
}