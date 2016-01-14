(function (angular) {
    'use strict';

    var editMarkers,
        viewModel,
        mapScope = {},
        subjectMarker,
        nodeSegmentMatchResource = {showConnectingEdges: function () {}},
        nodeUpdateResource = {updateNode: function () {}},
        locDiagResource = {showAllNodes: function () {}};

    angular
        .module('crNetEdit.NodeEditModule', ['crNetEdit.MapModule','leaflet-directive', 'ngResource'])
        .controller('NodeEditController', NodeEditController)
        .factory('LocDiagResource', NodeDiagResource)
        .factory('NodeSegmentMatchResource', NodeSegmentMatchResource)
        .factory('NodeUpdateResource', NodeUpdateResource)
        .service('ShowNodesService', ShowNodesService)
        .directive('crShowNodes', showNodesDirective)
    ;

    NodeEditController.$inject = [
        '$scope',
        'LocDiagResource',
        'NodeSegmentMatchResource',
        'NodeUpdateResource',
        'MapService'
    ];

    function NodeEditController (
        $scope,
        LocDiagResource,
        NodeSegmentMatchResource,
        NodeUpdateResource,
        MapService
    ) {
        var vm = this;

        $scope.nodeModule = {name: "NodeEditModule"};
        $scope.showNodesForEditing = showNodesForEditing;
        $scope.acceptEdit = acceptEdit;
        $scope.cancelEdit = cancelEdit;

        $scope.vm = vm;

        vm.editStatus = {
            editInProgress: false,
            editPointId: {},
            editPointLatLng: {},
            matchingEdgeIds: []
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
        nodeSegmentMatchResource = NodeSegmentMatchResource;
        nodeUpdateResource = NodeUpdateResource;
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

    /**
     * Updates an existing Node with a new Lat/Lon pair and brings back
     * the updated Edges.  Once user accepts the new location and Edges,
     * confirms the node update.
     * @param $resource
     * @constructor
     */
    function NodeUpdateResource ($resource) {
        return $resource('/crMain/rest/nodes/update', {}, {
            updateNode: {
                method: 'GET',
                params: {},
                isArray: false
            },
            confirmNode: {
                method: 'PUT',
                params: {},
                isArray: false
            }
        });
    }

    /**
     * Retrieves the segments (as Feature Collection) matching
     * the given Node's Point ID.
     * @param $resource
     * @returns {FeatureCollection}
     * @constructor
     */
    function NodeSegmentMatchResource ($resource) {
        return $resource('/crMain/rest/nodes/segment', {}, {
            getMatchingSegments: {
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

        // Service request to redraw affected edges based on node's new location
        nodeUpdateResource.updateNode({
            pointId: viewModel.editStatus.editPointId,
            lat: args.model.lat,
            lng: args.model.lng
        }, addMatchingEdgeToMap );

        // TODO: This is the wrong one (CA-96)
        //viewModel.editStatus.editPointLatLng = {
        //    lat: args.model.lat,
        //    lng: args.model.lng
        //}
    }

    /**
     * Node to edit has been selected for editing, so this function takes the target
     * marker, sets it off to the side and replaces it (same lat/lng) with another
     * marker that we let the user move around on the map.
     * If another marker had already been selected without accept/cancel, we swap out
     * the previous marker.
     * @param e
     */
    function makeNodeEditable(e) {
        var editMarker,
            markerPointId;

        /* Check if another marker had already been selected for edit. */
        if (viewModel.editStatus.editInProgress) {
            /* We have a marker out there; Change which marker is being edited. */
            subjectMarker.setOpacity(1.0);
            setFeatureLocation(subjectMarker.feature.properties.pointId,
                viewModel.editStatus.editPointLatLng.lat,
                viewModel.editStatus.editPointLatLng.lng
            );
        }
        /* Record details of the node being selected for edit. */
        subjectMarker = e.target;
        markerPointId = subjectMarker.feature.properties.pointId;
        /* Temporarily remove the subjectMarker by changing its lat/lon in the Feature List */
        hideTargetMarker(markerPointId);

        /* Begin editing with a brand new marker. */
        mapScope.$on('leafletDirectiveMarker.dragend', function (event, args) {
            updateEditedMarker(event, args);
        });

        console.log("Editing " + markerPointId);
        subjectMarker.setOpacity(0.0);
        //e.target.setOpacity(0.0);
        //subjectMarker.options.opacity = 0.0;
        editMarker = getEditableIcon(e.target.getLatLng());
        editMarker.draggable = true;
        angular.extend(editMarkers, {
            em: editMarker
        });

        viewModel.editStatus.editInProgress = true;
        viewModel.updateEditStatus({
            editInProgress: true,
            editPointId: markerPointId,
            editPointLatLng: subjectMarker.getLatLng(),
            matchingEdgeIds: []
        });
        showConnectingEdges(markerPointId);
    }

    function hideTargetMarker (pointId) {
        setFeatureLocation(pointId, 0.0, 0.0);
    }

    function setFeatureLocation(pointId, lat, lng) {
        var featureList = mapScope.mapModel.gjNetwork.newPoints.data.features;

        for (var feature in featureList) {
            if (!!featureList[feature].properties && !!featureList[feature].properties.pointId) {
                if (featureList[feature].properties.pointId === pointId) {
                    featureList[feature].geometry.coordinates[0] = lng;
                    featureList[feature].geometry.coordinates[1] = lat;
                }
            }
        }
    }

    function ShowNodesService () {
        return {
            showNodes: showNodesForEditing
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

    function showConnectingEdges (pointId) {
        nodeSegmentMatchResource.getMatchingSegments( {
            pointId: pointId
        }, addMatchingEdgeToMap);
    }

    function addMatchingEdgeToMap (lineFeature) {
        var mapModel = mapScope.mapModel;
        /* Want to clear after the promise is resolved, but before re-populating. */
        viewModel.editStatus.matchingEdgeIds = [];
        angular.extend(mapModel.gjNetwork, {
            matchingEdges: {
                data: lineFeature,
                style: {
                    color: '#FF7',
                    opacity: 0.8,
                    weight: 14
                },
                onEachFeature: captureEdgeId
            }
        });
    }

    function captureEdgeId (feature, layer) {
        viewModel.editStatus.matchingEdgeIds.push(feature.properties.edgeId);
    }

}(window.angular));
