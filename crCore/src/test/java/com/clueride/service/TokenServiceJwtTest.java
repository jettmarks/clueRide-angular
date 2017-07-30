package com.clueride.service;/*
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
 * Created by jett on 7/25/17.
 */

import javax.inject.Inject;
import javax.inject.Provider;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CoreGuiceModuleTest;
import static org.testng.Assert.assertEquals;

/**
 * Exercises the TokenServiceJwtTest class.
 */
@Guice(modules=CoreGuiceModuleTest.class)
public class TokenServiceJwtTest {
    private TokenService toTest;
    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjbHVlcmlkZS5jb20ifQ.i_K5QQC1lg2yeB-30wrMbm3dDIb7Az_WyCTxbg5wzr8";

    @Inject
    private Provider<TokenService> toTestProvider;

    @BeforeMethod
    public void setUp() throws Exception {
        toTest = toTestProvider.get();
    }

    @Test
    public void testGenerateSignedToken() throws Exception {
        String expected = token;
        String actual = toTest.generateSignedToken();
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testVerifyToken_null() throws Exception {
        toTest.verifyToken(null);
    }

    @Test
    public void testVerifyToken() throws Exception {
        toTest.verifyToken(token);
    }

}