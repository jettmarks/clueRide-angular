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

import java.util.List;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Interim implementation until I can get the TypeSafe config impl going.
 */
public class ConfigServiceImpl implements ConfigService {
    private static Config config = ConfigFactory.load();

    @Override
    public String get(String key) {
        return config.getString(key);
    }

    @Override
    public List<String> getAuthIssuers() {
        return config.getStringList("clueride.jwt.issuers");
    }

    @Override
    public String getAuthSecret() {
        return config.getString("clueride.jwt.secret");
    }

}
