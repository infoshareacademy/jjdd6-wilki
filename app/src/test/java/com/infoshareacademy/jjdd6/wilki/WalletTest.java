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
    void loadWallet(){
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
    void getCashFromProfits() {
    }

    @Test
    void addToCashFromProfits() {
    }

    @Test
    void getFreeCash() {
    }


    @Test
    void checkIfgetROEreturnsCorrectNumber() {
        //given

        //when
       double result = wallet.getROE();

        //then
        assertThat(result).isEqualTo(-6.267500000000092E-4);

    }

    @Test
    void increaseBaseCash() {
    }

    @Test
    void reduceBaseCash() {
    }

    @Test
    void getTotalBuyFees() {
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

        assertThat(result.getSharesTotalAmount()).isEqualTo(150);
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
    void sellShare() {
    }

    @Test
    void buyShare() {
    }

    @Test
    void setCashFromProfits() {
    }

    @Test
    void shouldReturnTrueIfCashIsEnoughToBuyShare() {
        //given


        //when
        boolean result = wallet.checkIfEnoughCash(100,100);

        //then
        assertThat(result).isTrue();


    }
}