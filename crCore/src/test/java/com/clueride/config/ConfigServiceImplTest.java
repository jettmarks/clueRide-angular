package com.clueride.config;/*
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
 * Created by jett on 10/22/18.
 */

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.CoreGuiceModuleTest;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the ConfigServiceImpl class.
 */
@Guice(modules= CoreGuiceModuleTest.class)
public class ConfigServiceImplTest {
    private ConfigService toTest;

    @Inject
    private Provider<ConfigService> toTestProvider;

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
        toTest = toTestProvider.get();
    }

    @Test
    public void testGet() throws Exception {
    }

    @Test
    public void testGetAuthIssuers() throws Exception {
        List<String> issuerTypes = toTest.getAuthIssuerTypes();

        for (String issuerType : issuerTypes) {
            String configValue = toTest.get("clueride.auth." + issuerType + ".url");
            System.out.println(configValue);
            assertNotNull(configValue);
        }
    }

    @Test
    public void testGetAuthSecret() throws Exception {
    }

}