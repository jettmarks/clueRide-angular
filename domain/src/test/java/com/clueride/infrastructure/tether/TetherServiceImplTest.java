package com.clueride.infrastructure.tether;/*
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

import java.util.Date;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.rest.dto.LatLonPair;

/**
 * Exercises the TetherServiceImplTest class.
 */
public class TetherServiceImplTest {
    private TetherServiceImpl toTest = new TetherServiceImpl();

    @BeforeMethod
    public void setUp() throws Exception {
    }

    @Test
    public void testGetDevModeLatLon() throws Exception {
        long startTic = new Date().getTime();

        for(long i=startTic; i< startTic+TetherServiceImpl.ONE_HOUR_IN_MS; i += 10000) {
            LatLonPair latLon = toTest.figureEightLatLon(i);
            System.out.println(
                    latLon.lat +
                            "," +
                            latLon.lon
            );
        }

    }

}