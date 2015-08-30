var app = angular.module("crMain", ['leaflet-directive', 'crMain.services']);
app.controller("BasicFirstController", [ "$scope", 'leafletData', 
                                         '$http', 'RawSegments', 'Locations',
                                         function($scope, leafletData, $http, RawSegments, Locations) {

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
		geojson: {},
		selectedFeature: {}
    });

    RawSegments.get({}, function(featureCollection) {
		angular.extend($scope.geojson, {
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

    $http.get("/crMain/tempJson/singleLineSegments.geojson").success(function(data, status) {
		angular.extend($scope.geojson, {
		    problems: {
				data: data,
				style: {
				    opacity: 0.7,
				    color: 'red',
				    weight: 18
				},
		
				onEachFeature: function (feature, layer) {
		
				    layer.on('mouseover', function(e) {
						e.target.setStyle({
						    weight: 24,
						    opacity: 1.0
						});
						e.target.bringToFront();
				    });
		
				    layer.on('mouseout', function(e) {
						e.target.setStyle({
						    weight: 18,
						    opacity: 0.7
						});
						e.target.bringToFront();
				    });
		
				    layer.on('click', function(e) {
						$scope.selectedFeature = this.feature;
				    });
				} 
		    }
		});
    });

    function checkNewPointLocation(latlng) {
        Locations.get({
            lat: latlng.lat,
            lon: latlng.lng
        }, function(pointFeature) {
            angular.extend($scope.geojson, {
                newPoints: {
                    data: pointFeature,
	                pointToLayer: function(feature, latlng) {
	                    marker = new L.marker(latlng, {
	                        icon: getMarkerIcon(feature.properties.state),
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
    
    // Responds to mouse-click to submit lat/lon and return point candidate
    leafletData.getMap().then(function(map) {
        map.on('click', function(mouseEvent) {
	        checkNewPointLocation(mouseEvent.latlng);
        });
    });

}]);