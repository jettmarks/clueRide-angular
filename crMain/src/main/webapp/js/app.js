(function (angular) {
    "use strict";

    var viewModel;

    angular
        .module('crNetEdit', [
            'leaflet-directive',
            'crMain.services',
            'crNetEdit.LocGroupModule',
            'crNetEdit.NodeAddModule',
            'crNetEdit.NodeEditModule',
            'crNetEdit.EditModeModule',
            'crNetEdit.MapModule',
            'crNetEdit.CourseModule',
            'crNetEdit.Feature',
            'where',
            'network',
            'crNetEdit.recs',
            'ui.bootstrap'
        ])
        .controller('AppController', AppController)
    ;

    AppController.$inject = [
        "$scope",
        'leafletData',
        'leafletMarkerEvents',
        'RawSegments',
        'LocDiagResource',
        'NetworkRefresh',
        'newNodeService',
        'ShowNodesService',
        'MapService'
    ];

    function AppController ($scope,
                            leafletData,
                            leafletMarkerEvents,
                            RawSegments,
                            LocDiagResource,
                            NetworkRefresh,
                            newNodeService,
                            ShowNodesService,
                            MapService
    ) {
        var mapModel = this;

        viewModel = mapModel;

        mapModel.layers = {
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
        };

        // TODO: May be able to pull this back up into the NodeEditModule
        mapModel.editableMarkers = {};

        /* TODO: This probably belongs somewhere else. */
        mapModel.events = {
            markers: {
                enable: leafletMarkerEvents.getAvailableEvents()
            }
        };

        angular.extend(mapModel, {
            center: {
                lat: 33.7627,
                lng: -84.3527,
                zoom: 12,
                autoDiscover: true
            },
            gjNetwork: {},
            gjTracks: {},
            selectedFeature: {},
            selectedSegment: {},
            circles: {},
            mouse: {
                location: {
                    lat: 33.0,
                    lng: -84.0
                }
            },
            showNodes: {}
        });

        // Bind the scope's segments with the service's segments
        MapService.setMapScope($scope);
        NetworkRefresh.refresh();
        mapModel.gjNetwork.segments = NetworkRefresh.segments();
        mapModel.selectedSegment = NetworkRefresh.selectedSegment();

        //mapModel.showNodes = ShowNodesService.showNodes();

        RawSegments.get({}, loadTracks);

        // TODO: CA-86 Set this up so we can turn on/off the response (goes in LocModule)
        leafletData.getMap('networkMap').then(function (networkMap) {
            // Responds to mouse-click to submit lat/lon and return point candidate
            networkMap.on('click', function (mouseEvent) {
                newNodeService.checkNode(mouseEvent.latlng);
            });
            networkMap.on('mousemove', function (mouseEvent) {
                mapModel.mouse.location = mouseEvent.latlng;
            });
        });
    }

    function loadTracks(featureCollection) {
        angular.extend(viewModel.gjTracks, {
            segments: {
                data: featureCollection,
                style: {
                    opacity: 0.7,
                    color: '#444',
                    weight: 4,
                },
                onEachFeature: onEachTrackFeature
            }
        });
    }

    function onEachTrackFeature(feature, layer) {
        layer.on('mouseover', function (e) {
            e.target.setStyle({
                weight: 8,
                opacity: 1.0
            });
        });

        layer.on('mouseout', function (e) {
            e.target.setStyle({
                weight: 4,
                opacity: 0.7
            });
        });

        layer.on('click', function (e) {
            viewModel.selectedFeature = feature;
        });
    }

}(window.angular));
