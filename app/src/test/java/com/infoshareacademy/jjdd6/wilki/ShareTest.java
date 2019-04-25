package com.infoshareacademy.jjdd6.wilki;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ShareTest {

    private Wallet wallet;

    @BeforeEach
    void loadWallet() {
        wallet = new WalletToXML().loadFromXml("testwallet.xml");
    }

    @Test
    void checkIfgetRiskRewardRatioReturnCorrectData() {
        //given

        BigDecimal stopLossPrice = BigDecimal.valueOf(100);
        BigDecimal takeProfitPrice = BigDecimal.valueOf(200);


        //when
        wallet.getShares().get(0).setTakeProfitPrice(takeProfitPrice);
        wallet.getShares().get(0).setStopLossPrice(stopLossPrice);
        Double result = wallet.getShares().get(0).getRiskRewardRatio();

        //then
        assertThat(result).isEqualTo(0.50);

    }

    @Test
    void checkIfgetRiskRewardRatioReturnZeroWhenRatioIsLessThanZero() {
        //given

        BigDecimal stopLossPrice = BigDecimal.valueOf(200).setScale(4,RoundingMode.HALF_UP);
        BigDecimal takeProfitPrice = BigDecimal.valueOf(100).setScale(4, RoundingMode.HALF_UP);


        //when
        wallet.getShares().get(0).setTakeProfitPrice(takeProfitPrice);
        wallet.getShares().get(0).setStopLossPrice(stopLossPrice);
        Double result = wallet.getShares().get(0).getRiskRewardRatio();

        //then
        assertThat(result).isEqualTo(0.00);

    }

    @Test
    void checkIfgetTargetPEcalculatesCorrectRatio() {
        //given

        BigDecimal takeProfitPrice = BigDecimal.valueOf(200).setScale(4,RoundingMode.HALF_UP);

        //when
        wallet.getShares().get(0).setTakeProfitPrice(takeProfitPrice);
        Double result = wallet.getShares().get(0).getTargetPE();
        Double expected = wallet.getShares().get(0).getTakeProfitPrice().setScale(4, RoundingMode.HALF_UP).doubleValue()
                / (wallet.getShares().get(0).getCurrentPE()
                / wallet.getShares().get(0).getCurrentPrice().setScale(4, RoundingMode.HALF_UP)
                .doubleValue());

        //then
        assertThat(result).isEqualTo(expected);

    }

    @Test
    void checkIfgetTakeProfitValueReturnsCorrectValue() {
        //given

        //when
        BigDecimal result = wallet.getShares().get(0).getTakeProfitValue();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(7500.00).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void checkIfgetStopLossValueReturnsCorrectValue() {
        //given

        //when
        BigDecimal result = wallet.getShares().get(0).getStopLossValue();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(4500.00).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void checkIfgetBaseValueReturnsCorrectValue() {
        //given

        //when
        BigDecimal result = wallet.getShares().get(0).getBaseValue();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(7500.00).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void checkIfgetCurrentValueReturnsCorrectValue() {
        //given

        //when
        BigDecimal result = wallet.getShares().get(0).getCurrentValue();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(5360.00).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void checkIfgetAvgBuyPriceReturnsCorrectValue() {
        //given
        wallet.buyShare("PEO", 100,100);
        wallet.buyShare("PEO", 100,200);

        //when
        BigDecimal result = wallet.getShares().get(2).getAvgBuyPrice();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(150.0000).setScale(4, RoundingMode.HALF_UP));

    }

    @Test
    void checkIfgetTotalProfitReturnsCorrectValue() {
        //given
        wallet.buyShare("PEO", 100,100);
        wallet.sellShare("PEO", 50,200);

        //when
        BigDecimal result = wallet.getShares().get(2).getTotalProfit();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(5000.00).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void checkIfgetSharesTotalAmountReturnsCorrectValue() {
        //given
        wallet.buyShare("PEO", 100,100);
        wallet.buyShare("PEO", 50,200);

        //when
        Integer result = wallet.getShares().get(2).getSharesTotalAmount();

        //then
        assertThat(result).isEqualTo(150);

    }

    @Test
    void checkIfgetFeeAmountReturnsCorrectValue() {
        //given
        wallet.buyShare("PEO", 100,100);
        wallet.sellShare("PEO", 50,200);

        //when
        BigDecimal result = wallet.getShares().get(2).getFeeAmount();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(20000.00).multiply(Transaction.transactionFee).setScale(2,RoundingMode.HALF_UP));
    }


}