/**
 *   Copyright 2015 Jett Marks
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created Oct 4, 2015
 */
package com.clueride.domain.dev;

/**
 * Description.
 *
 * @author jett
 *
 */
public class UnratedSegmentImpl extends TrackImpl implements UnratedSegment {

    private Node startNode;
    private Node endNode;

    /**
     * @param displayName
     * @param url
     */
    public UnratedSegmentImpl(String displayName, String url) {
        super(displayName, url);
    }

    /**
     * @see com.clueride.domain.dev.UnratedSegment#getStart()
     */
    public Node getStart() {
        return startNode;
    }

    /**
     * @see com.clueride.domain.dev.UnratedSegment#getEnd()
     */
    public Node getEnd() {
        return endNode;
    }

}
