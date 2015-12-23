/**
 *   Copyright 2015 Jett Marks
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created Oct 28, 2015
 */
package com.clueride.dao;

import com.clueride.domain.dev.NetworkProposal;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Maintains the NetworkProposals across a session.
 * 
 * Memory-based at this time, but could be persisted.
 * 
 * Based on the NetworkState and NodeEvaluationStatus which was the pre-cursor
 * to this guy.
 *
 * @author jett
 */
public class NetworkProposalStore {
    private static final Logger LOGGER = Logger
            .getLogger(NetworkProposalStore.class);

    /** Holds the instances across the session. */
    private static final Map<Integer, NetworkProposal> map = new HashMap<>();
    /** Identifies the last proposal to be put into the map. */
    private static Integer lastId = -1;

    public static NetworkProposal getLastProposal() {
        return map.get(lastId);
    }

    public static void add(NetworkProposal networkProposal) {
        synchronized (lastId) {
            lastId = networkProposal.getId();
            map.put(lastId, networkProposal);
        }
        LOGGER.info("Added proposal " + networkProposal);
    }
}
