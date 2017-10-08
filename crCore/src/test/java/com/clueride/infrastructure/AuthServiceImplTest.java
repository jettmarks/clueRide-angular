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
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

import com.google.inject.Inject;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CoreGuiceModuleTest;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the AuthServiceImplTest class.
 */
@Guice(modules = CoreGuiceModuleTest.class)
public class AuthServiceImplTest {
    private AuthService toTest;

    @Inject
    private Provider<AuthService> authServiceProvider;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
        toTest = authServiceProvider.get();
    }

    @Test
    public void testInjection() {
        assertNotNull(toTest);
    }

    /**
     * Checks that newly constructed instances aren't available for use until the chain is added.
     * @throws Exception
     */
    @Test(expectedExceptions = IllegalStateException.class)
    public void testIsValid() throws Exception {
        toTest.isValid();
    }

    @Test
    public void testAddChain() throws Exception {
        toTest.withChain(filterChain);
        toTest.withResponse(response);
        toTest.isValid();
    }

    @Test
    public void testIsOptionsRequest_isNotOptionRequest() throws Exception {
        /* setup */
        toTest.withChain(filterChain);
        toTest.withResponse(response);

        /* train mocks */
        when(request.getMethod()).thenReturn(HttpMethod.POST);

        /* make call */
        boolean actual = toTest.isOptionsRequest(request);

        /* verify results */
        assertFalse(actual);
        verify(filterChain, times(0)).doFilter(request, response);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testIsOptionsRequest_illegalState() throws Exception {
        toTest.isOptionsRequest(request);
    }

}