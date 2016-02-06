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
        'gameStateService',
        'locationService',
        'PathMapResource',
        'CourseLocationMapResource',
        'leafletData'];

    function MapController(
        gameStateService,
        locationService,
        PathMapResource,
        CourseLocationMapResource,
        leafletData
    ) {
        var pathId = gameStateService.getPathId(),
            completedPathIds = gameStateService.getCompletedPathIds();

        localModel.leafletData = leafletData;
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
            center: {
                lat: 33.7627,
                lng: -84.3527,
                zoom: 12,
                autoDiscover: true
            },
            geojson: {},
            markers: {}
        });

        /* Put the current path on the map (unless we don't have one). */
        if (pathId) {
            PathMapResource.getMap({
                pathId: pathId
            }, featuresToMapCurrent );
            for (var completedPathId in completedPathIds) {
                PathMapResource.getMap({
                    pathId: completedPathIds[completedPathId]
                }, featuresToMapHistory );
            }
        } else if (locationService.locations) {
            CourseLocationMapResource.getMap({
                locationId:  locationService.getLocationId()
            }, locationFeatureToMap );
        }

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

        /* Put responses to marker events here. */

        return marker;
    }

    function perFeatureHistory(feature, layer) {
        if (feature.geometry.type === 'LineString') {
            layer.setStyle({
                color: 'green',
                opacity: 0.6,
                weight: 9
            });
        }
    }

    function perFeatureCurrent(feature, layer) {
        if (feature.geometry.type === 'LineString') {
            console.log("Feature ID: " + feature.properties.edgeId);
            if (mapModel.featureBounds) {
                mapModel.featureBounds.extend(layer.getBounds());
            } else {
                mapModel.featureBounds = layer.getBounds();
            }
            layer.setStyle({
                color: 'blue',
                opacity: 0.4,
                weight: 6
            });
            localModel.leafletData.getMap('gameMap').then(function (gameMap)
                {
                    gameMap.fitBounds(mapModel.featureBounds);
                }
            );
        }
    }

    function featuresToMapHistory(pathFeatures) {
        angular.extend(mapModel.geojson, {
            path: {
                data: pathFeatures,
                pointToLayer: pointToLayer,
                onEachFeature: perFeatureHistory
            }
        });
    }

    function featuresToMapCurrent(pathFeatures) {
        /* Clear out previous dimensions of map. */
        mapModel.featureBounds = undefined;

        angular.extend(mapModel.geojson, {
            path: {
                data: pathFeatures,
                pointToLayer: pointToLayer,
                onEachFeature: perFeatureCurrent
            }
        });
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
