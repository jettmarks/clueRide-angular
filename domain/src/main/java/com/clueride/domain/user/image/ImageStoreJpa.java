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
 * Created by jett on 10/8/17.
 */
package com.clueride.domain.user.image;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of the Image Store against JPA.
 */
public class ImageStoreJpa implements ImageStore {

    private final EntityManager entityManager;

    @Inject
    public ImageStoreJpa(
            @Nonnull EntityManager entityManager
    ) {
        this.entityManager = requireNonNull(entityManager, "missing Entity Manager");
    }

    @Override
    public Integer addNew(Image.Builder imageBuilder) {
        entityManager.getTransaction().begin();
        entityManager.persist(imageBuilder);
        entityManager.getTransaction().commit();
        return imageBuilder.getId();
    }

    @Override
    public Image.Builder getById(Integer imageId) {
        if (imageId == null) {
            return null;
        }
        entityManager.getTransaction().begin();
        Image.Builder imageBuilder = entityManager.find(Image.Builder.class, imageId);
        entityManager.getTransaction().commit();
        return imageBuilder;
    }

    @Override
    public Integer linkImageToLocation(Integer imageId, Integer locationId) {
        ImageByLocation imageByLocation = new ImageByLocation();
        imageByLocation.image_id = imageId;
        imageByLocation.location_id = locationId;

        entityManager.getTransaction().begin();
        entityManager.persist(imageByLocation);
        entityManager.getTransaction().commit();
        return imageByLocation.id;
    }

    @Entity(name="image_by_location")
    public static class ImageByLocation {
        @Id
        @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="image_by_location_pk_sequence")
        @SequenceGenerator(name="image_by_location_pk_sequence",
                sequenceName="images_by_location_id_seq",
                allocationSize=1)
        Integer id;

        Integer image_id;
        Integer location_id;
    }
}
