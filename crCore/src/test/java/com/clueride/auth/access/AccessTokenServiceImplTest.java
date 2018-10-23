package com.clueride.auth.access;/*
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
 * Created by jett on 10/20/18.
 */

import javax.inject.Inject;
import javax.inject.Provider;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CoreGuiceModuleTest;
import com.clueride.auth.identity.ClueRideIdentity;
import com.clueride.auth.identity.IdentityStore;
import com.clueride.domain.account.member.Member;
import com.clueride.exc.RecordNotFoundException;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the AccessTokenServiceImplTest class.
 */
@Guice(modules = CoreGuiceModuleTest.class)
public class AccessTokenServiceImplTest {
    private AccessTokenServiceImpl toTest;

    @Inject
    private Provider<AccessTokenServiceImpl> toTestProvider;

    @Inject
    private AccessToken accessToken;

    @Inject
    private ClueRideIdentity clueRideIdentity;

    @Inject
    private IdentityStore identityStore;

    @Inject
    private Member member;

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
        toTest = toTestProvider.get();
        assertNotNull(toTest);

        toTest.emptyCache();
    }

    /** Main purpose is to make sure cache is doing what we expect. */
    @Test
    public void testGetIdentity() throws Exception {
        /* setup test */

        /* train mocks */
        when(identityStore.getIdentity(accessToken.getToken())).thenReturn(clueRideIdentity);

        /* make call -- multiple times */
        ClueRideIdentity actual;
        toTest.getIdentity(accessToken);
        toTest.getIdentity(accessToken);
        actual = toTest.getIdentity(accessToken);

        /* verify results */
        assertNotNull(actual);
        verify(identityStore, times(1)).getIdentity(accessToken.getToken());

    }

    /** Main purpose is to make sure Principal is retrieved. */
    @Test
    public void testGetPrincipalString() throws Exception {
        /* setup test */
        String expectedEmail = member.getEmailAddress();

        /* train mocks */
        when(identityStore.getIdentity(accessToken.getToken())).thenReturn(clueRideIdentity);

        /* make call */
        String actual = toTest.getPrincipalString(accessToken.getToken());

        /* verify results */
        assertEquals(actual, expectedEmail);

    }

    /** Tests the case when the access token isn't in the store. */
    @Test(expectedExceptions = RecordNotFoundException.class)
    public void testGetPrincipalString_recordNotFound() {
        /* setup test */

        /* train mocks */
        doThrow(new RecordNotFoundException()).when(identityStore).getIdentity(accessToken.getToken());

        /* make call */
        toTest.getPrincipalString(accessToken.getToken());

        /* verify results */
    }


}