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

/**
 * Session Tracking filter.
 */
public class CluerideSessionFilter implements Filter {
    private ServletContext servletContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
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
        this.servletContext.log("Requested Resource::"+uri);

        HttpSession session = req.getSession(false);

//        if(session == null && !(uri.endsWith("html") || uri.endsWith("LoginServlet"))){
        if (session == null && !(uri.contains("/login.html")
                || uri.endsWith("/rest/login")
                || uri.contains("loginBubbles.html")
                || uri.contains("bubble.html")
                || uri.contains("joinTeam.html")
                || uri.contains("favicon.ico")
                || uri.endsWith(".css")
                || uri.endsWith(".png")
                || uri.endsWith(".js")
                || uri.contains("fontawesome")
                || uri.endsWith("js.map")
        )) {
            this.servletContext.log("Unauthorized access request");
            res.sendRedirect("/login.html");
        } else {
            // pass the request along the filter chain
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
