(function (angular) {
    'use strict';

    var module = angular.module('crMap', ['leaflet-directive']);

    module.controller("MapController", ['$scope','$rootScope',
        function ($scope, $rootScope) {
            angular.extend($scope, {
                layers: {
                    baselayers: {
                        ocm: {
                            name: 'OpenCycleMap',
                            type: 'xyz',
                            url: 'http://{s}.tile.opencyclemap.org/cycle/{z}/{x}/{y}.png',
                            attribution: "All maps &copy; <a href=\"http://www.opencyclemap.org/\">OpenCycleMap</a>"
                        },
                        osm: {
                            name: 'OpenStreetMap',
                            url: 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
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
        }
    ]);

}(window.angular));
