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
 * Created by jett on 12/7/17.
 */
package com.clueride.domain.user.layer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * Defines the attributes for a Map Layer.
 */
@Entity(name="layer")
public class Layer {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "layer_pk_sequence")
    @SequenceGenerator(name="layer_pk_sequence", sequenceName = "layer_id_seq", allocationSize = 1)
    private Integer id;

    @Column
    private String name;

    @Column
    private String description;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
