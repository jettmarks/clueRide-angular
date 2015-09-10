var app = angular.module("crMain", ['leaflet-directive', 'crMain.services', 'LocGroupModule']);

app.controller("AppController", [ 
		"$scope", 
        'leafletData', 
        '$http', 
        'RawSegments', 
        'Locations', 
        'Network', 
        function($scope, leafletData, $http, RawSegments, Locations, 
                Network 
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
		circles: {}
    });

    Network.get({}, function(featureCollection) {
		angular.extend($scope.gjNetwork, {
		    segments: {
				data: featureCollection,
				style: {
				    opacity: 0.7,
				    color: '#030',
				    weight: 4,
				}
		    }
		});
    });
				
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

//    $http.get("/crMain/tempJson/singleLineSegments.geojson").success(function(data, status) {
//		angular.extend($scope.gjTracks, {
//		    problems: {
//				data: data,
//				style: {
//				    opacity: 0.7,
//				    color: 'red',
//				    weight: 18
//				},
//		
//				onEachFeature: function (feature, layer) {
//		
//				    layer.on('mouseover', function(e) {
//						e.target.setStyle({
//						    weight: 24,
//						    opacity: 1.0
//						});
//						e.target.bringToFront();
//				    });
//		
//				    layer.on('mouseout', function(e) {
//						e.target.setStyle({
//						    weight: 18,
//						    opacity: 0.7
//						});
//						e.target.bringToFront();
//				    });
//		
//				    layer.on('click', function(e) {
//						$scope.selectedFeature = this.feature;
//				    });
//				} 
//		    }
//		});
//    });
    
    function checkNewPointLocation(latlng) {
        Locations.get({
            lat: latlng.lat,
            lon: latlng.lng
        }, function(pointFeature) {
            angular.extend($scope.gjNetwork, {
                newPoints: {
                    data: pointFeature,
	                pointToLayer: function(feature, latlng) {
	                    marker = new L.marker(latlng, {
	                        icon: getMarkerIcon(feature.properties.state, feature.properties.selected),
	                        draggable: (feature.properties.name === 'candidate')
	                    });
		                marker.on({
	                    // Not able to modify the icon once dragging is turned on
	//                        mouseover: pointMouseover,
	//                        mouseout: pointMouseout
	                          dragend: function(e) {
	                              console.log(e.target.getLatLng());
                                  e.target.setLatLng(e.target.getLatLng()).update();
                                  checkNewPointLocation(e.target.getLatLng());
	                          }
		                });
	                    return marker;
	                }
                }
            });
        });
    }

    
    leafletData.getMap('networkMap').then(function(networkMap) {
        // Responds to mouse-click to submit lat/lon and return point candidate
        networkMap.on('click', function (mouseEvent) {
	        checkNewPointLocation(mouseEvent.latlng);
        });
    });

}]);