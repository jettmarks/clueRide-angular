/*
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
 * Created Aug 27, 2015
 */
package com.clueride.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.clueride.rest.dto.LatLonPair;
import com.clueride.rest.dto.PointId;
import com.clueride.rest.dto.RecId;
import com.clueride.service.NodeService;
import com.clueride.service.RecommendationService;

/**
 * Maps between the locations end point and the LocationService which knows how
 * to manage locations.
 *
 * @author jett
 *
 */
// TODO: Survey the endpoints to learn which ones are still in use -- or plan to be used.
@Path("nodes")
public class Nodes {
    private final NodeService nodeService;
    private final RecommendationService recService;

    @Inject
    public Nodes(NodeService nodeService, RecommendationService recService) {
        this.nodeService = nodeService;
        this.recService = recService;
    }

    /**
     * Return the geometry corresponding to a particular Recommendation.
     *
     * @param recId - Unique ID for the Recommendation whose geometry is being requested
     *              for display on the map.
     * @return JSON String based on a Feature Collection.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("new")
    public String getRecGeometry(@QueryParam("recId") Integer recId) {
        return recService.getRecGeometry(recId);
    }

    /**
     * Accepts LatLonPair (in JSON) representing a new node we want to add to
     * the network, and returns potential links to the existing network.
     * 
     * @param pair - Lat/Lon of a Node in consideration for adding to the network.
     * @return JSON String carrying recommendation summary.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("new")
    public String getNewLocationPost(LatLonPair pair) {
        return nodeService.addNewNode(pair.lat, pair.lon);
    }

    /**
     * After having recommended a segment, the response to clicking on that
     * segment results in a call to this "PUT" to confirm that we're accepting
     * the proposed segment.
     * 
     * Data regarding the segment is held server-side for performing the actions
     * required to add the segment to the network.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("new")
    public String confirmProposedSegment(RecId recId ) {
        return recService.confirmRecommendation(recId.recId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("groups")
    public String getNodeGroups() {
        return nodeService.getNodeGroups();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("group/set")
    public String setNodeGroup(@QueryParam("id") Integer id,
            @QueryParam("lat") Double lat, @QueryParam("lon") Double lon) {
        return nodeService.setNodeGroup(id, lat, lon);
    }

    // TODO: CA-55 Put in the Diagnostics package/bag
    // return new LocationService().showPointsOnTrack(pair.lat, pair.lon);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("allNodes")
    public String showAllNodes() {
        return nodeService.showAllNodes();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("segment")
    public String getMatchingSegments(@QueryParam("pointId") Integer pointId) {
        return nodeService.getMatchingSegments(pointId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("update")
    public String getUpdatedEdges(
            @QueryParam("pointId") Integer pointId,
            @QueryParam("lat") Double lat,
            @QueryParam("lng") Double lng
    ) {
        return nodeService.getEdgesAtNewLocation(pointId, lat, lng);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("update")
    public String confirmUpdatedEdges(PointId pointId) {
        return nodeService.confirmEdgesAtNewLocation(pointId.pointId);
    }
}
