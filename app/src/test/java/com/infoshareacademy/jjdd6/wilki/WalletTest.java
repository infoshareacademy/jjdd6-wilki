package com.infoshareacademy.jjdd6.wilki;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class WalletTest {

    private Wallet wallet;

    @BeforeEach
    void loadWallet() {
        wallet = new WalletToXML().loadFromXml("testwallet.xml");
    }


    @Test
    void shouldReturn2Shares() {
        //given

        //when
        List<Share> result = wallet.getShares();

        //then
        assertThat(result.size()).isEqualTo(2);

    }

    @Test
    void shouldReturnCorrectBaseWorth() {
        //given

        //when
        BigDecimal result = wallet.getBaseWorth();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(12450.00).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void shouldReturnCorrectCurrentWorth() {
        //given
        BigDecimal expectedValue = wallet.getSharesCurrentWorth().add(wallet.getFreeCash()).setScale(2, RoundingMode.HALF_UP);

        //when
        BigDecimal result = wallet.getCurrentWorth();

        //then
        assertThat(result).isEqualTo(expectedValue);

    }

    @Test
    void shouldReturnCorrectSharesCurrentWorth() {
        //given
        BigDecimal expectedValue = wallet.getShares().stream()
                .map(Share::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        //when
        BigDecimal result = wallet.getSharesCurrentWorth();

        //then
        assertThat(result).isEqualTo(expectedValue);

    }

    @Test
    void shouldNotAddShareWithZeroAmountToList() {
        //given
        wallet.sellShare("PKN", 55, 100);

        //when
        List<Share> result = wallet.walletToDisplay(wallet);

        //then
        assertThat(result.stream().filter(o -> o.getSharesTotalAmount() == 0).collect(Collectors.toList())).size().isEqualTo(0);
    }

    @Test
    void shouldReturnCorrectStopLossWorth() {
        //given

        //when
        BigDecimal result = wallet.getStopLossWorth();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(8900.00).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void shouldReturnCorrectTakeProfitWorth() {
        //given

        //when
        BigDecimal result = wallet.getTakeProfitWorth();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(14100.00).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void shouldReturnCorrectValueOfCashFromProfits() {
        //given

        //when
        BigDecimal result = wallet.getCashFromProfits();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(450.00).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void shouldAddProfitToCashFromProfits() {
        //given

        BigDecimal profit = BigDecimal.valueOf(10000.25);

        //when
        wallet.addToCashFromProfits(profit);
        BigDecimal result = wallet.getCashFromProfits();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(10450.25).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void shouldReturnCorrectAmountOfFreeCash() {
        //given

        //when
        BigDecimal result = wallet.getFreeCash();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(987918.10).setScale(2, RoundingMode.HALF_UP));

    }


    @Test
    void shouldReturnCorrectRoeInPercentsWithoutSymbol() {
        //given
        BigDecimal expectedValue = (wallet.getCurrentWorth().divide(wallet.getBaseCash(), RoundingMode.HALF_UP)).subtract(BigDecimal.ONE).multiply(BigDecimal.valueOf(100.00)).setScale(2, RoundingMode.HALF_UP);
        //when
        BigDecimal result = wallet.getROE();

        //then
        assertThat(result).isEqualTo(expectedValue);

    }

    @Test
    void shouldReturnCorrectAmountOfBaseCash() {
        //given

        BigDecimal amount = BigDecimal.valueOf(10000.10);

        //when
        wallet.increaseBaseCash(amount);
        BigDecimal result = wallet.getBaseCash();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(1010000.10).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void shouldSubstractCashFromBaseCash() {
        //given

        BigDecimal amount = BigDecimal.valueOf(10000.10);

        //when
        wallet.reduceBaseCash(amount);
        BigDecimal result = wallet.getBaseCash();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(989999.90).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void shouldReturnTotalAmountOfPaidFees() {
        //given

        //when
        BigDecimal result = wallet.getTotalBuyFees();

        //then
        assertThat(result).isEqualTo(BigDecimal.valueOf(81.90).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void shouldReturnTickerInUppercase() {
        //given


        //when
        Share result = wallet.scanWalletForShare("kgh");

        //then

        assertThat(result.getTicker()).isEqualTo("KGH");
    }

    @Test
    void shouldReturnShareObjectForCorrectTicker() {
        //given


        //when
        Share result = wallet.scanWalletForShare("kgh");

        //then

        assertThat(result.getSharesTotalAmount()).isEqualTo(50);
    }

    @Test
    void shouldReturnNewShareObjectWhenShareNotInWallet() {
        //given


        //when
        Share result = wallet.scanWalletForShare("peo");

        //then
        assertThat(result).isNotNull();
        assertThat(result.getSharesTotalAmount()).isEqualTo(0);
    }

    @Test
    void shouldReturnTrueWhenShareInWallet() {
        //given


        //when
        boolean result = wallet.checkIfShareIsPresent("kgh");

        //then
        assertThat(result).isTrue();

    }

    @Test
    void shouldUpdateShareAmountFreeCashSharePriceAfterSellTransaction() {
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
        assertThat(resultFreeCash).isEqualTo(BigDecimal.valueOf(997679.88).setScale(2, RoundingMode.HALF_UP));
        assertThat(resultPriceFromTransactionList).isEqualTo(BigDecimal.valueOf(200.0000).setScale(4, RoundingMode.HALF_UP));

    }

    @Test
    void shouldUpdateShareAmountFreeCashAvgPriceAfterBuyTransaction() {
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
        assertThat(resultFreeCash).isEqualTo(BigDecimal.valueOf(978079.88).setScale(2, RoundingMode.HALF_UP));
        assertThat(resultPriceFromTransactionList).isEqualTo(BigDecimal.valueOf(200.0000).setScale(4, RoundingMode.HALF_UP));

    }

    @Test
    void shouldReturnTrueIfEnoughCashToBuyShare() {
        //given


        //when
        boolean result = wallet.checkIfEnoughCash(100, 100);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseIfNotEnoughCashToBuyShare() {
        //given

        //when
        boolean result = wallet.checkIfEnoughCash(10000, 1000);

        //then
        assertThat(result).isFalse();
    }
}