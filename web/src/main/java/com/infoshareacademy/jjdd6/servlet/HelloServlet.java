package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.wilki.Share;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("Hello from empty wallet!");

        Share share = new Share();

        resp.getWriter().println("My empty share is " + share);
    }
}
