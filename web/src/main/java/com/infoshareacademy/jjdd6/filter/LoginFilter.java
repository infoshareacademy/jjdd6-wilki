package com.infoshareacademy.jjdd6.filter;

import com.infoshareacademy.jjdd6.dao.UserDao;
import com.infoshareacademy.jjdd6.wilki.FacebookToken;
import com.infoshareacademy.jjdd6.wilki.FacebookUser;
import com.infoshareacademy.jjdd6.wilki.User;
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

@WebFilter(
        filterName = "LoginFilter",
        urlPatterns = {"/login"})
public class LoginFilter implements Filter {

    private final String STATE = String.valueOf(Math.random() * 1000 * Math.random() * 1000);
    private final String APP_ID = "2337908682898870";
    private final String REDIRECT_URL = "http://localhost:8080/wallet";
    private final String APP_SECRET = "918fce13c991ddf3477eeb04bf4c5a4f";
    private static Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Inject
    UserDao userDao;

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

        if (session.getAttribute("token") == null) {
            String error = req.getParameter("error");

            if (error != "" && error != null) {
                logger.info("Unauthorized login attempt");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

            if (session.getAttribute("state") == null) {
                session.setAttribute("state", STATE);
            }

            if (req.getParameter("state") != STATE || req.getParameter("state") == null) {

                resp.sendRedirect("https://www.facebook.com/v3.3/dialog/oauth?client_id=" + APP_ID
                        + "&redirect_uri=" + REDIRECT_URL
                        + "&state=" + STATE);
            } else {
                String code = req.getParameter("code");
                if (code != "" && code != null) {
                    session.setAttribute("code", code);
                    final Client client = ClientBuilder.newClient();
                    final WebTarget webTarget = client.target("https://graph.facebook.com/v3.3/oauth/access_token?"
                            + "client_id=" + APP_ID
                            + "&redirect_uri=" + REDIRECT_URL
                            + "&client_secret=" + APP_SECRET
                            + "&code=" + code);
                    final Response response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
                    final FacebookToken userToken = response.readEntity(FacebookToken.class);
                    response.close();
                    session.setAttribute("token", userToken);

                    final Client client2 = ClientBuilder.newClient();
                    final WebTarget webTarget2 = client.target("https://graph.facebook.com/me"
                            + "?fields=id,name,email&"
                            + "access_token=" + userToken.getAccessToken());
                    final Response response2 = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
                    final FacebookUser facebookUser = response.readEntity(FacebookUser.class);
                    User user = userDao.findByFbUserId(facebookUser.getId());
                    if (user == null) {
                        user.setEmail(facebookUser.getEmail());
                        user.setFbUserId(facebookUser.getId());
                        user.setName(facebookUser.getName());
                        user.setUserToken(userToken);
                        userDao.save(user);
                        session.setAttribute("user", user.getId());
                    } else {

                    }


                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
