package com.infoshareacademy.jjdd6.filter;

import com.infoshareacademy.jjdd6.dao.FacebookTokenDao;
import com.infoshareacademy.jjdd6.dao.UserDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.wilki.FacebookToken;
import com.infoshareacademy.jjdd6.wilki.User;
import com.infoshareacademy.jjdd6.wilki.Wallet;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;

@Transactional
@WebFilter(
        filterName = "CreateAdminFilter",
        urlPatterns = {"/*"})
public class CreateAdminFilter implements Filter {

    @Inject
    private UserDao userDao;

    @Inject
    private WalletDao walletDao;

    @Inject
    private FacebookTokenDao facebookTokenDao;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpSession session = req.getSession();

        if (userDao.findAll().isEmpty()) {

            final Wallet wallet = new Wallet();
            wallet.setBaseCash(BigDecimal.valueOf(1000));
            walletDao.save(wallet);

            final User user = new User();
            user.setEmail("testuser@test");
            user.setWallet(wallet);
            user.setFbUserId("test");
            user.setName("Adam Testowy");

            FacebookToken facebookToken = new FacebookToken();
            facebookToken.setAccessToken("test");
            facebookToken.setExpirationSeconds(999999999L);
            facebookToken.setTokenType("test");
            user.setUserToken(facebookToken);
            facebookTokenDao.save(facebookToken);
            userDao.save(user);
            session.setAttribute("user", user.getId());
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}