'use strict';

var module = angular.module('network');

module
    .factory('NetworkResource', function ($resource) {
        return $resource('/crMain/rest/network', {}, {
            request: {
                method: 'GET',
                params: {},
                isArray: false
            }
        });
    });

module
    .factory('NetworkRefresh', function(NetworkResource) {
        var segments = {},
            selectedSegment = {};
	    
        return {
            refresh: function () {
                NetworkResource.get(
                    {} , function (networkFeatureCollection) {
				        angular.extend(segments, {
				            data: networkFeatureCollection,
				            style: {
				                opacity: 0.7,
				                color: '#030',
				                weight: 4
				            },
				            onEachFeature: function (feature, layer) {
				                layer.on('click', function (e) {
				                    angular.extend(selectedSegment, this.feature);
				                });
				            }
				        });
				    }
                );
            },
            segments: function() {return segments},
            selectedSegment: function () {return selectedSegment}
        };
    });
