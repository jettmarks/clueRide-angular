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
            }
        });
    }

}(window.angular));