'use strict';

angular.module('LocGroupModule')
	.factory('LocGroupResource', function ($resource) {
	    return $resource('/crMain/rest/locations/groups', {}, {
	        get: {
	            method: 'GET',
	            params: {},
	            isArray: false
	        }
	    })
	});