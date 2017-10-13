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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * Implementation of ImageStore that places the Image data into a file
 * on an apache server's file system.
 *
 * Caller is responsible for knowing how to build the URL to access the data
 * which is keyed by the Location ID (provided) and the sequence ID (returned).
 *
 * This implementation places the file under a configurable root directory using
 * the location ID as the sub-directory, and a sequential file name for multiple
 * instances of images within that sub-directory.
 */
public class FileImageStore implements ImageStore {
    private static final Logger LOGGER = Logger.getLogger(FileImageStore.class);

    // TODO: CA-319 - remove testing dependency on this mount being present.
    private static final String BASE_DIR_STRING = "/media/crImg/img";
    private static final File BASE_DIR = new File(BASE_DIR_STRING);
    private Map<Integer,File> locationDirectories = new HashMap<>();

    public FileImageStore() {
        initialize();
    }

    /**
     * There is an opportunity to build a cache of the contents of each directory,
     * but for now, this only builds a cache of the locations' directories.
     */
    private void initialize() {
        if (!BASE_DIR.canWrite()) {
            throw new IllegalAccessError("Unable to write to Image Datastore");
        }

        for (File locDir : BASE_DIR.listFiles()) {
            try {
                Integer locId = Integer.parseInt(locDir.getName());
                locationDirectories.put(locId, locDir);
            } catch (NumberFormatException nfe) {
                LOGGER.warn("Unexpected entry in image directory: " + locDir.getName());
            }
        }

    }

    @Override
    public Integer addNew(Integer locationId, InputStream imageData) throws FileNotFoundException {
        Integer newSequenceId = 1;
        File locDir;
        File newImageFile;
        // Make sure only one thread at a time is updating the list of image files
        synchronized (locationDirectories) {
            if (locationDirectories.containsKey(locationId)) {
                // Use existing directory; find maximum
                locDir = locationDirectories.get(locationId);
                for (File imageFile : locDir.listFiles()) {
                    Integer existingSeqId = getIdFromFileName(imageFile);
                    if (newSequenceId <= existingSeqId) {
                        newSequenceId = existingSeqId+1;
                    }
                }
            } else {
                // create new directory
                locDir = new File(BASE_DIR + File.separator + locationId);
                locDir.mkdirs();
                locationDirectories.put(locationId, locDir);
            }
            String newFileName = locDir.getPath() + File.separator + newSequenceId + ".jpg";
            newImageFile = new File(newFileName);
            try {
                newImageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Ready to write the file
        try {
            FileOutputStream out = new FileOutputStream(newImageFile);
            IOUtils.copy(imageData, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newSequenceId;
    }

    /**
     * Turns an image file name into the integer sequence number.
     * @param imageFile - an existing (or perhaps proposed) file name for an image file.
     * @return Integer representing the sequence id of this image file.
     * @throws IllegalArgumentException if the file name doesn't match the pattern.
     */
    Integer getIdFromFileName(File imageFile) {
        return Integer.parseInt(imageFile.getName().split("\\.")[0]);
    }
}
