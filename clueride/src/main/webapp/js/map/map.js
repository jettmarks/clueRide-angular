(function (angular) {
    'use strict';

    var mapModel,
        localModel = {};

    angular
        .module('crMap', [
            'leaflet-directive',
            'crPlayer.GameState',
            'crPlayer.Location',
            'crPlayer.Path',
            'common.CourseResource',
            'ui.bootstrap'
        ])
        .controller("MapController", MapController)
    ;

    MapController.$inject = [
        '$scope',
        '$location',
        'GameStateService',
        'LocationService',
        'PathMapResource',
        'CourseLocationMapResource',
        'leafletData'];

    function MapController(
        $scope,
        $location,
        GameStateService,
        LocationService,
        PathMapResource,
        CourseLocationMapResource,
        leafletData
    ) {
        var pathId = GameStateService.getPathId();

        localModel.leafletData = leafletData;
        localModel.pathMapResource = PathMapResource;
        localModel.completedPathIds = GameStateService.getCompletedPathIds();
        localModel.setLocationByNodeId = GameStateService.setLocationByNodeId;
        localModel.dollarLocation = $location;

        /* Sets up the Marker prefix so we get the ionic Icons. */
        L.AwesomeMarkers.Icon.prototype.options.prefix = 'ion';

        mapModel = this;

        mapModel.layers = {
            baselayers: {
                osm: {
                    name: 'OpenStreetMap',
                    url: '//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
                    type: 'xyz'
                },
                ocm: {
                    name: 'OpenCycleMap',
                    type: 'xyz',
                    // url: 'http://{s}.tile.opencyclemap.org/cycle/{z}/{x}/{y}.png',
                    /* Proxy the HTTPS */
                    url: '/tiles/{s}/{z}/{x}/{y}.png',
                    attribution: "All maps &copy; <a href=\"http://www.opencyclemap.org/\">OpenCycleMap</a>"
                }
            }
        };

        angular.extend(mapModel, {
            /* Center the map at last zoom level for editing. */
            center: {
                lat: LocationService.getMapCoords().lat,
                lng: LocationService.getMapCoords().lng,
                zoom: LocationService.getMapZoom(),
                autoDiscover: true
            },
            geojson: {},
            markers: {}
        });

        /* Record our last positions prior to closing the map. */
        $scope.$on("$destroy", function() {
            /* Clear out previous edit location. */
            LocationService.setEditLocation({});
            LocationService.setMapZoom(mapModel.center.zoom);
            LocationService.setMapCoords({
                lat: mapModel.center.lat,
                lon: mapModel.center.lng
            });
        });

        /* Put the current path on the map (unless we don't have one). */
        if (pathId) {
            console.log("Retrieving (current) map for Path " + pathId);
            PathMapResource.getMap({
                pathId: pathId
            }, featuresToMapCurrent );
            for (var completedPathId in localModel.completedPathIds) {
                makeHistoricalPathMapRequest(completedPathId)
            }
        } else if (LocationService.locations) {
            CourseLocationMapResource.getMap({
                locationId:  LocationService.getLocationId()
            }, locationFeatureToMap );
        }

        /* Add our current location to the map with a marker. */
        // TODO: Bring in the tethered values
        mapModel.markers = {
            me: new L.marker(
                {
                    lat: mapModel.center.lat,
                    lon: mapModel.center.lng || -84.1
                },
                {
                    icon: getMarkerIcon(0, 'BOOGER', false)
                }
            )
        }
    }

    function makeHistoricalPathMapRequest(p) {
        var pathId = localModel.completedPathIds[p];
        console.log("Retrieving (historical) map for Path " + pathId);
        localModel.pathMapResource.getMap({
            pathId: pathId
        }, featuresToMapHistory );
    }

    /**
     * How we place the Locations on the Map.
     *
     * @param feature
     * @param latlng
     * @returns {o.marker}
     */
    function pointToLayer(feature, latlng) {
        var marker = new L.marker(latlng, {
            icon: getMarkerIcon(
                feature.properties.pointId,
                feature.properties.state,
                feature.properties.selected
            )
        });

        marker.on({
            click: function (e) {
                var nodeId = e.target.feature.properties.pointId;
                console.log(e.target.feature.properties);
                localModel.setLocationByNodeId(nodeId);
                localModel.dollarLocation.path("/location");
            }
        });

        return marker;
    }

    function perFeatureHistory(feature, layer) {
        if (feature.geometry.type === 'LineString') {
            layer.setStyle({
                color: 'blue',
                opacity: 0.4,
                weight: 6
            });
        }
    }

    function perFeatureCurrent(feature, layer) {
        if (feature.geometry.type === 'LineString') {
            // console.log("Feature ID: " + feature.properties.edgeId);
            if (mapModel.featureBounds) {
                mapModel.featureBounds.extend(layer.getBounds());
            } else {
                mapModel.featureBounds = layer.getBounds();
            }
            layer.setStyle({
                color: 'green',
                opacity: 0.6,
                weight: 9
            });
            localModel.leafletData.getMap('gameMap').then(function (gameMap)
                {
                    gameMap.fitBounds(mapModel.featureBounds, {padding: [35,35]});
                }
            );
        }
    }

    function featuresToMapHistory(pathFeatures) {
        var pathId;

        // TODO: Brittle - order dependent
        pathId = pathFeatures.features[2].properties.pathId;

        // Define something if it isn't there already
        if (!mapModel.geojson[pathId]) {
            mapModel.geojson[pathId] = {};
        }

        angular.extend(mapModel.geojson[pathId],
            {
                data: pathFeatures,
                pointToLayer: pointToLayer,
                onEachFeature: perFeatureHistory
            }
        );
    }

    function featuresToMapCurrent(pathFeatures) {
        var pathId;

        /* Clear out previous dimensions of map. */
        mapModel.featureBounds = undefined;

        // TODO: Brittle - order dependent
        pathId = pathFeatures.features[2].properties.pathId;

        // Define something if it isn't there already
        if (!mapModel.geojson[pathId]) {
            mapModel.geojson[pathId] = {};
        }

        angular.extend(mapModel.geojson[pathId],
            {
                data: pathFeatures,
                pointToLayer: pointToLayer,
                onEachFeature: perFeatureCurrent
            }
        );
    }

    /**
     * Used to display a single point on the map, centered and at closest zoom level.
     * Generally used to show the origination of a Course.
     * @param pointFeatures - from a GeoJson Feature Collection of a single point/node.
     */
    function locationFeatureToMap(pointFeatures) {
        /* Replace previous dimensions of map. */
        mapModel.featureBounds = undefined;

        angular.extend(mapModel.geojson, {
            location: {
                data: pointFeatures,
                pointToLayer: pointToLayer,
                onEachFeature: perLocationFeature
            }
        });
    }

    function perLocationFeature(feature, layer) {
        var center = {
            lat: feature.geometry.coordinates[1],
            lng: feature.geometry.coordinates[0]
        };

        localModel.leafletData.getMap('gameMap').then(function (gameMap)
            {
                gameMap.setView(center, 17);
            }
        );

    }

}(window.angular));
