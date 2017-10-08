package com.clueride.domain.user.image;/*
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

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Provider;

import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.clueride.domain.DomainGuiceModuleTest;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Exercises the ImageServiceImplTest class.
 */
@Guice(modules= DomainGuiceModuleTest.class)
public class ImageServiceImplTest {
    private boolean runWithDB = ("true".equals(System.getProperty("db.available")));
    private boolean runWithMocks = !runWithDB;

    private ImageServiceImpl toTest;

    @Inject
    private Provider<ImageServiceImpl> toTestProvider;

    @Inject
    private Image image;

    @Inject
    private Image.Builder imageBuilder;

    @Inject
    private ImageStore imageStore;

    @BeforeMethod
    public void setUp() throws Exception {
        reportRunState();
        assertNotNull(toTestProvider);
        assertNotNull(image);

        toTest = toTestProvider.get();
        assertNotNull(toTest);
    }

    @Test
    public void testGetById() throws Exception {
    }

    @Test
    public void testAddNew() throws Exception {
        validateDBAvailability();

        toTest.addNew(Image.Builder.from(image));
    }

    @Test
    public void testGetImageUrl_OK() throws Exception {
        /* setup test */
        URL expected = image.getUrl();
        /* train mocks */
        if (runWithMocks) {
            when(imageStore.getById(image.getId())).thenReturn(imageBuilder);
        }

        /* make call */
        URL actual = toTest.getImageUrl(image.getId());
        assertEquals(actual, expected);
    }

    @Test
    public void testGetImageUrl_noImageForId() throws Exception {
        /* train mocks */
        if (runWithMocks) {
            when(imageStore.getById(image.getId())).thenReturn(imageBuilder);
        }

        /* make call */
        URL actual = toTest.getImageUrl(image.getId()+1);

        assertTrue(actual == null);
    }

    @Test
    public void testGetImageUrl_missingId() throws Exception {
        /* train mocks */
        if (runWithMocks) {
            when(imageStore.getById(image.getId())).thenReturn(imageBuilder);
        }

        /* make call */
        URL actual = toTest.getImageUrl(null);

        assertTrue(actual == null);
    }

    private void validateDBAvailability() {
        if (!runWithDB) {
            throw new SkipException("Database isn't available");
        }
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testAddNew_emptyUrl() throws Exception {
        /* setup test */
        Image.Builder imageBuilderWithBadUrl = Image.Builder.builder().withUrlString("");
        toTest.addNew(imageBuilderWithBadUrl);
    }

    @Test(expectedExceptions = MalformedURLException.class)
    public void testAddNew_malformedUrl() throws Exception {
        /* setup test */
        Image.Builder imageBuilderWithBadUrl = Image.Builder.builder().withUrlString("htp://domain.org");
        toTest.addNew(imageBuilderWithBadUrl);
    }

    private void reportRunState() {
        if (runWithDB) {
            System.out.println("Running with actual DB connection");
        }

        if (runWithMocks) {
            System.out.println("Running with Mocked DAO/Stores");
        }
    }

}