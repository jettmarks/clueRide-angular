'use strict';

var LocModule = angular.module('crNetEdit.LocModule');

LocModule
    .factory('LocResource', function ($resource) {
        return $resource('/crMain/rest/locations/new', {}, {
            request: {
                method: 'POST',
                params: {},
                isArray: false
            },
            confirm: {
                method: 'PUT',
                params: {},
                isArray: false
            }
        });
    });