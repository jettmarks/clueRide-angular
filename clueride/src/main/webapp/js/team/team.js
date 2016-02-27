(function (angular) {
    'use strict';

    var module;

    module = angular.module('crPlayer.Team', ['ngResource']);

    module.factory('TeamResource', function ($resource) {
        return $resource('/rest/team', {}, {
            get: {
                method: 'GET',
                params: {},
                isArray: false
            }
        })
    });

    module.controller('TeamController', ['$scope','TeamResource',
        function ($scope, TeamResource) {
            TeamResource.get({}, function (team) {
                $scope.crPlayerTeam = team;
            });
        }
    ]);

}(window.angular));
