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
package com.clueride.poc.geotools;

import java.util.List;

import com.clueride.domain.dev.Node;
import com.clueride.domain.dev.Segment;
import com.clueride.domain.dev.SegmentImpl;

/**
 * Description.
 *
 * @author jett
 *
 */
public class TempSegmentImpl extends SegmentImpl implements Segment {

    public TempSegmentImpl(Integer id) {
        super("", "");
        this.id = id;
    }

    /**
     * @see com.clueride.domain.dev.Track#getId()
     */
    @Override
    public Integer getId() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see com.clueride.domain.dev.Track#getNodeList()
     */
    @Override
    public List<Node> getNodeList() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param name
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
