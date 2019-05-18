package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.service.ChartGenerator;
import com.infoshareacademy.jjdd6.service.UserService;
import com.infoshareacademy.jjdd6.validation.Validators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Transactional
@WebServlet("/chart")
public class ChartServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ChartServlet.class);


    @Inject
    private ChartGenerator chartGenerator;

    @Inject
    private Validators validators;

    @Inject
    UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String ticker = req.getParameter("ticker").toLowerCase();
        String type = req.getParameter("type");
        String monthsStr = req.getParameter("months");
        String from = req.getParameter("from");
        String buyPriceStr = req.getParameter("buyprice");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String mini = "mini";
        String full = "full";

        Double buyPrice = 0.0;
        if (buyPriceStr != null && !buyPriceStr.isEmpty()) {
            try {
                buyPrice = Double.parseDouble(buyPriceStr);
            } catch (NumberFormatException e) {
                logger.error("Error while parsing buy price, omitting");
            }
        }

        if (validators.isTickerNotValid(ticker)) {
            logger.info("Ticker = {} is not valid.", ticker);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (validators.isTypeIncorrect(type, mini, full)) {
            logger.info("Incorrect chart type = {} .", type);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (type.equals(mini)) {
            Optional<LocalDate> fromDateOpt = userService.loggedUser(req)
                    .getWallet()
                    .scanWalletForShare(ticker)
                    .getTransactionHistory().stream()
                    .filter(o -> o.getAmountForCalc() > 0)
                    .limit(1)
                    .map(o -> o.getDate())
                    .findFirst();
            LocalDate fromDate;
            if (fromDateOpt.isPresent()) {
                fromDate = fromDateOpt.get();
            } else {
                fromDate = LocalDate.now().minusMonths(3);
            }
            String forward = "/images/" + chartGenerator.getMiniChart(ticker, fromDate);
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(forward);
            requestDispatcher.forward(req, resp);
        } else if (type.equals(full)) {

            if (from != null && !from.isEmpty()) {
                try {
                    LocalDate fromDate = LocalDate.parse(from, df);
                    LocalDate toDate = LocalDate.now();
                    forwardToChart(req, resp, ticker, fromDate, toDate, buyPrice);
                } catch (NumberFormatException e) {
                    resp.setStatus(400);
                    return;
                }
            } else if (monthsStr != null) {

                try {
                    Integer months = Integer.valueOf(req.getParameter("months"));
                    LocalDate fromDate = LocalDate.now().minusMonths(months);
                    LocalDate toDate = LocalDate.now();
                    forwardToChart(req, resp, ticker, fromDate, toDate, buyPrice);
                } catch (NumberFormatException e) {
                    resp.setStatus(400);
                    return;
                }
            } else {
                LocalDate toDate = LocalDate.now();
                LocalDate fromDate = toDate.minusMonths(6);
                forwardToChart(req, resp, ticker, fromDate, toDate, buyPrice);
            }

        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void forwardToChart(HttpServletRequest req, HttpServletResponse resp, String ticker, LocalDate fromDate, LocalDate toDate, Double buyPrice) throws ServletException, IOException {
        String forward = "/images/" + chartGenerator.getChart(ticker, fromDate, toDate, buyPrice);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(forward);
        requestDispatcher.forward(req, resp);
    }
}
