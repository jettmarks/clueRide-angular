'use strict';

/* Services */

angular.module('crMain.services', ['ngResource']);

angular.module('crMain.services').factory('RawSegments', function ($resource) {
    return $resource('/crMain/rest/segments/raw', {}, {
        query: {
            method: 'GET',
            params: {},
            isArray: false
        }
    })
});