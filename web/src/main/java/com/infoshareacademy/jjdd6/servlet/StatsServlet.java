package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.dao.StatsDao;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

@WebServlet("/stats")
@Transactional
public class StatsServlet extends HttpServlet {

    @Inject
    StatsDao statsDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("Most traded stocks (buy):");
        statsDao.getMostBoughtStocks();
        for (String mostBoughtStock : statsDao.getMostBoughtStocks()) {
            resp.getWriter().println(mostBoughtStock);
        }
        resp.getWriter().println("Most traded stocks (sell):");
        statsDao.getMostSoldStocks();
        for (String mostSoldStock : statsDao.getMostSoldStocks()) {
            resp.getWriter().println(mostSoldStock);
        }
    }
}
