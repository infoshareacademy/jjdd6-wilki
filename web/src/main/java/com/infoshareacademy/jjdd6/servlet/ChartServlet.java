package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.service.ChartGenerator;
import com.infoshareacademy.jjdd6.wilki.DownloadCurrentData;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import com.infoshareacademy.jjdd6.validation.Validators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/chart")
public class ChartServlet extends HttpServlet {

    @Inject
    private ChartGenerator chartGenerator;

    @Inject
    private DownloadCurrentData downloadCurrentData;


    private static Logger logger = LoggerFactory.getLogger(ChartServlet.class);

    @Inject
    private Validators validators;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String ticker = req.getParameter("ticker").toLowerCase();
        String type = req.getParameter("type");
        String monthsStr = req.getParameter("months");
        String from = req.getParameter("from");
        String to = req.getParameter("from");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");


        if (validators.isTickerNotValid(ticker)) {
            logger.info("Ticker = {} is not valid.", ticker);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (type.equals("mini")) {
            String forward = "/images/" + chartGenerator.getMiniChart(ticker);
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(forward);
            requestDispatcher.forward(req, resp);
        } else if (type.equals("full")) {

            if (from != null && !from.isEmpty()) {
                try {
                    LocalDate fromDate = LocalDate.parse(from, df);
                    LocalDate toDate = LocalDate.now();
                    forwardToChart(req, resp, ticker, fromDate, toDate);
                } catch (NumberFormatException e) {
                    resp.setStatus(400);
                    return;
                }
            } else if (monthsStr != null) {
                try {
                    Integer months = Integer.valueOf(req.getParameter("months"));
                    LocalDate fromDate = LocalDate.now().minusMonths(months);
                    LocalDate toDate = LocalDate.now();
                    forwardToChart(req, resp, ticker, fromDate, toDate);
                } catch (NumberFormatException e) {
                    resp.setStatus(400);
                    return;
                }
            } else {
                LocalDate toDate = LocalDate.now();
                LocalDate fromDate = toDate.minusMonths(6);
                forwardToChart(req, resp, ticker, fromDate, toDate);
            }

        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void forwardToChart(HttpServletRequest req, HttpServletResponse resp, String ticker, LocalDate fromDate, LocalDate toDate) throws ServletException, IOException {
        String forward = "/images/" + chartGenerator.getChart(ticker, fromDate, toDate);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(forward);
        requestDispatcher.forward(req, resp);
    }
}
