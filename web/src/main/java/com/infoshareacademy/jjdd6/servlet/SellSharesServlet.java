package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.validation.TickerValidator;
import com.infoshareacademy.jjdd6.wilki.Wallet;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/share/buy")
public class SellSharesServlet extends HttpServlet {

    @Inject
    Wallet wallet;

    @Inject
    private TickerValidator  tickerValidator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        wyświetlenie strony
//         stan kasy - ile mamy
//         lista tickerów z nazwami (z bazy - wczytane na wejściu)
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {


        tickerValidator.isTickerValid(req.getParameter("ticker"));

        String ticker = req.getParameter("ticker");
        String amount = req.getParameter("amount");
        String price = req.getParameter("price");
        String date = req.getParameter("date");

        if (ticker == null || ticker.isEmpty()
                || amount == null || amount.isEmpty()
                || price == null || price.isEmpty()
                || date == null || date.isEmpty()) {
            resp.getWriter().write("All parameters are obligatory!");
            return;
        }

        int amountInteger = Integer.parseInt(amount);
        double priceDouble = Double.parseDouble(price);

        wallet.sellShare(ticker, amountInteger, priceDouble);

    }
}
