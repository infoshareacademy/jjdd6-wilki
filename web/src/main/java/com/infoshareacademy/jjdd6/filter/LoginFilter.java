package com.infoshareacademy.jjdd6.filter;

import com.infoshareacademy.jjdd6.dao.FacebookTokenDao;
import com.infoshareacademy.jjdd6.dao.UserDao;
import com.infoshareacademy.jjdd6.dao.WalletDao;
import com.infoshareacademy.jjdd6.wilki.FacebookToken;
import com.infoshareacademy.jjdd6.wilki.FacebookUser;
import com.infoshareacademy.jjdd6.wilki.User;
import com.infoshareacademy.jjdd6.wilki.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebFilter(
        filterName = "LoginFilter",
        urlPatterns = {"/*"})
public class LoginFilter implements Filter {

    private final String STATE = String.valueOf(Math.random() * 1000 * Math.random() * 1000);
    private final String APP_ID = "2337908682898870";
    private final String REDIRECT_URL = "http://localhost:8080/wallet";
    private final String APP_SECRET = "918fce13c991ddf3477eeb04bf4c5a4f";
    private static Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Inject
    UserDao userDao;

    @Inject
    WalletDao walletDao;

    @Inject
    FacebookTokenDao facebookTokenDao;

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
        User user = (User) session.getAttribute("user");

        if (user != null) {
            if (user.getUserToken().getExpireDate().isBefore(LocalDateTime.now())) {
                logger.info("Token expired on " + user.getUserToken().getExpireDate() + " for user " + user.getName() + "");
                session.invalidate();
            }
        }

        if (user == null) {
            String error = req.getParameter("error");

            if (error != null && !error.isEmpty()) {
                logger.info("Unauthorized login attempt: {}", error);
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            if (req.getParameter("state") == null || !req.getParameter("state").equals(STATE)) {

                resp.sendRedirect("https://www.facebook.com/v3.3/dialog/oauth?client_id=" + APP_ID
                        + "&redirect_uri=" + REDIRECT_URL
                        + "&state=" + STATE);
            } else {
                String code = req.getParameter("code");
                logger.info("Returned code: " + code);
                if (code != null && !code.isEmpty()) {
                    final Client client = ClientBuilder.newClient();
                    final WebTarget webTarget = client.target("https://graph.facebook.com/v3.3/oauth/access_token?"
                            + "client_id=" + APP_ID
                            + "&redirect_uri=" + REDIRECT_URL
                            + "&client_secret=" + APP_SECRET
                            + "&code=" + code);
                    final Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
                    final FacebookToken userToken = response.readEntity(FacebookToken.class);
                    response.close();
                    logger.info("Token: " + userToken.getAccessToken());
                    session.setAttribute("token", userToken);

                    final Client client2 = ClientBuilder.newClient();
                    final WebTarget webTarget2 = client2.target("https://graph.facebook.com/me"
                            + "?fields=id,name,email&"
                            + "access_token=" + userToken.getAccessToken());
                    final Response response2 = webTarget2.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
                    final FacebookUser facebookUser = response2.readEntity(FacebookUser.class);
                    response2.close();
                    logger.info("User: " + facebookUser.getName());
                    List<User> userList = userDao.findByFbUserId(facebookUser.getId());
                    if (userList.size() == 0) {
                        user = new User();
                        user.setEmail(facebookUser.getEmail());
                        user.setFbUserId(facebookUser.getId());
                        user.setName(facebookUser.getName());
                        userToken.setExpireDate(LocalDateTime.now()
                                .plusSeconds(Long.parseLong(userToken.getExpirationSeconds())));
                        user.setUserToken(userToken);
                        facebookTokenDao.save(userToken);
                        final Wallet wallet = new Wallet();
                        user.setWallet(wallet);
                        walletDao.save(wallet);
                        userDao.save(user);
                    } else {
                        user = userList.get(0);
                        if (!user.getEmail().equals(facebookUser.getEmail())) {
                            user.setEmail(facebookUser.getEmail());
                        }
                        if (!user.getName().equals(facebookUser.getName())) {
                            user.setName(facebookUser.getName());
                        }
                        if (!user.getUserToken().getAccessToken().equals(userToken.getAccessToken())) {
                            user.setUserToken(userToken);
                        }
                    }
                    session.setAttribute("user", user);
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
