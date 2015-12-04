package com.clueride.domain.user;

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
public enum LocationType {
    RESTAURANT("Restaurant"),
    FOOD_TO_GO("Food to Go"),
    BAR("Bar"),
    COFFEE("Coffee"),
    RETAIL("Retail"),
    BEVERAGES_TO_GO("Beverages to Go"),
    ART_SCULPTURE("Art & Sculpture"),
    MUSEUM("Museum"),
    PICNIC("Picnic: Parks, Cemeteries, Squares"),
    SCENIC("Scenic"),
    CHAMPION_TREE("Champion Tree"),
    GEOCACHE("GeoCache"),
    HISTORICAL_MARKER("Historical Marker");

    private final String name;
    LocationType(String name) {this.name = name;}
    public String getValue() {return name;}

}