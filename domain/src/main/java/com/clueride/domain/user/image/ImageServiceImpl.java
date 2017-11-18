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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;

import com.clueride.aop.badge.BadgeCapture;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.requireNonNull;

/**
 * Implementation of ImageService interface.
 */
public class ImageServiceImpl implements ImageService {
    private final ImageStore imageStore;

    @Inject
    public ImageServiceImpl(
            ImageStore imageStore
    ) {
        this.imageStore = imageStore;
    }

    @Override
    public Image getById(Integer id) {
        Image.Builder imageBuilder = imageStore.getById(id);
        if (imageBuilder == null) {
            return null;
        }
        try {
            imageBuilder.withUrl(new URL(imageBuilder.getUrlString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Image from datastore has malformed URL", e);
        }
        return imageBuilder.build();
    }

    @Override
    public URL getImageUrl(Integer id) {
        if (id == null) {
            return null;
        }

        Image image = getById(id);
        if (image == null) {
            return null;
        }

        return image.getUrl();
    }

    @Override
    public Integer addNew(Image.Builder imageBuilder) throws MalformedURLException {
        validateUrlString(imageBuilder.getUrlString());
        imageBuilder
                .withId(null)
                .withUrl(new URL(imageBuilder.getUrlString()));
        return imageStore.addNew(imageBuilder);
    }

    @Override
    @BadgeCapture
    public Integer addNewToLocation(
            Image.Builder imageBuilder,
            Integer locationId
    ) throws MalformedURLException {
        validateLocationId(locationId);
        Integer newImageId = addNew(imageBuilder);
        Integer mapId = imageStore.linkImageToLocation(newImageId, locationId);
        return newImageId;
    }

    private void validateLocationId(Integer locationId) {
        requireNonNull(locationId);
    }

    @Override
    public List<Image> getImagesByLocation(Integer locationId) {
        return null;
    }

    private void validateUrlString(String urlString) throws MalformedURLException {
        if (isNullOrEmpty(urlString)) {
            throw new RuntimeException("URL is null or empty");
        }
        new URL(urlString);
    }
}
