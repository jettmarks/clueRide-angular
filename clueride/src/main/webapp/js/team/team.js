(function (angular) {
    'use strict';

    var module;

    module = angular.module('team', ['ngResource']);

    module.factory('TeamResource', function ($resource) {
        return $resource('/player/rest/team', {}, {
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
                $scope.team = team;
            });
        }
    ]);

}(window.angular));
