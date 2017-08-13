package com.clueride.infrastructure;/*
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

import javax.inject.Provider;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CoreGuiceModuleTest;
import com.clueride.token.TokenService;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

/**
 * Exercises the AuthenticationFilterTest class.
 */
@Guice(modules = CoreGuiceModuleTest.class)
public class AuthenticationFilterTest {
    private AuthenticationFilter toTest;
    private static String ORIG_GUEST_TOKEN = "GuestToken";
    private static String BAD_GUEST_TOKEN = "SomeoneElseGuestToken";
    private static String NEW_GUEST_TOKEN = "NewGuestToken";

    @Inject
    private Provider<AuthenticationFilter> toTestProvider;

    @Mock
    private ContainerRequestContext requestContext;

    @Inject
    private TokenService tokenService;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        reset(
                tokenService
        );
        toTest = toTestProvider.get();
    }

    @Test
    public void testFilter_OK() throws Exception {
        /* train mocks */
        when(requestContext.getMethod()).thenReturn(HttpMethod.POST);
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION))
                .thenReturn("Bearer " + NEW_GUEST_TOKEN);

        // Not expected for this scenario
        ArgumentCaptor<Response> captor = ArgumentCaptor.forClass(Response.class);
        doNothing().when(requestContext).abortWith(captor.capture());

        /* make call */
        toTest.filter(requestContext);

        /* verify results */
        verify(requestContext, times(0)).abortWith(any(Response.class));
    }

    // TODO: https://youtrack.clueride.com/issue/CA-298 - get Guest working again.
//    @Test
    public void testFilter_guest() throws Exception {
        /* train mocks */
        when(requestContext.getMethod()).thenReturn(HttpMethod.POST);
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION))
                .thenReturn("Bearer " + ORIG_GUEST_TOKEN);
        when(tokenService.generateSignedToken()).thenReturn(NEW_GUEST_TOKEN);
        ArgumentCaptor<Response> captor = ArgumentCaptor.forClass(Response.class);
        doNothing().when(requestContext).abortWith(captor.capture());

        /* make call */
        toTest.filter(requestContext);

        /* verify results */
        Response capturedResponse = captor.getValue();
        assertEquals(capturedResponse.getHeaderString(HttpHeaders.AUTHORIZATION), "Bearer " + NEW_GUEST_TOKEN);
        verify(requestContext).abortWith(any(Response.class));
        verify(tokenService, times(0)).validateToken(any(String.class));
    }

    @Test
    public void testFilter_malformedToken() throws Exception {
        /* train mocks */
        when(requestContext.getMethod()).thenReturn(HttpMethod.POST);
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION))
                .thenReturn("missing required text" + ORIG_GUEST_TOKEN);
        when(tokenService.generateSignedToken()).thenReturn(NEW_GUEST_TOKEN);
        ArgumentCaptor<Response> captor = ArgumentCaptor.forClass(Response.class);
        doNothing().when(requestContext).abortWith(captor.capture());

        /* make call */
        toTest.filter(requestContext);

        /* verify results */
        Response capturedResponse = captor.getValue();
        assertEquals(capturedResponse.getStatusInfo(), Response.Status.UNAUTHORIZED);
        verify(requestContext).abortWith(any(Response.class));

    }

    @Test
    public void testFilter_invalidToken() throws Exception {
        /* train mocks */
        when(requestContext.getMethod()).thenReturn(HttpMethod.POST);
        when(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION))
                .thenReturn("Bearer " + BAD_GUEST_TOKEN);
        when(tokenService.generateSignedToken()).thenReturn(NEW_GUEST_TOKEN);
        doThrow(new RuntimeException()).when(tokenService).validateToken(BAD_GUEST_TOKEN);
        ArgumentCaptor<Response> captor = ArgumentCaptor.forClass(Response.class);
        doNothing().when(requestContext).abortWith(captor.capture());

        /* make call */
        toTest.filter(requestContext);

        /* verify results */
        Response capturedResponse = captor.getValue();
        assertEquals(capturedResponse.getStatusInfo(), Response.Status.UNAUTHORIZED);
        verify(requestContext).abortWith(any(Response.class));
    }

}