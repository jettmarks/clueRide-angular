'use strict';

/* Services */

var services = angular.module('crMain.services', ['ngResource']);

services.factory('RawSegments', function ($resource) {
    return $resource('/crMain/rest/segments/raw', {}, {
        query: {
            method: 'GET',
            params: {},
            isArray: false
        }
    })
});