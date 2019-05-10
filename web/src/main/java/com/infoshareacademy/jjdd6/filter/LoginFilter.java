package com.infoshareacademy.jjdd6.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(
        filterName = "LoginFilter",
        urlPatterns = {"/*"})
public class LoginFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();

        if (!req.getRequestURI().equals("/login")) {
            if (session.getAttribute("user") == null) {
                logger.info("User not logged in, trying to login");
                forwardToLogin(req, resp, session);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void forwardToLogin(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws ServletException, IOException {
        if (session.getAttribute("reqPath") == null) {
            session.setAttribute("reqPath", req.getRequestURI());
        }
        logger.info("Requested view: " + req.getRequestURI());
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/login");
        requestDispatcher.forward(req, resp);
    }
}
