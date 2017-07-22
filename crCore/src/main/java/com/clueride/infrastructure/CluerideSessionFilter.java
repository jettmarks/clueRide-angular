/*
 * Copyright 2016 Jett Marks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by jett on 3/6/16.
 */
package com.clueride.infrastructure;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.clueride.domain.InvitationFull;
import com.clueride.domain.user.Badge;
import com.clueride.service.AuthenticationService;
import com.clueride.service.InvitationService;

/**
 * Session Tracking filter.
 */
public class CluerideSessionFilter implements Filter {
    private ServletContext servletContext;

    private AuthenticationService authenticationService;
    private InvitationService invitationService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO: CA-269 Move this to dependency injection
        authenticationService = CoreGuiceSetup.getGuiceInjector(null).getInstance(AuthenticationService.class);
        invitationService = CoreGuiceSetup.getGuiceInjector(null).getInstance(InvitationService.class);

        this.servletContext = filterConfig.getServletContext();
        this.servletContext.log("Clueride Session Filter initialized");
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        HttpSession session = req.getSession(false);

        /* CORS - May want separate filters for this. */
        if (req.getMethod().equals("OPTIONS")) {
            /* TODO: Make this configurable; currently the Ionic Server in dev mode. */
            res.setHeader("Access-Control-Allow-Origin",
                    "http://localhost:8100");
            res.setHeader(
                    "Access-Control-Allow-Headers",
                    new StringBuilder()
                            .append("Access-Control-Allow-Headers,")
                            .append("Origin,")
                            .append("Accept,")
                            .append("X-Requested-With,")
                            .append("Authorization,")
                            .append("Content-Type,")
                            .append("Access-Control-Request-Method,")
                            .append("Access-Control-Request-Headers,")
                            .toString()
            );
            chain.doFilter(request, response);
            return;
        }

        /* Providing the Authorization token is a get into jail free card. */
        if (((HttpServletRequest) request).getHeader("Authorization") != null) {
            chain.doFilter(request, response);
            return;
        }

        if (session == null) {
            /* No session; are we presenting the login page? */
            if (isLoginPageUri(uri)) {
                chain.doFilter(request, response);
                return;
            }
            /* Check to see if this is an invitation with a valid token. */
            if (isValidInvitation(req)) {
                this.servletContext.log("Found Invitation");
                /* Lookup details of invitation to place in the session. */
                InvitationFull invitation = invitationService.getInvitationByToken(req.getParameter("inviteToken"));
                List<Badge> badges = authenticationService.loginReturningBadges(invitation);
                authenticationService.establishSession(badges, req);
                chain.doFilter(request, response);
                return;
            }
            this.servletContext.log("Requested Resource::"+uri);
            this.servletContext.log("Unauthorized access request");
            this.servletContext.log("Host: "+req.getRequestURL());
            if (isRestCall(uri)) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                res.sendRedirect(req.getContextPath() + "/login.html");
            }
        } else {
            /* We have a session;  pass the request along the filter chain */
            chain.doFilter(request, response);
        }
    }

    private boolean isValidInvitation(HttpServletRequest req) {
        boolean result = false;
        String invitationToken = req.getParameter("inviteToken");
        if (invitationToken != null) {
            this.servletContext.log("Invitation Token Found: " + invitationToken);
            result = true;
        }
        return result;
    }

    /**
     *  This method is intended to return true for any of the URLs that
     * are associated with logging in -- they get a free pass because they
     * are in the business of establishing a session.
     *
     * TODO: Need a better method because this constantly breaks.
     */
    boolean isLoginPageUri(String uri) {
        return uri.contains("login.html")
                || uri.endsWith("/rest/login")
                || uri.endsWith("/rest/login/oauth")
                || uri.contains("loginBubbles.html")
                || uri.endsWith("credentials.html")
                || uri.contains("bubble.html")
                || uri.contains("joinTeam.html")
                || uri.contains("favicon.ico")
                || uri.endsWith(".css")
                || uri.endsWith(".png")
                || uri.endsWith(".js")
                || uri.contains("fontawesome")
                || uri.endsWith("js.map");
    }

    boolean isRestCall(String uri) {
        return uri.contains("/rest/");
    }

    @Override
    public void destroy() {

    }
}
