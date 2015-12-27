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
 * Created Aug 30, 2015
 */
package com.clueride.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.clueride.service.DefaultNetwork;

/**
 * Maps between the Network end point and the NetworkService which knows how to
 * manage the Network.
 *
 * @author jett
 *
 */
@Path("network")
public class Network {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getNetwork() {
        return DefaultNetwork.getInstance().getNetworkForDisplay();
    }

}
