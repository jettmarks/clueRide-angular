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

/**
 * Defines operations on Images.
 */
public interface ImageService {
    /**
     * Retrieves the Image instance matching the given ID.
     * @param id unique identifier for the image.
     * @return Fully-populated Image instance with valid URL.
     */
    Image getById(Integer id);

    /**
     * Given the ID of an image, retrieve the URL used to locate the image.
     * @param id unique identifier for the image.
     * @return Validated URL or null if there is no URL for the ID.
     */
    URL getImageUrl(Integer id);

    /**
     * Persists a new image.
     * Validation of the URL occurs within this method.
     * @param imageBuilder mutable object that contains at least the String representation for the image URL.
     * @return New ID for the record.
     */
    Integer addNew(Image.Builder imageBuilder) throws MalformedURLException;

    /**
     * Persists a new image for a given Location.
     * Validation of the location assures it has been created.
     * @param imageBuilder mutable object containing at least the String representation for the image URL.
     * @param locationId unique identifier for the Location this image represents.
     * @return New ID for the record.
     * @throws MalformedURLException if the supplied URL has problems.
     */
    Integer addNewToLocation(
            Image.Builder imageBuilder,
            Integer locationId
    ) throws MalformedURLException;

    /**
     * Given a Location ID, retrieve the list of Images for that location.
     * @param locationId unique identifier for the location.
     * @return List<Image> matching the location.
     */
    List<Image> getImagesByLocation(Integer locationId);

}
