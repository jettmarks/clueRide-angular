package com.clueride.token;/*
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
 * Created by jett on 8/1/17.
 */

import java.util.ArrayList;
import java.util.List;

import javax.inject.Provider;

import com.google.inject.Inject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CoreGuiceModuleTest;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the JtiServiceImplTest class.
 */
@Guice(modules = CoreGuiceModuleTest.class)
public class JtiServiceImplTest {
    private JtiServiceImpl toTest;
    private String okId;
    private String badId;

    @Inject
    private Provider<JtiServiceImpl> toTestProvider;

    @BeforeMethod
    public void setUp() throws Exception {
        toTest = toTestProvider.get();
        okId = toTest.registerNewId();
        badId = "kqg9m503u7io4cs7m3ibjpf83f";
    }

    @Test
    public void testRegisterNewId() throws Exception {
        String actual = toTest.registerNewId();
        assertNotNull(actual);
    }

    @Test
    public void testValidateId_OK() throws Exception {
        toTest.validateId(okId);
    }

    @Test
    public void testMultipleRegistrations() throws Exception {
        List<String> ids = new ArrayList<>();
        for (int i=0; i<5; i++) {
            ids.add(toTest.registerNewId());
        }

        for (String id : ids) {
            toTest.validateId(id);
        }
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testValidateId_Bad() throws Exception {
        toTest.validateId(badId);
    }

    @Test
    public void testGenRandomString() throws Exception {
    }

}