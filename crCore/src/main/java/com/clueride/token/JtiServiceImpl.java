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
package com.clueride.token;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.apache.log4j.Logger;

/**
 * Simple implementation of JtiService.
 */
public class JtiServiceImpl implements JtiService {
    private static final Logger LOGGER = Logger.getLogger(JtiServiceImpl.class);
    private static List<String> validIds;

    @Inject
    public JtiServiceImpl() {
        if (validIds == null) {
            validIds = new ArrayList<>();
        }
    }

    @Override
    public String registerNewId() {
        String jtiId = genRandomString();
        validIds.add(jtiId);
        System.out.printf("Adding %s as ID #%d\n", jtiId, validIds.size());
        return jtiId;
    }

    public void blacklistId(String jtiId) {

    }

    @Override
    public void retireId(String jtiId) {
        validIds.remove(jtiId);
    }

    @Override
    public void validateId(String jtiId) {
        if (!validIds.contains(jtiId)) {
            LOGGER.warn("JTI ID not found: " +  jtiId);
            throw new RuntimeException("Not found");
        }
    }

    String genRandomString() {
        Random random = new SecureRandom();
        return (new BigInteger(130, random).toString(32));
    }
}
