/*
 * Copyright 2018 Jett Marks
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
 * Created by jett on 10/18/18.
 */
package com.clueride.auth;

import java.io.IOException;
import java.net.URL;

/**
 * How we connect to the Auth0 Identity Provider.
 *
 * Hides the details behind providing an access token
 * and receiving the matching response JSON from Auth0.
 *
 * Wraps the URL, HttpsConnection, and InputStream for ease of mocking.
 */
public interface Auth0Connection {

    /**
     * Perform the connection to the given URL for Auth0 along with
     * the access token matching the client whose identity we want.
     *
     * @param auth0Url One of the domains under which the user can authenticate.
     * @param accessToken opaque token provided by Auth0 to the client for
     *                    use in accessing resources.
     * @return int response code -- 200 or 401 generally.
     */
    int makeRequest(URL auth0Url, String accessToken) throws IOException;

    /**
     * Given an access token provided by the client, return the matching
     * JSON Response that holds the Client's identity.
     *
     * @return String containing the raw JSON response.
     */
    String getJsonResponse();

    /**
     * Provides the response code back from the request.
     * @return int response code -- 200 or 401 generally.
     */
    int getResponseCode();

    /**
     * Provides the response message back from the request.
     * @return String response message -- OK or Unauthorized.
     */
    String getResponseMessage();

}
