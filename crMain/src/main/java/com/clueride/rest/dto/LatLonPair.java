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
 * Created Sep 16, 2015
 */
package com.clueride.rest.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Description.
 *
 * @author jett
 *
 */

@XmlRootElement
public class LatLonPair {
    @XmlElement
    public double lat;
    @XmlElement
    public double lon;

    public LatLonPair() {
    }
    //
    // public LatLonPair(Double lat, Double lon) {
    // this.lat = lat;
    // this.lon = lon;
    // }
}
