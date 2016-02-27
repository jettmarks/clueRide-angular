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
 * Created by jett on 2/20/16.
 */
package com.clueride.rest.dto;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.clueride.domain.user.LocationType;

/**
 * Holds the post parameters for an updated Location.
 */
@XmlRootElement
public class Location {
    public Integer id;
    public String name;
    public String description;
    public Integer nodeId;
    public LocationType locationType;
    public Integer[] clueIds;

    @XmlElement(nillable=true)
    public Establishment establishment;

    @XmlElement(nillable=true)
    public Integer locationGroupId;
    public List<URL> imageUrls;

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", locationType=" + locationType +
                ", clueIds=" + Arrays.toString(clueIds) +
                ", establishment=" + establishment +
                ", locationGroupId=" + locationGroupId +
                '}';
    }

}
