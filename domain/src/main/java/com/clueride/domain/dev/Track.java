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
 * Created Jul 28, 2015
 */
package com.clueride.domain.dev;

import java.util.List;

/**
 * Directed sequence of Nodes representing a path that can be traversed in at
 * least the forward direction by bicycle.
 *
 * Generally, these are one-to-one with Segments where a single rating can be
 * applied to the entire length of the Track. This avoids two problems:
 * <OL>
 * <LI>recording different ratings within a Track and
 * <LI>needing to handle Tracks with "features" embedded within the Track.
 * </OL>
 * 
 * An example makes the second point easier to follow. If there is a set of
 * stairs that needs to be traversed in the middle of a track, it is easier for
 * the Course and Leg modeling to treat this as two tracks that are connected by
 * a set of stairs.
 * 
 * @author jett
 */
public interface Track {
    Integer getId();

    String getDisplayName();

    void setDisplayName(String displayName);

    /**
     * Many of these tracks are brought into this tool via RideWithGPS tracks;
     * this refers back to the URL where the track can be retrieved. TODO: May
     * be a spot we could use the Optional<String> type.
     * 
     * @return String representing the URL of the source track.
     */
    String getUrl();

    List<Node> getNodeList();
}
