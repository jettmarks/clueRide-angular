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
 * Created Sep 17, 2015
 */
package com.clueride.domain.dev;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * Global spot for the NodeEvaluationStatus.
 * 
 * Single instance of this per instance of the application.
 *
 * @author jett
 *
 */
public class NetworkState {
    private static final Logger LOGGER = Logger.getLogger(NetworkState.class);

    private static final Map<Integer, NodeEvaluationStatus> nodeEvaluationMap = new HashMap<>();
    private static Integer lastEvalId;

    public static NodeEvaluationStatus getNodeEvaluationInstance() {
        NodeEvaluationStatus nodeEvaluation = new NodeEvaluationStatus();
        nodeEvaluationMap.put(nodeEvaluation.getId(), nodeEvaluation);
        lastEvalId = nodeEvaluation.getId();
        dumpMap();
        return nodeEvaluation;
    }

    /**
     * Provides some feedback on what is happening with the NetworkProposals.
     */
    private static void dumpMap() {
        for (Entry<Integer, NodeEvaluationStatus> entry : nodeEvaluationMap
                .entrySet()) {
            LOGGER.info(entry.getKey() + ": " + entry.getValue());
        }
        LOGGER.info("LastEvalID: " + lastEvalId);
    }

    public static NodeEvaluationStatus getLastNodeEvaluation() {
        dumpMap();
        return nodeEvaluationMap.get(lastEvalId);
    }
}
