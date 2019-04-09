package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class WalletTest {

    public void walletTest() {

        Wallet wallet = new Wallet();

//        String filter = "KGH";
//        Share found = wallet.getShares().stream()
//                .filter((o) -> o.getTicker().contains(filter))
//                .findFirst()
//                .orElse(new Share(filter));
//        found.buyShares();

        wallet.scanWalletForShare("kgh").buyShares(100,100.3);
        wallet.scanWalletForShare("kgh").buyShares(101,103.3);
        wallet.scanWalletForShare("pkn").buyShares(1020,10.3);
        wallet.scanWalletForShare("pkn").sellShares(1010,10.3);

//        Share share = new Share("kgh");
//        share.buyShares(share, 1000, 100.2);
//        wallet.getShares().add(share);
//
//        Share share2 = new Share("pkn");
//        share.buyShares(share2, 1000, 120.2);
//        wallet.getShares().add(share2);
//
//        share.buyShares(share, 100, 200);
//        share.sellShares(share2, 100, 101.3);
//        share.setStopLossPrice(BigDecimal.valueOf(100.0));
//        share.setTakeProfitPrice(BigDecimal.valueOf(200.4));
        printWallet(wallet);

    }
        public void printWallet(Wallet wallet){

        for (int i = 0; i < wallet.getShares().size(); i++) {
            wallet.getShares().get(i).setCurrentPE();
            wallet.getShares().get(i).setCurrentPrice();
            wallet.getShares().get(i).setVolume();
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
            );

        }


    }

}
