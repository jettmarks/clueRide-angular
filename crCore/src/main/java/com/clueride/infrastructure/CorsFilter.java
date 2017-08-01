/*
 * Copyright 2017 Jett Marks
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
 * Created by jett on 7/27/17.
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

/**
 * Implements CORS request handling.
 *
 * There may be more work to get this to implement the full set of responses expected of servers making use
 * of this filter.  See this page for more detail:
 * https://stackoverflow.com/questions/26777083/best-practice-for-rest-token-based-authentication-with-jax-rs-and-jersey
 */
public class CorsFilter implements Filter {
    private ServletContext servletContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
       this.servletContext = filterConfig.getServletContext();
       this.servletContext.log("CORS Filter initialized");
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        /* TODO: Make this configurable; currently the Ionic Server in dev mode. */
        response.setHeader("Access-Control-Allow-Origin",
                "http://localhost:8100");
        response.setHeader(
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

        response.setHeader(
                /* Custom header for Clue Ride. */
                "Access-Control-Expose-Headers",
                "Authorization"
        );

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
