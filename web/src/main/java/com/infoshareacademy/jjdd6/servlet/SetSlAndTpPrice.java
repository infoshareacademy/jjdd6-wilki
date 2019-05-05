package com.infoshareacademy.jjdd6.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/sl-and-tp")
public class SetSlAndTpPrice extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(SetSlAndTpPrice.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final String action = getAction(req, resp);
        if (action == null) {
            return;
        }

        if (action.equals("sl")) {
            setStopLoos(req, resp);
        } else if (action.equals("tp")) {
            setTakeProfit(req, resp);
        } else {
            resp.getWriter().write("Unknown action.");
        }
    }

    private String getAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String action = req.getParameter("action");
        logger.info("Requested action: {}", action);
        if (action == null || action.isEmpty()) {
            resp.getWriter().write("Empty action parameter.");
        }
        return action;
    }

    private void setStopLoos(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

    }

    private void setTakeProfit(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

    }
}
