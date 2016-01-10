(function (angular) {
    'use strict';

    var editMarkers,
        viewModel,
        mapScope = {},
        subjectMarker,
        locDiagResource = {showAllNodes: function () {}};

    angular
        .module('crNetEdit.NodeEditModule', ['crNetEdit.MapModule','leaflet-directive', 'ngResource'])
        .controller('NodeEditController', NodeEditController)
        .factory('LocDiagResource', NodeDiagResource)
        .service('ShowNodesService', ShowNodesService)
        .directive('crShowNodes', showNodesDirective)
    ;

    NodeEditController.$inject = ['$scope','LocDiagResource','MapService'];

    function NodeEditController ($scope, LocDiagResource, MapService) {
        var vm = this;

        $scope.nodeModule = {name: "NodeEditModule"};
        $scope.showNodesForEditing = showNodesForEditing;
        $scope.acceptEdit = acceptEdit;

        $scope.vm = vm;

        vm.editStatus = {
            editInProgress: false,
            editPointId: {},
            editPointLatLng: {}
        }
        vm.updateEditStatus = updateEditStatus;
        viewModel = vm;     // For reading, but not writing

        mapScope = MapService.getMapScope();

        // Store external scope found in parent
        editMarkers = $scope.$parent.editMarkers;

        function updateEditStatus(editStatus) {
            vm.editStatus = editStatus;
        }

        locDiagResource = LocDiagResource;
    }

    function NodeDiagResource ($resource) {
        return $resource('/crMain/rest/nodes/allNodes', {}, {
            showAllNodes: {
                method: 'GET',
                params: {},
                isArray: false
            }
        });
    }

    function showNodesDirective () {
        return {
            transclude: true,
            scope: {
                // External Scope found in parent
                editMarkers: '='
            },
            templateUrl: 'js/node/showNodes.html'
        }
    }

    function acceptEdit() {
        cancelEdit();
    }

    function cancelEdit() {
        viewModel.editStatus.editInProgress = false;
        subjectMarker.setOpacity(1.0);
        /* Remove Edit Marker; no longer needed. */
        //mapScope.mapModel.clearEditableMarkers();
        //mapScope.mapModel.editableMarkers = {};
        angular.extend(editMarkers, {em: {}});

        /* Update View Status. */
        viewModel.editStatus.editPointId = {};
        viewModel.editStatus.editPointLatLng = {};
    }

    function updateEditedMarker (event, args) {
        console.log("Edit Location: " + args.model.lat + "," + args.model.lng);
        viewModel.editStatus.editPointLatLng = {
            lat: args.model.lat,
            lng: args.model.lng
        }
    }

    function makeNodeEditable(e) {
        var editMarker,
            markerPointId;

        /* Check if another marker had already been selected for edit. */
        if (viewModel.editStatus.editInProgress) {
            /* We have a marker out there; Change which marker is being edited. */
            subjectMarker.setOpacity(1.0);
        }
        /* Record details of the node being selected for edit. */
        subjectMarker = e.target;
        markerPointId = subjectMarker.feature.properties.pointId;

        /* Begin editing with a brand new marker. */
        mapScope.$on('leafletDirectiveMarker.dragend', function (event, args) {
            updateEditedMarker(event, args);
        });

        console.log("Editing " + markerPointId);
        subjectMarker.setOpacity(0);
        editMarker = getEditableIcon(e.target.getLatLng());
        editMarker.draggable = true;
        angular.extend(editMarkers, {
            em: editMarker
        });

        viewModel.editStatus.editInProgress = true;
        viewModel.updateEditStatus({
            editInProgress: true,
            editPointId: markerPointId,
            editPointLatLng: subjectMarker.getLatLng()
        });
    }

    function ShowNodesService () {
        return {
            showNodes: showNodes
        }
    }

    // TODO: Find a different way to bring in the desired scope for listening to events
    function showNodes(scope) {
        var mapModel = scope.mapModel;
        // Record our map's scope for later use
        mapScope = scope;
        showNodesForEditing();
    }

    function showNodesForEditing () {
        var mapModel = mapScope.mapModel;
        locDiagResource.showAllNodes( {},
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
                                draggable: false,
                                message: feature.properties.pointId
                            });
                            marker.on({
                                click: makeNodeEditable
                            });
                            return marker;
                        }
                    }
                })
            }
        );
    }

}(window.angular));
