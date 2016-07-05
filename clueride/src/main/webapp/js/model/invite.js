(function (angular) {
    'use strict';

    /** Invitation Model / Resource. */

    var module = angular.module(
        'crPlayer.invite',
        ['ngResource']
    );

    function InviteResource($resource) {
        return $resource('/rest/invite/:token',
            {},
            {
                get: {
                    method: 'GET',
                    isArray: false
                }
            });
    }

    InviteResource.$inject = ['$resource'];

    module.factory(
        'InviteResource',
        InviteResource
    );

}(window.angular));
