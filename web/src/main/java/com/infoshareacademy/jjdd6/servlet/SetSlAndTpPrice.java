package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.validation.Validator;
import com.infoshareacademy.jjdd6.wilki.Share;
import com.infoshareacademy.jjdd6.wilki.Wallet;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/sl-and-tp")
public class SetSlAndTpPrice extends HttpServlet {

    @Inject
    Validator validator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String ticker = req.getParameter("ticker");
        String stopLoss = req.getParameter("stopLoss");
        String takeProfit = req.getParameter("takeProfit");

        validator.isNotEmptyIsNumeric(stopLoss);
        validator.isNotEmptyIsNumeric(takeProfit);
        validator.isPositiveNumber(stopLoss);
        validator.isPositiveNumber(takeProfit);

    }
}
