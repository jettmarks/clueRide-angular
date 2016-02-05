(function (angular) {
    'use strict';

    var mapModel;

    angular
        .module('crMap', ['leaflet-directive', 'crPlayer.GameState','crPlayer.Path','ui.bootstrap'])
        .controller("MapController", MapController)
    ;

    MapController.$inject = ['$scope','gameStateService','PathMapResource'];

    function MapController($scope, gameStateService, PathMapResource) {
        var pathId = gameStateService.getPathId(),
            completedPathIds = gameStateService.getCompletedPathIds();

        /* Sets up the Marker prefix so we get the ionic Icons. */
        L.AwesomeMarkers.Icon.prototype.options.prefix = 'ion';

        //network = $scope;
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
            layer.setStyle({
                color: 'blue',
                opacity: 0.4,
                weight: 6
            });
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
        angular.extend(mapModel.geojson, {
            path: {
                data: pathFeatures,
                pointToLayer: pointToLayer,
                onEachFeature: perFeatureCurrent
            }
        });
    }

}(window.angular));
