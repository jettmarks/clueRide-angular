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
 * Created by jett on 8/12/17.
 */
package com.clueride.service.builder;

import com.jettmarks.gmaps.encoder.Track;
import com.jettmarks.gmaps.encoder.Trackpoint;

/**
 * Born during a refactoring of Test modules, needed something to construct simple instances of the Track class
 * which exists outside of this module.
 */
public class TrackBuilder {
    private Trackpoint begin;
    private Trackpoint end;

    public TrackBuilder () {}

    public TrackBuilder withBegin(Trackpoint begin) {
        this.begin = begin;
        return this;
    }

    public TrackBuilder withEnd(Trackpoint end) {
        this.end = end;
        return this;
    }

    public Track build() {
        return new EasyTrack(
                this.begin,
                this.end
        );
    }

    private class EasyTrack extends Track {
        EasyTrack(
                Trackpoint begin,
                Trackpoint end
        ) {
            super();
            this.addTrackpoint(begin);
            this.addTrackpoint(end);
        }
    }

}
