'use strict';

var LocModule = angular.module('crNetEdit.LocModule');

LocModule
    .factory('LocResource', function ($resource) {
        return $resource('/crMain/rest/nodes/new', {}, {
            request: {
                method: 'POST',
                params: {},
                isArray: false
            },
            confirm: {
                method: 'PUT',
                params: {},
                isArray: false
            }
        });
    })
    .factory('LocDiagResource', function ($resource) {
        return $resource('/crMain/rest/nodes/allNodes', {}, {
            showAllNodes: {
                method: 'GET',
                params: {},
                isArray: false
            }
        });
    });


    function showNodes($scope, LocDiagResource) {
        LocDiagResource.showAllNodes( {},
            function (pointFeature) {
                angular.extend($scope.gjNetwork, {
                    newPoints: {
                        data: pointFeature,
                        pointToLayer: function (feature, latlng) {
                            var marker = new L.marker(latlng, {
                                icon: getMarkerIcon(
                                    feature.properties.pointId,
                                    feature.properties.state,
                                    feature.properties.selected
                                ),
                                // Later when we want to edit, we'll pay attention
                                draggable: false
                            });
                            return marker;
                        }
                    }
                })
            }
        );
    };

    LocModule.showNodes = showNodes;