(function (angular) {
    'use strict';

    var locResource,
        networkRefresh,
        viewModel,
        network;

    angular
        .module('crNetEdit.recs', ['crNetEdit.LocModule','ui.bootstrap'])
        .controller('RecSelectController', RecSelectController)
        .service('newNodeService', newNodeService)
        .directive('crRecSelect', crRecSelect)
    ;

    RecSelectController.$inject = [ '$scope', 'LocResource', 'NetworkRefresh', 'newNodeService'];

    function RecSelectController ($scope, LocResource, NetworkRefresh, newNodeService) {
        var vm = this;

        vm.recs = [];
        vm.selectedRec = {
            id: 0,
            name: 'Click on Map to get Recs'
        };
        vm.select = select;
        viewModel = vm;
        network = $scope.$parent.network;

        locResource = LocResource;
        networkRefresh = NetworkRefresh;

        function select(rec) {
            vm.selectedRec = rec;
            newNodeService.requestRec(rec ? rec.id : 0);
        }
    }

    /*
     * Handling of the Recommendation back from newNode request
     * for all features, not just the points.  This drops a thin
     * line for the tracks that are part of the proposal, and a thick
     * line for the portions which are "Proposed".  Clicking
     * on the layer is what sends back a "confirm" message to the server */
    function perFeature (feature, layer) {
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
            layer.on('click', function() {
                console.log("Selecting the recommended Segment");
                locResource.confirm({
                    recId: viewModel.selectedRec.id
                }, function (confirmResponse) {
                    var response = {};
                    angular.extend(response, confirmResponse);
                    if (response.status === 'OK') {
                        /* Retrieve updated network once we get a response
                             * to our confirmation request. */
                        networkRefresh.refresh();
                    } else {
                        alert("Sorry, problem on the server");
                    }
                });
            });
//	        layer.bringToFront();
        }
    }

    function featuresToMap(pointFeature) {
        /* Our extension of the local network. */
        angular.extend(network, {
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
                onEachFeature: perFeature
            }
        });
    }

    function requestRec (recId) {
        locResource.request({
            recId: recId
        }, featuresToMap )
    }

    function populateDropdown(networkProposal) {
        var rec;
        // Clear out previous set of choices
        viewModel.recs.splice(0);
        switch(networkProposal.type) {
            case 'ON_MULTI_TRACK':
            case 'ON_SINGLE_TRACK':
                /* Send relevant rec list over to Rec Selection code. */
                angular.extend(viewModel.recs, networkProposal.recs);
                rec = networkProposal.recs[0];
                break;

            case 'OFF_NETWORK':
                rec = {
                    id: 0,
                    name: 'Off Network'
                };
                break;

            case 'UNDEFINED':
            default:
                rec = {
                    id: 0,
                    name: 'Undefined'
                };
                break;
        }
        //viewModel.select(networkProposal.recs[0]);
        viewModel.select(rec);
    }

    function checkNewNode(latlng) {
        locResource.check({
            lat: latlng.lat,
            lon: latlng.lng
        }, populateDropdown)
    }
    /*
     * Called to evaluate a lat/lon pair for connection to network.
     */
    function newNodeService () {
        return {
            requestRec: requestRec,
            checkNode: checkNewNode
        };
    }

    /*
     * Directive definition.
     */
    function crRecSelect() {
        return {
            transclude: true,
            scope: {
                network: '='
            },
            templateUrl: 'js/recs/recSelect.html'
        }
    }

}(window.angular));