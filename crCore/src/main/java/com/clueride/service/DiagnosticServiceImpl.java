/*
 * Copyright 2016 Jett Marks
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
 * Created by jett on 2/10/16.
 */
package com.clueride.service;

import java.util.List;

import javax.inject.Inject;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

import com.clueride.domain.DefaultGeoNode;
import com.clueride.domain.GeoNode;
import com.clueride.domain.dev.NetworkProposal;
import com.clueride.domain.dev.NewNodeProposal;
import com.clueride.domain.dev.rec.DiagnosticRec;
import com.clueride.domain.factory.PointFactory;
import com.clueride.feature.TrackFeature;
import com.clueride.geo.TranslateUtil;

/**
 * Implementation of the Diagnostic Service.
 */
public class DiagnosticServiceImpl implements DiagnosticService {
    /**
     * Injectable constructor.
     */
    @Inject
    DiagnosticServiceImpl() {}

    @Override
    public String showPointsOnTrack(Double lat, Double lon) {
        String result;

        GeoNode newLocation;
        newLocation = getCandidateLocation(lat, lon);
        NetworkProposal networkProposal = buildPointsOnTrackProposal(newLocation);
        result = networkProposal.toJson();
        return result;
    }

    /**
     * @param lat - Latitude of a point on the Track we want to see.
     * @param lon - Longitude of a point on the Track we want to see.
     * @return Node at the provided location.
     */
    private GeoNode getCandidateLocation(Double lat, Double lon) {
        GeoNode newLocation;
        Point point = PointFactory.getJtsInstance(lat, lon, 0.0);
        newLocation = new DefaultGeoNode(point);
        newLocation.setName("candidate");
        return newLocation;
    }

    /**
     * @param node - The node that may reside on the track whose points we want to see.
     * @return NetworkProposal which can be turned into a GeoJSON list of points.
     */
    private NetworkProposal buildPointsOnTrackProposal(GeoNode node) {
        GeoEval geoEval = GeoEval.getInstance();
        NewNodeProposal newNodeProposal = new NewNodeProposal(node);
        List<TrackFeature> coveringTracks = geoEval.listCoveringTracks(node);
        DiagnosticRec diagRec = new DiagnosticRec(node);
        if (coveringTracks.size() == 1) {
            TrackFeature track = coveringTracks.get(0);
            LineString lineString = track.getLineString();
            for (int i = 0; i < lineString.getNumPoints(); i++) {
                diagRec.addFeature(TranslateUtil
                        .geoNodeToFeature(new DefaultGeoNode(lineString
                                .getPointN(i))));
            }
        }
        newNodeProposal.add(diagRec);
        return newNodeProposal;
    }
}
