'use strict';

angular.module('LocGroupModule')
	.factory('LocGroupResource', function ($resource) {
	    return $resource('/crMain/rest/locations/groups', {}, {
	        get: {
	            method: 'GET',
	            params: {},
	            isArray: false
	        }
	    });
	});

angular.module('LocGroupModule')
.factory('LocGroupUpdater', function ($resource) {
    return $resource('/crMain/rest/locations/group/set?id=:id&lat=:lat&lon=:lon', {}, {
        set: {
            method: 'GET',
            params: {},
            isArray: false
        }
    });
});