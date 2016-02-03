(function (angular) {
    'use strict';

    angular
        .module('crPlayer.Path', ['ngResource'])
        .factory('PathMapResource', PathMapResource)
    ;

    function PathMapResource ($resource) {
        return $resource('/rest/path/map', {}, {
            getMap: {
                method: 'GET',
                params: {},
                isArray: false
            }
        });
    }

}(window.angular));