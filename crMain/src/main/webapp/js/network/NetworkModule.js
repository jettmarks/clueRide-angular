(function (angular) {
'use strict';
    var segments = {},
        selectedSegment = {},
        controlResource = function () {};


    angular.module('network', ['ngResource'])
        .controller('NetworkController', NetworkController)
        .factory('NetworkResource', NetworkResource)
        .factory('ControlResource', ControlResource)
        .service('NetworkRefresh', NetworkRefresh)
    ;

    NetworkController.$inject = ['$scope',
        'NetworkResource',
        'ControlResource',
        'NetworkRefresh'];

    function NetworkController($scope, NetworkResource, ControlResource, NetworkRefresh) {
        $scope.networkResource = NetworkResource;
        $scope.controlResource = ControlResource;
        $scope.networkRefresh = NetworkRefresh;
        controlResource = ControlResource;
    }

    function ControlResource($resource) {
        return $resource('/crMain/rest/control/refresh', {}, {
            refresh: {
                method: 'GET',
                params: {},
                isArray: false
            }
        });
    }

    function NetworkResource($resource) {
        return $resource('/crMain/rest/network', {}, {
            request: {
                method: 'GET',
                params: {},
                isArray: false
            }
        });
    }

    function load(NetworkResource) {
        //var networkResource = angular.injector().get('NetworkResource');

        NetworkResource.get(
            {} , function (networkFeatureCollection) {
                angular.extend(segments, {
                    data: networkFeatureCollection,
                    style: {
                        opacity: 0.7,
                        color: '#030',
                        weight: 4
                    },
                    onEachFeature: function (feature, layer) {
                        layer.on('click', function (e) {
                            angular.extend(selectedSegment, this.feature);
                        });
                    }
                });
            }
        );
    }

    /**
     * Makes call over to server to refresh the indices.
     */
    function refreshMap() {
        controlResource.refresh( {},
            function (retVal) {});
    }

    function NetworkRefresh() {
        return {
            refresh: refreshMap,
            load: load,
            segments: function () {
                return segments
            },
            selectedSegment: function () {
                return selectedSegment
            }
        };
    }

}(window.angular));
