package com.infoshareacademy.jjdd6.wilki;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletTest {

    private Wallet wallet;

    @BeforeEach
    void loadWallet(){
        wallet = new WalletToXML().loadFromXml("/home/pewu/IdeaProjects/jjdd6-wilki/app/src/test/resources/testwallet.xml");
    }


    @Test
    void getShares() {
    }

    @Test
    void getBaseWorth() {
    }

    @Test
    void getCurrentWorth() {
    }

    @Test
    void getSharesCurrentWorth() {
    }

    @Test
    void getStopLossWorth() {
    }

    @Test
    void getTakeProfitWorth() {
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
    void getROE() {
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