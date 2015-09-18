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

/**
 * Global spot for the NodeEvaluationStatus.
 * 
 * Single instance of this per instance of the application.
 *
 * @author jett
 *
 */
public class NetworkState {
    private static final Map<Integer, NodeEvaluationStatus> nodeEvaluationMap = new HashMap<>();
    private static Integer lastEvalId;

    public static NodeEvaluationStatus getNodeEvaluationInstance() {
        NodeEvaluationStatus nodeEvaluation = new NodeEvaluationStatus();
        nodeEvaluationMap.put(nodeEvaluation.getId(), nodeEvaluation);
        lastEvalId = nodeEvaluation.getId();
        return nodeEvaluation;
    }

    public static NodeEvaluationStatus getLastNodeEvaluation() {
        return nodeEvaluationMap.get(lastEvalId);
    }
}
