package com.infoshareacademy.jjdd6.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

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

        if (!req.getRequestURI().equals("/login") && !req.getRequestURI().equals("/chart")) {
            if (session.getAttribute("user") == null || checkIfTokenExpired(session)) {
                logger.info("User not logged in, trying to login");
                forwardToLogin(req, resp, session);
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean checkIfTokenExpired(HttpSession session) {
        LocalDateTime expireDate = (LocalDateTime) session.getAttribute("tokenExpireDate");
        if (expireDate.isBefore(LocalDateTime.now())) {
            logger.info("Token expired on " + expireDate + " for user id: " + session.getAttribute("user"));
            session.invalidate();
            return true;
        }
        return false;
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
