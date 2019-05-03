//package com.infoshareacademy.jjdd6.servlet;
//
//import com.infoshareacademy.jjdd6.validation.Validator;
//import com.infoshareacademy.jjdd6.wilki.Wallet;
//
//import javax.inject.Inject;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebServlet("/share/sell")
//public class SellSharesServlet extends HttpServlet {
//
//    @Inject
//    Wallet wallet;
//
//    @Inject
//    private Validator validator;
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
////        wyświetlenie strony
////         stan kasy - ile mamy
////         lista tickerów z nazwami (z bazy - wczytane na wejściu)
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//
//        String ticker = req.getParameter("ticker");
//        String amount = req.getParameter("amount");
//        String price = req.getParameter("price");
//
//        validator.isTickerValid(ticker);
//        validator.isNotEmptyIsNumeric(amount);
//        validator.isEnoughShares(amount);
//        validator.isNotEmptyIsNumeric(price);
//        validator.isPositiveNumber(amount);
//        validator.isPositiveNumber(price);
//
//        int amountInteger = Integer.parseInt(amount);
//        double priceDouble = Double.parseDouble(price);
//
////        wallet.sellShare(ticker, amountInteger, priceDouble);
//
//    }
//}
