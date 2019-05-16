package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.service.ChartGenerator;
import com.infoshareacademy.jjdd6.service.StatsService;
import com.infoshareacademy.jjdd6.service.UserService;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

@Transactional
@WebServlet("/stats")
public class StatsServlet extends HttpServlet {

    @Inject
    private ChartGenerator chartGenerator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String chart = req.getParameter("chart");

        if (chart != null && chart.equals("appbuy")) {
            String forward = "/images/" + chartGenerator.getMostTradedBuyChart();
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(forward);
            requestDispatcher.forward(req, resp);
        } else if (chart != null && chart.equals("appsell")) {
            String forward = "/images/" + chartGenerator.getMostTradedSellChart();
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(forward);
            requestDispatcher.forward(req, resp);
        } else {
            String forward = "/images/" + chartGenerator.getMostTradedWSEChart();
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(forward);
            requestDispatcher.forward(req, resp);
        }
    }
}