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
 * Created by jett on 11/28/17.
 */
package com.clueride.infrastructure.tether;

import java.util.Date;

import com.clueride.rest.dto.LatLonPair;

/**
 * Implementation of Tether Service providing mock LatLon positioning when
 * GPS isn't available (or we want to force a given set of positions).
 */
public class TetherServiceImpl implements TetherService {
    private static LatLonPair latLonCenter = new LatLonPair();
    public static final int ONE_HOUR_IN_MS = 3600 * 1000;
    public static final double X_SCALE_IN_DEG = 0.050;
    public static final double Y_SCALE_IN_DEG = 0.025;

    static {
        latLonCenter.lat = 33.76;
        latLonCenter.lon = -84.38;
    }

    @Override
    public LatLonPair getDevModeLatLon() {
        return figureEightLatLon(new Date().getTime());
    }

    LatLonPair figureEightLatLon(long time) {
        long tic = time % ONE_HOUR_IN_MS;
        double radians = 2 * Math.PI * tic / ONE_HOUR_IN_MS;

        LatLonPair latLonPair = new LatLonPair();
        latLonPair.lat = Math.cos(radians) * Y_SCALE_IN_DEG + latLonCenter.lat;
        latLonPair.lon = Math.sin(radians * 2.0) * X_SCALE_IN_DEG + latLonCenter.lon;
        return (latLonPair);
    }

}
