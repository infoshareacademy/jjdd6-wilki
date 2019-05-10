package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.service.UserService;
import com.infoshareacademy.jjdd6.view.FacebookTokenParse;
import com.infoshareacademy.jjdd6.wilki.FacebookToken;
import com.infoshareacademy.jjdd6.wilki.FacebookUser;
import com.infoshareacademy.jjdd6.wilki.User;
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
import java.util.List;

@Transactional
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final String STATE = "FMuJDKrajzZ2sTcJZ0bV";
    private final String APP_ID = "2337908682898870";
    private final String APP_SECRET = "918fce13c991ddf3477eeb04bf4c5a4f";

    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    @Inject
    UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String REDIRECT_URL = req.getRequestURL().toString();
        logger.info(REDIRECT_URL);
        userService.checkIfTokenExpired(session);

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
                if (code != null && !code.isEmpty()) {
                    FacebookTokenParse facebookTokenParse = getFacebookTokenParse(REDIRECT_URL, code);
                    FacebookToken userToken = new FacebookToken(facebookTokenParse.getAccess_token(), facebookTokenParse.getToken_type(), facebookTokenParse.getExpires_in());
                    FacebookUser facebookUser = getFacebookUser(userToken);
                    List<User> userList = userService.findByFbUserId(facebookUser);
                    userService.setupUser(session, userToken, facebookUser, userList);
                    forwardToRequestedView(req, resp, session);
                }
            }
        }

    }

    private FacebookUser getFacebookUser(FacebookToken userToken) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("https://graph.facebook.com/me"
                + "?fields=id,name,email&"
                + "access_token=" + userToken.getAccessToken());
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        FacebookUser facebookUser = response.readEntity(FacebookUser.class);
        response.close();
        return facebookUser;
    }

    private FacebookTokenParse getFacebookTokenParse(String REDIRECT_URL, String code) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("https://graph.facebook.com/v3.3/oauth/access_token?"
                + "client_id=" + APP_ID
                + "&redirect_uri=" + REDIRECT_URL
                + "&client_secret=" + APP_SECRET
                + "&code=" + code);
        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        FacebookTokenParse facebookTokenParse = response.readEntity(FacebookTokenParse.class);
        response.close();
        return facebookTokenParse;
    }

    private void forwardToRequestedView(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws ServletException, IOException {
        String forward = (String) session.getAttribute("reqPath");
        session.removeAttribute("reqPath");
        resp.sendRedirect(forward);
    }
}