var crNetEdit = angular.module("crNetEdit", [
    'leaflet-directive',
    'crMain.services',
    'crNetEdit.LocGroupModule',
    'crNetEdit.LocModule',
    'where',
    'network',
    'crNetEdit.recs',
    'ui.bootstrap'
]);

crNetEdit.controller("AppController", [ 
    "$scope",
    'leafletData',
    '$http',
    'RawSegments',
    'LocResource',
    'LocDiagResource',
    'NetworkResource',
    'NetworkRefresh',
    'newNodeService',
    function($scope,
             leafletData,
             $http,
             RawSegments,
             LocResource,
             LocDiagResource,
             NetworkResource,
             NetworkRefresh,
             newNodeService
    ) {
        $scope.layers = {
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

        angular.extend($scope, {
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
        NetworkRefresh.refresh();
        $scope.gjNetwork.segments = NetworkRefresh.segments();
        $scope.selectedSegment = NetworkRefresh.selectedSegment();
        $scope.showNodes = LocModule.showNodes($scope,LocDiagResource);
    
    RawSegments.get({}, function(featureCollection) {
		angular.extend($scope.gjTracks, {
		    segments: {
				data: featureCollection,
				style: {
				    opacity: 0.7,
				    color: '#444',
				    weight: 4,
				},
		
				onEachFeature: function (feature, layer) {
		
				    layer.on('mouseover', function(e) {
						e.target.setStyle({
						    weight: 8,
						    opacity: 1.0
						});
				    });
		
				    layer.on('mouseout', function(e) {
						e.target.setStyle({
						    weight: 4,
						    opacity: 0.7
						});
				    });
		
				    layer.on('click', function(e) {
						$scope.selectedFeature = this.feature;
				    });
				} 
		    }
		});
    });

    
    // TODO: Set this up so we can turn on/off the response (goes in LocModule)
    leafletData.getMap('networkMap').then(function(networkMap) {
        // Responds to mouse-click to submit lat/lon and return point candidate
        networkMap.on('click', function (mouseEvent) {
	        newNodeService.checkNode(mouseEvent.latlng);
        });
        networkMap.on('mousemove', function (mouseEvent) {
            $scope.mouse.location = mouseEvent.latlng;
        });
    });

}]);