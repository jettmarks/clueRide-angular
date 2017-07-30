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
 * Created by jett on 7/30/17.
 */
package com.clueride.infrastructure;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Injectable portion of the CluerideSessionFilter.
 * The Servlet instantiation currently depends on a Filter newed up by the webapp container.  Moving much of the logic
 * out to this service allows easier testing.
 */
public interface AuthService {
    /**
     * The FilterChain is involved with most operations; make sure we've got one.
     * It would be nice to inject this so the constructor assures we've always got what we need, but I don't think
     * the timing will work.
     * @param filterChain used to make sure each filter gets a turn at the request and response.
     */
    void withChain(FilterChain filterChain);

    void withResponse(HttpServletResponse response);

    /**
     * Upon credentials having been confirmed, add the token to the Response Headers.
     */
    void addToken();

    /**
     * Checks to see if we continue processing the chain or defer to the rest of the chain.
     * @return true if no further processing is possible (or required).
     */
    boolean isOptionsRequest(HttpServletRequest request) throws IOException, ServletException;

    /**
     * Checks existing state and throws exception if there are problems.
     */
    void isValid();
}
