(function (angular) {
    'use strict';

    angular
        .module('common.ClueResource', ['ngResource'])
        .factory('ClueResource', ClueResource)
    ;

    function ClueResource($resource) {
        return $resource('/rest/clue/:clueId', {clueId: '@clueId'}, {
            get: {
                method: 'GET',
                isArray: false
            },
            query: {
                method: 'GET',
                url: '/rest/clue/location/:locId',
                params: {locId: '@locId'},
                isArray: true
            },
            remove: {
                method: 'DELETE',
                url: '/rest/clue/:locId/:clueId',
                params: {
                    locId: '@locId',
                    clueId: '@clueId'
                }
            }
        });
    }

}(window.angular));