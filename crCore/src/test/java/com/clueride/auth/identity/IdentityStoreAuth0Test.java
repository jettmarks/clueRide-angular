package com.clueride.auth.identity;/*
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
 * Created by jett on 10/16/18.
 */

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Provider;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CoreGuiceModuleTest;
import com.clueride.auth.Auth0Connection;
import com.clueride.config.ConfigService;
import com.clueride.exc.RecordNotFoundException;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the IdentityStoreAuth0 class.
 */
@Guice(modules = CoreGuiceModuleTest.class)
public class IdentityStoreAuth0Test {
    private IdentityStoreAuth0 toTest;

    @Inject
    private Provider<IdentityStoreAuth0> toTestProvider;

    @Inject
    private Auth0Connection auth0Connection;

    @Inject
    private ConfigService configService;

    private URL socialUrl;
    private URL passwordlessUrl;
    private String testAccessToken = "Access Token";

    @BeforeMethod
    public void setUp() throws Exception {
        reset(
                auth0Connection
        );

        toTest = toTestProvider.get();

        String socialIssuerUrlString = configService.get("clueride.auth.Social.url") + "userinfo";
        String passwordlessIssuerUrlString = configService.get("clueride.auth.Passwordless.url") + "userinfo";
        socialUrl = new URL(socialIssuerUrlString);
        passwordlessUrl = new URL(passwordlessIssuerUrlString);
    }

    @Test
    public void testGetIdentity_social() throws Exception {
        /* setup test */
        String rawJsonSocial = "{\"sub\":\"google-oauth2|109592720416091912525\",\"given_name\":\"Jett\",\"family_name\":\"Marks\",\"nickname\":\"jettmarks\",\"name\":\"Jett Marks\",\"picture\":\"https://lh6.googleusercontent.com/-7JrKyOcbBks/AAAAAAAAAAI/AAAAAAAAABo/GPHHoWYpK7k/photo.jpg\",\"gender\":\"male\",\"locale\":\"en\",\"updated_at\":\"2018-10-18T01:24:40.746Z\",\"email\":\"jettmarks@gmail.com\",\"email_verified\":true}";

        /* train mocks */
        when(auth0Connection.makeRequest(socialUrl, testAccessToken)).thenReturn(200);
        when(auth0Connection.makeRequest(passwordlessUrl, testAccessToken)).thenReturn(401);
        when(auth0Connection.getJsonResponse()).thenReturn(rawJsonSocial);

        /* make call */
        ClueRideIdentity actual = toTest.getIdentity(testAccessToken);

        /* verify results */
        assertNotNull(actual);
        System.out.println(actual);

    }

    @Test
    public void testGetIdentity_passwordless() throws Exception {
        /* setup test */
        String rawJsonPasswordless = "{\"sub\":\"email|5a5f7faf105d880aef7f07e8\",\"nickname\":\"bikeangel.atl\",\"name\":\"bikeangel.atl@gmail.com\",\"picture\":\"https://s.gravatar.com/avatar/bd5f7d2f03b304601e750f4f49937dd0?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fbi.png\",\"updated_at\":\"2018-10-20T14:13:21.483Z\",\"email\":\"bikeangel.atl@gmail.com\",\"email_verified\":true}";

        /* train mocks */
        when(auth0Connection.makeRequest(socialUrl, testAccessToken)).thenReturn(401);
        when(auth0Connection.makeRequest(passwordlessUrl, testAccessToken)).thenReturn(200);
        when(auth0Connection.getJsonResponse()).thenReturn(rawJsonPasswordless);

        /* make call */
        ClueRideIdentity actual = toTest.getIdentity(testAccessToken);

        /* verify results */
        assertNotNull(actual);
        System.out.println(actual);

    }

    @Test(expectedExceptions = RecordNotFoundException.class)
    public void testGetIdentity_recordNotFound() throws Exception {
        /* train mocks */
        when(auth0Connection.makeRequest(socialUrl, testAccessToken)).thenReturn(401);
        when(auth0Connection.makeRequest(passwordlessUrl, testAccessToken)).thenReturn(401);

        /* make call */
        toTest.getIdentity(testAccessToken);
    }

    @Test(expectedExceptions = RecordNotFoundException.class)
    public void testGetIdentity_parseProblem() throws Exception {
        /* train mocks */
        when(auth0Connection.makeRequest(socialUrl, testAccessToken)).thenReturn(401);
        when(auth0Connection.makeRequest(passwordlessUrl, testAccessToken)).thenReturn(200);
        when(auth0Connection.getJsonResponse()).thenReturn("malformed JSON");

        /* make call */
        toTest.getIdentity(testAccessToken);
    }

    @Test(expectedExceptions = RecordNotFoundException.class)
    public void testGetIdentity_IOProblem() throws Exception {
        /* train mocks */
        doThrow(new IOException()).when(auth0Connection).makeRequest(socialUrl, testAccessToken);
        doThrow(new IOException()).when(auth0Connection).makeRequest(passwordlessUrl, testAccessToken);

        /* make call */
        toTest.getIdentity(testAccessToken);
    }

}