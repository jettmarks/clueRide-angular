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
 * Created by jett on 3/5/16.
 */
package com.clueride.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import org.apache.log4j.Logger;

import com.clueride.domain.Outing;

/**
 * Implementation of the Outing Service.
 */
public class OutingServiceImpl implements OutingService {
    private static final Logger LOGGER = Logger.getLogger(OutingServiceImpl.class);

    private static List<Outing> outings;
    private static Map<Integer,Outing> outingPerId;
    private static Map<Integer,Outing> outingPerTeam;
    private static Map<Integer,Long> lastModifiedStatePerOuting;
    private static Map<Integer,Long> lastModifiedPositionPerOuting;

    @Inject
    public OutingServiceImpl() {
        if (outings == null || outings.isEmpty()) {
            loadOutings();
        }
        reIndex();
    }

    private void loadOutings() {
        outings = new ArrayList<>();
        outingPerId = new HashMap<>();
        outingPerTeam = new HashMap<>();
        lastModifiedStatePerOuting = new HashMap<>();
        lastModifiedPositionPerOuting = new HashMap<>();
    }

    private void reIndex() {
        outingPerId.clear();
        outingPerTeam.clear();

        for (Outing outing : outings) {
            Integer outingId = outing.getId();
            outingPerTeam.put(outing.getTeamId(), outing );
            outingPerId.put(outingId, outing);

            /* Initialize timestamps to 0 if not present already. */
            if (!lastModifiedPositionPerOuting.containsKey(outingId)) {
                lastModifiedPositionPerOuting.put(outingId, 0L);
            }
            if (!lastModifiedStatePerOuting.containsKey(outingId)) {
                lastModifiedStatePerOuting.put(outingId, 0L);
            }
        }
    }

    @Override
    public Outing getByOutingId(Integer outingId) {
        LOGGER.info("Retrieving Outing ID " + outingId);
        return outingPerId.get(outingId);
    }

    @Override
    public Outing getByTeamId(Integer teamId) {
        LOGGER.info("Retrieving Outing for Team ID " + teamId);
        return outingPerTeam.get(teamId);
    }

    @Override
    public Outing createOuting(Outing outing) {
        LOGGER.info("Creating new Outing with ID " + outing.getId());
        outings.add(outing);
        reIndex();
        return outing;
    }

    @Override
    public Integer updateOuting(Outing outing) {
        reIndex();
        return outing.getId();
    }

    @Override
    public Long lastStateChange(Integer outingId) {
        return lastModifiedStatePerOuting.get(outingId);
    }

    @Override
    public Long lastPositionChange(Integer outingId) {
        return lastModifiedPositionPerOuting.get(outingId);
    }

    @Override
    public void updateState(Integer outingId) {
        lastModifiedStatePerOuting.put(outingId, (new Date()).getTime());
    }

    @Override
    public void updatePosition(Integer outingId) {
        lastModifiedPositionPerOuting.put(outingId, (new Date()).getTime());
    }
}
