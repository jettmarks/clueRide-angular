'use strict';

var LocModule = angular.module('crNetEdit.LocModule');

LocModule
    .factory('LocResource', function ($resource) {
        return $resource('/crMain/rest/nodes/new', {}, {
            check: {
                /* Propose new lat/lon pair, and tell us if it is on the network. */
                method: 'POST',
                params: {},
                isArray: false
            },
            request: {
                /* Given a set of recommendations, get the geometry for a particular rec. */
                method: 'GET',
                params: {},
                isArray: false
            },
            confirm: {
                /* Tell the server which recommendation to accept. */
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
        var mapModel = $scope.mapModel;
        LocDiagResource.showAllNodes( {},
            function (pointFeature) {
                angular.extend(mapModel.gjNetwork, {
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