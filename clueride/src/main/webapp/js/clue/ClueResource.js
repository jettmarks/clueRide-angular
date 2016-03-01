(function (angular) {
    'use strict';

    angular
        .module('common.ClueResource', ['ngResource'])
        .factory('ClueResource', ClueResource)
    ;

    function ClueResource($resource) {
        return $resource('/rest/clue', {}, {
            get: {
                method: 'GET',
                url: '/rest/clue/:clueId',
                params: {clueId: '@clueId'},
                isArray: false
            },
            query: {
                method: 'GET',
                url: '/rest/clue/location/:locId',
                params: {locId: '@locId'},
                isArray: true
            },
            create: {
                method: 'POST',
                isArray: false
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