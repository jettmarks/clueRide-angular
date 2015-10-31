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
 * Created Sep 24, 2015
 */
package com.clueride.geo.score;

import com.jettmarks.gmaps.encoder.Track;
import com.jettmarks.gmaps.encoder.Trackpoint;

/**
 * Extension that makes it easy to setup a 2-point Track for Testing.
 * 
 * @author jett
 */
public class EasyTrack extends Track {

    public EasyTrack(Trackpoint a, Trackpoint b) {
        super();
        this.addTrackpoint(a);
        this.addTrackpoint(b);
    }

}
