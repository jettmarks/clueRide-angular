'use strict';

var LocGroupModule = angular
	.module('LocGroupModule', [
	      'leaflet-directive', 
	      'ngResource'
          ]);

var locationGroups = [],
    activeGroupId = 0,
    editable = true;

    
angular.module('LocGroupModule')
    .factory('LocGroupSvc', function(LocGroupResource, LocGroupUpdater, leafletData) {
        var load = function (isEditable) {
            editable = isEditable;
            locationGroups = [];
            LocGroupResource.get({}, function (featureCollection) {
                featureCollection.features.forEach(function (feature) {
                    var geom = feature.geometry,
                        radius = feature.properties.radius;
                    if (geom.type === 'Point') {
                        var lon = geom.coordinates[0], 
                            lat = geom.coordinates[1],
                            circle = L.circle([lat, lon], radius, {
                                color: '#030',
                                fillColor: '#030',
                                fillOpacity: 0.2,
                            }),
                            locGroup = {
                                circle: circle,
                                id: feature.properties.nodeGroupId
	                        };
                        locationGroups.push(locGroup);
                    }
                });
                addCirclesToMap();
            });     
        };
        
        // Private method at this time; make public by returning it below
        var addCirclesToMap = function () {
		    leafletData.getMap('networkMap').then(function(networkMap) {
		        console.log(locationGroups);
		        locationGroups.forEach(function (locGroup) {
		            locGroup.circle.addTo(networkMap);
		            if (editable) {
		                // Allow Dragging of locGroup per locGroup
	                    locGroup.circle.on({
	                        mousedown: function () {
	                            networkMap.on('mousemove', function (e) {
	                                locGroup.circle.setLatLng(e.latlng);
	                            });
	                            activeGroupId = locGroup.id;
	                        }
                        }); 
		            }
		        });
		        if (editable) {
		            // Turn off mouseup only once
                    networkMap.on('mouseup',function(e){
                        networkMap.removeEventListener('mousemove');
                        if (activeGroupId !== 0) {
                            console.log(activeGroupId+": "+e.latlng);
                            // TODO: Trigger the update of the lat/lon for this guy
                            LocGroupUpdater.set({
                                id: activeGroupId,
                                lat: e.latlng.lat,
                                lon: e.latlng.lng
                            });
                            activeGroupId = 0;
                        }
                    });
		        }
		    });
        };

        return {
            load: load,
            editable: function (b) {editable = b;}
        }
        
    });


LocGroupModule.run(function (LocGroupSvc) {
    LocGroupSvc.load(true);
});
