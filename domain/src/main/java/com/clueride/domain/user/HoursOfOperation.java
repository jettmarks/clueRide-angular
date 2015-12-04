package com.clueride.domain.user;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2015 Jett Marks
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Created by jett on 11/23/15.
 */
public class HoursOfOperation {
    private Map<Integer,Time> openTimes = new HashMap<>();
    private Map<Integer,Time> closeTimes = new HashMap<>();

    public Map<Integer, Time> getOpenTimes() {
        return openTimes;
    }

    public void setOpenTimes(Map<Integer, Time> openTimes) {
        this.openTimes = openTimes;
    }

    public Map<Integer, Time> getCloseTimes() {
        return closeTimes;
    }

    public void setCloseTimes(Map<Integer, Time> closeTimes) {
        this.closeTimes = closeTimes;
    }
}
