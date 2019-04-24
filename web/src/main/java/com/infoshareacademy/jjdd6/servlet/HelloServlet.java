package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.wilki.DummyDataProvider;
import com.infoshareacademy.jjdd6.wilki.Share;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @EJB
    private DummyDataProvider dataProvider;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("Hello from empty wallet!");

        Share share = new Share();

        Point p = dataProvider.getData();

        resp.getWriter().println("My empty share is " + share + " A moje przykladowe dane to:" + p.getX() + ": " + p.getY());
    }
}
