package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class WalletTest {

    public void walletTest() {

        Wallet wallet = new Wallet();
        wallet.setBaseCash(BigDecimal.valueOf(23000));
        wallet.buyShare("kgh", 100, 90.1);
        wallet.buyShare("pkn", 100, 90.3);
        wallet.sellShare("pkn", 99, 140.0);
        printWallet(wallet);
        SaveData saveData = new SaveData();
        saveData.serializeToXml(wallet);

    }
        public void printWallet(Wallet wallet){


        for (int i = 0; i < wallet.getShares().size(); i++) {
            wallet.getShares().get(i).setCurrentPE();
            wallet.getShares().get(i).setCurrentPrice();
            wallet.getShares().get(i).setVolume();
            wallet.getShares().get(i).setFullCompanyName();
            System.out.println(wallet.getShares().get(i).getTicker() + "\t\t"
                    + wallet.getShares().get(i).getCurrentPE() + "\t\t"
                    + wallet.getShares().get(i).getSharesTotalAmount() + "\t\t"
                    + wallet.getShares().get(i).getAvgBuyPrice() + "\t\t"
                    + wallet.getShares().get(i).getBaseValue() + "\t\t"
                    + wallet.getShares().get(i).getFeeAmount() + "\t\t"
                    + wallet.getShares().get(i).getCurrentPrice() + "\t\t"
                    + wallet.getShares().get(i).getCurrentValue() + "\t\t"
                    + wallet.getShares().get(i).getCurrentValue().divide(wallet.getShares().get(i).getBaseValue(), RoundingMode.HALF_UP) + "\t\t"
                    + wallet.getShares().get(i).getStopLossPrice() + "\t\t"
                    + wallet.getShares().get(i).getTakeProfitPrice() + "\t\t"
                    + wallet.getShares().get(i).getTargetPE() + "\t\t"
                    + wallet.getShares().get(i).getRiskRewardRatio() + "\t\t"
                    + wallet.getShares().get(i).getTotalProfit()
                    + wallet.getShares().get(i).getVolume() + "\t\t"
                    + wallet.getShares().get(i).getFeeAmount() + "\t\t"
                    + wallet.getShares().get(i).getFullCompanyName()
            );

        }

            System.out.println(wallet.getROE());
            System.out.println(wallet.getBaseCash());
            System.out.println(wallet.getCurrentWorth());
    }

}
