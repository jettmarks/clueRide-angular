(function (angular) {
    'use strict';

    var module;

    module = angular.module('crPlayer.Team', ['ngResource']);

    module.factory('TeamResource', function ($resource) {
        return $resource('/rest/team', {}, {
            get: {
                method: 'GET',
                url: '/rest/team/:teamId',
                params: {teamId: '@teamId'},
                isArray: false
            },
            create: {
                method: 'POST',
                url: '/rest/team/:teamId',
                params: {teamId: '@teamId'},
                isArray: false
            }
        })
    });

}(window.angular));
