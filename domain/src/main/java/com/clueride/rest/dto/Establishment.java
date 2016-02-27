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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Holds the post parameters for an updated Establishment as part of a Location.
 */
@XmlRootElement
public class Establishment {
    public String name;
    public String description;

}
