(function (angular) {
    'use strict';

    var locResource,
        networkRefresh,
        module = angular.module('crNetEdit.recs', ['crNetEdit.LocModule','ui.bootstrap']);

    module.controller('RecSelectController', ['$scope', 'LocResource', 'NetworkRefresh',
        function ($scope, LocResource, NetworkRefresh) {
            var selectedRec = {
                    id: 0,
                    name: 'Choose a Recommendation'
                },
                recs = [
                {
                    id: 1,
                    name: 'POINT(-84.12345,34.12345)'
                },
                {
                    id: 2,
                    name: 'POINT(-84.22345,34.12345)'
                },
                {
                    id: 3,
                    name: 'POINT(-84.32345,34.12345)'
                }
            ];

            locResource = LocResource;
            networkRefresh = NetworkRefresh;

            //$scope.recs = function () {return (recs)};
            $scope.recs = recs;
            $scope.selectedRec = selectedRec;

            $scope.select = function (rec) {
                $scope.selectedRec = rec;
            };

        }
    ]);

    /*
     * Called to evaluate a lat/lon pair for connection to network.
     */
    module.service('newNodeService', function () {
        /* Our copy of the shared instance of the network that we update */
        var gjNetwork = {};

        return {
            // For binding to the appropriate scope
            gjNetwork: function () {
                return gjNetwork;
            },

            checkNode: function checkNewNode(latlng) {
                locResource.request({
                    lat: latlng.lat,
                    lon: latlng.lng
                }, function (pointFeature) {
                    /* Our extension of the local network. */
                    angular.extend(gjNetwork, {
                        newPoints: {
                            data: pointFeature,
                            pointToLayer: function (feature, latlng) {
                                var marker = new L.marker(latlng, {
                                    icon: getMarkerIcon(
                                        feature.properties.pointId,
                                        feature.properties.state,
                                        feature.properties.selected),
                                    draggable: (feature.properties.name === 'candidate')
                                });
                                marker.on({
                                    // Not able to modify the icon once dragging is turned on
                                    //                        mouseover: pointMouseover,
                                    //                        mouseout: pointMouseout
                                    dragend: function (e) {
                                        console.log(e.target.getLatLng());
                                        e.target.setLatLng(e.target.getLatLng()).update();
                                        checkNewNode(e.target.getLatLng());
                                    }
                                });
                                return marker;
                            },
                            onEachFeature: function (feature, layer) {
                                if (feature.geometry.type === 'LineString') {
                                    layer.setStyle({
                                        color: 'green',
                                        opacity: 0.5,
                                        weight: 1
                                    });
                                }
                                if (feature.properties.name === 'Proposed') {
                                    layer.setStyle({
                                        color: '#7F7',
                                        opacity: 0.8,
                                        weight: 14
                                    });
                                    layer.on('click', function(e) {
                                        console.log("Selecting the recommended Segment");
                                        locResource.confirm({}, function (confirmResponse) {
                                            var response = {};
                                            angular.extend(response, confirmResponse);
                                            if (response.status === 'OK') {
//	                                    alert("Changes accepted");
                                                NetworkRefresh.refresh();
                                            } else {
                                                alert("Sorry, problem on the server");
                                            }
                                        });
                                    });
//	                        layer.bringToFront();
                                }
                            }
                        }
                    });
                });
            }

        };
    });

    module.directive('crRecSelect', function factory() {
        return {
            templateUrl: 'js/recs/recSelect.html'
        }
    });

}(window.angular));