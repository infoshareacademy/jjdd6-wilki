package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.validation.Validators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/chart")
public class ChartServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(ChartServlet.class);

    @Inject
    private Validators validators;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ticker = req.getParameter("ticker").toLowerCase();

        if (validators.isTickerNotValid(ticker)) {
            resp.getWriter().println("Ticker = {" + ticker + "} is not valid");
            logger.info("Ticker = {} is not valid.", ticker);
            return;
        }

        resp.getWriter().print("<img src=\"https://stooq.com/c/?s=" + ticker + "&c=5m&t=c&a=lg&b&g.png\" />");
    }
}
