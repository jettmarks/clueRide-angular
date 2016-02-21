(function (angular) {
    'use strict';

    angular
        .module('common.LocationResource', ['ngResource'])
        .factory('LocationNearestResource', LocationNearestResource)
        .factory('LocationResource', LocationResource)
        .factory('LocationTypeResource', LocationTypeResource)
    ;

    function LocationResource($resource) {
        return $resource('/rest/location/update',
            /* Selected Location */
            {},
            {
                save: {
                    method: 'POST',
                    isArray: false
                }
            }
        );
    }

    function LocationTypeResource($resource) {
        return $resource('/rest/location/types',
            {},
            {
                get: {
                    method: 'GET',
                    isArray: true
                }
            }
        );
    }

    function LocationNearestResource($resource) {
        return $resource('/rest/location/nearest',
            {lat: 34.0, lon: -81.0},
            {
                get: {
                    method: 'GET',
                    isArray: true
                }
            }
        );
    }

}(window.angular));