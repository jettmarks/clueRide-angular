/*
 * Copyright 2016 Jett Marks
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
 * Created by jett on 6/26/16.
 */
package com.clueride.service;

import java.util.concurrent.atomic.AtomicInteger;

import com.clueride.domain.account.member.Member;

/**
 * Memory-based approach for assigning unique IDs for {@link Member}.
 */
public class MemoryBasedMemberIdProvider implements IdProvider {
    private static AtomicInteger lastId = new AtomicInteger(1);

    @Override
    public Integer getId() {
        return lastId.addAndGet(1);
    }

    @Override
    public void registerId(Integer id) {
        if (id > lastId.get()) {
            lastId.set(id);
        }
    }

    @Override
    public int getLastId() {
        return lastId.get();
    }
}
