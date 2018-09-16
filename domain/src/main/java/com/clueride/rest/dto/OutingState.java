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
 * Created by jett on 3/6/16.
 */
package com.clueride.rest.dto;

/**
 * DTO for the Outing State; shares the ID of the corresponding Outing.
 * @deprecated - No longer need to support the "DTO" instances.
 */
public class OutingState {
    public Integer outingId;
    /* True if we are ready to begin play. */
    public Boolean teamConfirmed;
    /* Index into the Outing for the current index into the Path list. */
    public Integer pathIndex;
    /* Less easy to follow; should deprecate this. */
    public Boolean mostRecentClueSolvedFlag;
    /* True if we have departed and are approaching the next location. */
    public Boolean rolling;
}
