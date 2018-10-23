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
 * Created by jett on 8/1/17.
 */
package com.clueride.config;

import java.util.ArrayList;
import java.util.List;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * TypeSafe-backed implementation.
 *
 * Values can be overridden by defining JVM args (-D flag).
 */
public class ConfigServiceImpl implements ConfigService {
    private static Config config = ConfigFactory.load();

    @Override
    public String get(String key) {
        return config.getString(key);
    }

    @Override
    public List<String> getAuthIssuerTypes() {
        return config.getStringList("clueride.auth.issuerTypes");
    }

    @Override
    public List<String> getAuthIssuers() {
        List<String> authIssuers = new ArrayList<>();
        List<String> issuerTypes = getAuthIssuerTypes();

        for (String issuerType : issuerTypes) {
            authIssuers.add(get("clueride.auth." + issuerType + ".url"));
        }
        return authIssuers;
    }

    @Override
    public String getAuthSecret() {
        return config.getString("clueride.auth.secret");
    }

    @Override
    public String getTestToken() {
        return get("clueride.test.token");
    }

    @Override
    public String getTestAccount() {
        return get("clueride.test.account");
    }

}
