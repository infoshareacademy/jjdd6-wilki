package com.infoshareacademy.jjdd6.wilki;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WalletTest {

    private Wallet wallet;

    @BeforeEach
    void loadWallet() {
        wallet = new WalletToXML().loadFromXml("/home/pewu/IdeaProjects/jjdd6-wilki/app/src/test/resources/testwallet.xml");
    }


    @Test
    void checkIfgetSharesReturnsCorrectListOfShares() {
        //given

        //when
        List<Share> result = wallet.getShares();

        //then
        assertThat(result.size()).isEqualTo(2);

    }

    @Test
    void checkIfgetBaseWorthReturnsCorrectAmount() {
        //given

        //when
        BigDecimal result = wallet.getBaseWorth();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(12450.00).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void checkIfgetCurrentWorthReturnsCorrectAmount() {
        //given

        //when
        BigDecimal result = wallet.getCurrentWorth();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(999373.25).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void checkIfgetSharesCurrentWorthReturnCorrectAmount() {
        //given

        //when
        BigDecimal result = wallet.getSharesCurrentWorth();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(10923.25).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void checkIfgetStopLossWorthReturnsCorrectAmount() {
        //given

        //when
        BigDecimal result = wallet.getStopLossWorth();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(8900.00).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void checkIfgetTakeProfitWorthReturnsCorrectAmount() {
        //given

        //when
        BigDecimal result = wallet.getTakeProfitWorth();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(14100.00).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void checkIfgetCashFromProfitsReturnsCorrectAmount() {
        //given

        //when
        BigDecimal result = wallet.getCashFromProfits();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(450.00).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void checkIfaddToCashFromProfitsWorksCorrectly() {
        //given

        BigDecimal profit = BigDecimal.valueOf(10000.25);

        //when
        wallet.addToCashFromProfits(profit);
        BigDecimal result = wallet.getCashFromProfits();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(10450.25).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void checkIfgetFreeCashReturnsCorrectAmount() {
        //given

        //when
        BigDecimal result = wallet.getFreeCash();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(988450.00).setScale(2, RoundingMode.HALF_UP));

    }


    @Test
    void checkIfgetROEreturnsCorrectNumber() {
        //given

        //when
        BigDecimal result = wallet.getROE();
//        DecimalFormat df = new DecimalFormat("0.00");
//        String result = df.format(resultROE);

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(-0.06));

    }

    @Test
    void checkIfincreaseBaseCashReturnsCorrectAmount() {
        //given

        BigDecimal amount = BigDecimal.valueOf(10000.10);

        //when
        wallet.increaseBaseCash(amount);
        BigDecimal result = wallet.getBaseCash();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(1010000.10).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void checkIfreduceBaseCashReturnsCorrectAmount() {
        //given

        BigDecimal amount = BigDecimal.valueOf(10000.10);

        //when
        wallet.reduceBaseCash(amount);
        BigDecimal result = wallet.getBaseCash();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(989999.90).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void checkIfgetTotalBuyFeesReturnsCorrectAmount() {
        //given

        //when
        BigDecimal result = wallet.getTotalBuyFees();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(46.80).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void checkIfscanWalletForShareReturnsShareWithCorrectTicker() {
        //given


        //when
        Share result = wallet.scanWalletForShare("kgh");

        //then

        assertThat(result.getTicker()).isEqualTo("KGH");
    }

    @Test
    void checkIfscanWalletForShareReturnsShareWhenPresent() {
        //given


        //when
        Share result = wallet.scanWalletForShare("kgh");

        //then

        assertThat(result.getSharesTotalAmount()).isEqualTo(50);
    }

    @Test
    void checkIfscanWalletForShareReturnsNewObjectWhenShareIsNotPresent() {
        //given


        //when
        Share result = wallet.scanWalletForShare("peo");

        //then
        assertThat(result).isNotNull();
        assertThat(result.getSharesTotalAmount()).isEqualTo(0);
    }

    @Test
    void checkIfShareIsPresentShouldReturnTrueIfShareIsIncludedInWallet() {
        //given


        //when
        boolean result = wallet.checkIfShareIsPresent("kgh");

        //then
        assertThat(result).isTrue();

    }

    @Test
    void checkIfsellShareWorksCorrectly() {
        //given
        String ticker = "KGH";
        int amount = 49;
        double price = 200;

        //when
        wallet.sellShare(ticker, amount, price);
        int resultAmount = wallet.getShares().get(0).getSharesTotalAmount();
        BigDecimal resultFreeCash = wallet.getFreeCash();
        BigDecimal resultPriceFromTransactionList = wallet.getShares().get(0).getTransactionHistory().get(1).getPrice();

        //then
        assertThat(resultAmount).isEqualTo(1);
        assertThat(resultFreeCash).isEqualTo(BigDecimal.valueOf(1000700.00).setScale(2, RoundingMode.HALF_UP));
        assertThat(resultPriceFromTransactionList).isEqualTo(BigDecimal.valueOf(200.0000).setScale(4, RoundingMode.HALF_UP));

    }

    @Test
    void CheckIfbuyShareWorksCorrectly() {
        //given
        String ticker = "KGH";
        int amount = 49;
        double price = 200;

        //when
        wallet.buyShare(ticker, amount, price);
        int resultAmount = wallet.getShares().get(0).getSharesTotalAmount();
        BigDecimal resultAvgPrice = wallet.getShares().get(0).getAvgBuyPrice();
        BigDecimal resultFreeCash = wallet.getFreeCash();
        BigDecimal resultPriceFromTransactionList = wallet.getShares().get(0).getTransactionHistory().get(1).getPrice();

        //then
        assertThat(resultAmount).isEqualTo(99);
        assertThat(resultAvgPrice).isEqualTo(BigDecimal.valueOf(174.7475).setScale(4, RoundingMode.HALF_UP));
        assertThat(resultFreeCash).isEqualTo(BigDecimal.valueOf(978650.00).setScale(2, RoundingMode.HALF_UP));
        assertThat(resultPriceFromTransactionList).isEqualTo(BigDecimal.valueOf(200.0000).setScale(4, RoundingMode.HALF_UP));

    }

    @Test
    void shouldReturnTrueIfCashIsEnoughToBuyShare() {
        //given


        //when
        boolean result = wallet.checkIfEnoughCash(100, 100);

        //then
        assertThat(result).isTrue();
    }
}