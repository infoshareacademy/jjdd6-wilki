package com.infoshareacademy.jjdd6.wilki;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LoadDataTest {

//    @Test
//    void checkIfloadAndScanTickersReturnsFullNameForCorrectTicker() {
//        //given
//        String correctTicker = "KGH";
//
//        //when
//        String result = new LoadData().loadAndScanTickers(correctTicker);
//
//        //then
//        assertThat(result).isEqualTo("KGHM POLSKA MIEDŹ SA");
//
//    }

    @Test
    void checkIfvalidateTickerReturnsTrueForCorrectTicker() {
        //given
        String correctTicker = "KGH";

        //when
        Boolean result = new DownloadCurrentData().validateTicker(correctTicker);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void checkIfvalidateTickerReturnsFalseWhenTickerIsInvalid() {
        //given
        String falseTicker = "KGA";

        //when
        Boolean result = new DownloadCurrentData().validateTicker(falseTicker);

        //then
        assertThat(result).isFalse();
    }

}