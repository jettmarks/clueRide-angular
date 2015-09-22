var crNetEdit = angular.module("crNetEdit", [
        'leaflet-directive', 
        'crMain.services',
        'crNetEdit.LocGroupModule',
        'crNetEdit.LocModule',
        'where',
        'network'
        ]);

crNetEdit.controller("AppController", [ 
		"$scope", 
        'leafletData', 
        '$http', 
        'RawSegments', 
        'LocResource', 
        'NetworkResource',
        'NetworkRefresh',
        function($scope, leafletData, $http, RawSegments, LocResource, 
                NetworkResource, NetworkRefresh
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
		}
    });
    
    // Bind the scope's segments with the service's segments
    NetworkRefresh.refresh();
    $scope.gjNetwork.segments = NetworkRefresh.segments();
    $scope.selectedSegment = NetworkRefresh.selectedSegment();
    
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

    // TODO: Move this into the LocModule
    function checkNewPointLocation(latlng) {
        LocResource.request({
            lat: latlng.lat,
            lon: latlng.lng
        }, function (pointFeature) {
            angular.extend($scope.gjNetwork, {
                newPoints: {
                    data: pointFeature,
	                pointToLayer: function (feature, latlng) {
	                    marker = new L.marker(latlng, {
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
                                  checkNewPointLocation(e.target.getLatLng());
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
	                            LocResource.confirm({}, function (confirmResponse) {
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

    
    // TODO: Set this up so we can turn on/off the response (goes in LocModule)
    leafletData.getMap('networkMap').then(function(networkMap) {
        // Responds to mouse-click to submit lat/lon and return point candidate
        networkMap.on('click', function (mouseEvent) {
	        checkNewPointLocation(mouseEvent.latlng);
        });
        networkMap.on('mousemove', function (mouseEvent) {
            $scope.mouse.location = mouseEvent.latlng;
        });
    });

}]);