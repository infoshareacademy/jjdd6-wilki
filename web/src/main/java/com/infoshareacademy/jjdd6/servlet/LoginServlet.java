package com.infoshareacademy.jjdd6.servlet;

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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    //    private final String STATE = String.valueOf(Math.random() * 1000 * Math.random() * 1000);
    private final String STATE = "FMuJDKrajzZ2sTcJZ0bV";
    private final String APP_ID = "2337908682898870";
    private final String APP_SECRET = "918fce13c991ddf3477eeb04bf4c5a4f";

    private static Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    @Inject
    UserDao userDao;

    @Inject
    WalletDao walletDao;

    @Inject
    FacebookTokenDao facebookTokenDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String reqPath = (String) session.getAttribute("reqPath");
        String REDIRECT_URL = "http://localhost:8080/" + reqPath;

        if (session.getAttribute("user") != null) {
            Long userId = (Long) session.getAttribute("user");
            User user = userDao.findById(userId);

            if (user.getUserToken() != null && user.getUserToken().getExpireDate() != null && user.getUserToken().getExpireDate().isBefore(LocalDateTime.now())) {
                logger.info("Token expired on " + user.getUserToken().getExpireDate() + " for user " + user.getName() + "");
                session.invalidate();
            }
        }

        if (session.getAttribute("user") == null || session.getAttribute("user").toString().isEmpty()) {
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
                        User user = new User();
                        user.setEmail(facebookUser.getEmail());
                        user.setFbUserId(facebookUser.getId());
                        user.setName(facebookUser.getName());
                        userToken.setExpireDate(LocalDateTime.now()
                                .plusSeconds(userToken.getExpirationSeconds()));
                        user.setUserToken(userToken);
                        facebookTokenDao.save(userToken);
                        final Wallet wallet = new Wallet();
                        user.setWallet(wallet);
                        walletDao.save(wallet);
                        userDao.save(user);
                    } else {
                        User user = userList.get(0);
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
                }
            }
        }
        String forward = (String) session.getAttribute("reqPath");
        session.removeAttribute("reqPath");
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(forward);
        requestDispatcher.forward(req, resp);
    }
}