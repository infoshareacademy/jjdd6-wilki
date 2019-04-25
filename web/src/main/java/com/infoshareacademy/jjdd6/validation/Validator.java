package com.infoshareacademy.jjdd6.validation;

import com.infoshareacademy.jjdd6.wilki.LoadData;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isAlpha;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@RequestScoped
public class Validator {

    @Inject
    LoadData loadData;

    public boolean isTickerValid(String ticker) {
        return (loadData.validateTicker(ticker)
                && isEmpty(ticker)
                && isAlpha(ticker));
    }

    public boolean isNotEmptyAndIsNumeric(String text) {
        return (isNumeric(text)
                && isEmpty(text));
    }

}
