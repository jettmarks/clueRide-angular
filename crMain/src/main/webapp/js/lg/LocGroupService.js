'use strict';

angular.module('crNetEdit.LocGroupModule')
	.factory('LocGroupResource', function ($resource) {
	    return $resource('/crMain/rest/nodes/groups', {}, {
	        get: {
	            method: 'GET',
	            params: {},
	            isArray: false
	        }
	    });
	});

angular.module('crNetEdit.LocGroupModule')
.factory('LocGroupUpdater', function ($resource) {
    return $resource('/crMain/rest/nodes/group/set?id=:id&lat=:lat&lon=:lon', {}, {
        set: {
            method: 'GET',
            params: {},
            isArray: false
        }
    });
});