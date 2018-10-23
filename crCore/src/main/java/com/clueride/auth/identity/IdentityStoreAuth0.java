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
 * Created by jett on 10/13/18.
 */
package com.clueride.auth.identity;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

import javax.inject.Inject;
import javax.mail.internet.AddressException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;

import com.clueride.auth.Auth0Connection;
import com.clueride.config.ConfigService;
import com.clueride.exc.RecordNotFoundException;

/**
 * Implementation of Identity Store that is backed by Auth0 as the identity provider.
 */
public class IdentityStoreAuth0 implements IdentityStore {
    private ObjectMapper objectMapper = new ObjectMapper();
    private final Auth0Connection auth0Connection;
    private final ConfigService configService;

    @Inject
    public IdentityStoreAuth0(
            Auth0Connection auth0Connection,
            ConfigService configService
    ) {
        this.auth0Connection = auth0Connection;
        this.configService = configService;
    }

    @Override
    public ClueRideIdentity getIdentity(String accessToken) {
        List<String> issuers = configService.getAuthIssuers();

        /* Walk through the configured list of Issuers. */
        for (String issuer : issuers) {
            int responseCode = HttpStatus.SC_UNAUTHORIZED;

            try {
                /* Build URL for userinfo endpoint*/
                URL identityProviderUrl = new URL(issuer +"userinfo");

                /* Make Request and look at the response code. */
                responseCode = auth0Connection.makeRequest(
                        identityProviderUrl,
                        accessToken
                );
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (HttpStatus.SC_OK == responseCode) {
                String jsonResponse = auth0Connection.getJsonResponse();
                try {
                    /* Parse response from Identity Provider. */
                    return objectMapper.readValue(jsonResponse, ClueRideIdentity.Builder.class).build();
                } catch (IOException | ParseException | AddressException e) {
                    e.printStackTrace();
                }
            }
        }

        /* No issuer was able to verify the Access Token. */
        throw new RecordNotFoundException(accessToken);
    }

}
