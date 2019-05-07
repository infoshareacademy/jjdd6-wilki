package com.infoshareacademy.jjdd6.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/chart")
public class ChartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ticker = req.getParameter("ticker").toLowerCase();
        resp.getWriter().print("<img src=\"https://stooq.com/c/?s=" + ticker + "&c=5m&t=c&a=lg&b&g.png\" />");
    }
}
