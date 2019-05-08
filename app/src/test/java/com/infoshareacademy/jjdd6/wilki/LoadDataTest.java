package com.infoshareacademy.jjdd6.wilki;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoadDataTest {

    @Test
    void shouldReturnFullNameForCorrectTicker() {
        //given
        String correctTicker = "KGH";

        //when
        String result = new DownloadCurrentData().loadAndScanTickers(correctTicker);

        //then
        assertThat(result).isEqualTo("KGHM POLSKA MIEDŹ SA");

    }

    @Test
    void shouldReturnTrueForCorrectTicker() {
        //given
        String correctTicker = "KGH";

        //when
        Boolean result = new DownloadCurrentData().validateTicker(correctTicker);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenTickerIsInvalid() {
        //given
        String falseTicker = "KGA";

        //when
        Boolean result = new DownloadCurrentData().validateTicker(falseTicker);

        //then
        assertThat(result).isFalse();
    }

}