'use strict';

/* Services */

angular.module('crMain.services', ['ngResource']);

angular.module('crMain.services').factory('Network', function ($resource) {
    return $resource('/crMain/rest/network', {}, {
        query: {
            method: 'GET',
            params: {},
            isArray: false
        }
    })
});

angular.module('crMain.services').factory('RawSegments', function ($resource) {
    return $resource('/crMain/rest/segments/raw', {}, {
        query: {
            method: 'GET',
            params: {},
            isArray: false
        }
    })
});

//angular.module('crMain.services').factory('Locations', function ($resource) {
//	return $resource('/crMain/rest/locations/new?lat=:lat&lon=:lon', {}, {
//		request: {
//			method: 'GET',
//			params: {},
//			isArray: false
//		},
//		confirm: {
//		    method: 'GET',
//		    params: {},
//		    isArray: false
//		}
//	})
//});