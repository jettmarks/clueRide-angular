package com.clueride.aop.badge;
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
 * Created by jett on 11/18/17.
 */

import java.security.Principal;

import javax.inject.Inject;
import javax.inject.Provider;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.aop.AopServiceConsumerImpl;
import com.clueride.domain.DomainGuiceModuleTest;
import com.clueride.domain.account.principal.SessionPrincipal;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the BadgeCaptureInterceptorTest class.
 */
@Guice(modules = DomainGuiceModuleTest.class)
public class BadgeCaptureInterceptorTest {
    private AopServiceConsumerImpl toTest;

    @Inject
    private SessionPrincipal sessionPrincipal;

    @Inject
    private Principal principal;

    @Inject
    private Provider<AopServiceConsumerImpl> toTestProvider;

    @BeforeMethod
    public void setUp() throws Exception {
        reset(
                sessionPrincipal
        );
        toTest = toTestProvider.get();
        assertNotNull(toTest);
    }

    @Test
    public void testInjection() throws Exception {
        /* setup test */
        Integer expected = 123;

        /* train mocks */
        when(sessionPrincipal.getSessionPrincipal()).thenReturn(principal);

        /* make call */
        Integer actual = toTest.performService(expected);

        /* verify results */
        assertEquals(actual, expected);
        verify(sessionPrincipal).getSessionPrincipal();
    }

//    @Test
    public void testAccessToPrincipal() throws Exception {
        /* setup test */
        Integer expected = 123;

        /* train mocks */

        /* make call */
        Integer actual = toTest.performService(expected);

        /* verify results */
        assertEquals(actual, expected);

    }

}