(function (angular) {
    'use strict';

    var network = { };

    angular
        .module('crMap', ['leaflet-directive', 'crPlayer.GameState','crPlayer.Path'])
        .controller("MapController", MapController)
    ;

    MapController.$inject = ['$scope','gameStateService','PathMapResource'];

    function MapController($scope, gameStateService, PathMapResource) {
        var pathId = gameStateService.getPathId();

        network = $scope;

        angular.extend($scope, {
            layers: {
                baselayers: {
                    ocm: {
                        name: 'OpenCycleMap',
                        type: 'xyz',
                        // url: 'http://{s}.tile.opencyclemap.org/cycle/{z}/{x}/{y}.png',
                        /* Proxy the HTTPS */
                        url: '/tiles/{s}/{z}/{x}/{y}.png',
                        attribution: "All maps &copy; <a href=\"http://www.opencyclemap.org/\">OpenCycleMap</a>"
                    },
                    osm: {
                        name: 'OpenStreetMap',
                        url: '//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
                        type: 'xyz'
                    }
                }
            }
        });

        angular.extend($scope, {
            center: {
                lat: 33.7627,
                lng: -84.3527,
                zoom: 12,
                autoDiscover: true
            },
            geojson: {},
            markers: {}
        });

        /* Put the current path on the map. */
        PathMapResource.getMap({
                pathId: pathId
            }, featuresToMap );

    }

    function featuresToMap(pathFeatures) {
        angular.extend(network.geojson, {
            path: {
                data: pathFeatures
            }
        });
    }

}(window.angular));
