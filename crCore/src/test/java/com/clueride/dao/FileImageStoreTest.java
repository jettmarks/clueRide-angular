/*
 * Copyright 2015 Jett Marks
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
 * Created by jett on 12/20/15.
 */
package com.clueride.dao;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.InputStream;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.*;

/**
 * Exercises the FileImageStoreTest class.
 */
public class FileImageStoreTest {

    private FileImageStore toTest;

    @Mock
    InputStream inputStream;

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
        toTest = new FileImageStore();
    }

    @Test
    public void testAddNewOK() throws Exception {
        Integer expected = 1;
        Integer actual = toTest.addNew(-1, inputStream);
        assertEquals(actual, expected);
    }

    @Test
    public void testGetIdFromFileNameOK() throws Exception {
        Integer expected = 6;
        File testFile = new File("6.jpg");
        Integer actual = toTest.getIdFromFileName(testFile);
        assertEquals(actual, expected);
    }

    @Test (expectedExceptions = IllegalArgumentException.class)
    public void testGetIdFromFileNameRogueFile() throws Exception {
        Integer expected = 6;
        File testFile = new File("6jpg");
        Integer actual = toTest.getIdFromFileName(testFile);
        assertEquals(actual, expected);
    }
}