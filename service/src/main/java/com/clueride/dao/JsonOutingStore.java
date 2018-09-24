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
 * Created by jett on 7/5/16.
 */
package com.clueride.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import com.clueride.domain.outing.Outing;
import com.clueride.domain.outing.OutingStore;
import com.clueride.io.PojoJsonUtil;

/**
 * Implementation of OutingStore.
 */
public class JsonOutingStore implements OutingStore {
    private static List<Outing> outings = new ArrayList<>();
    private static Map<Integer,Outing> outingsPerId = new HashMap<>();

    @Inject
    public JsonOutingStore() {
        loadAll();
    }

    private void loadAll() {
        outings = PojoJsonUtil.loadOutings();
        reIndex();
    }

    private void reIndex() {
        for (Outing outing : outings) {
            outingsPerId.put(outing.getId(), outing);
        }
    }

    @Override
    public Integer addNew(Outing outing) throws IOException {
        return null;
    }

    @Override
    public Outing getOutingById(Integer outingId) {
        return outingsPerId.get(outingId);
    }

}
