(function (angular) {
    'use strict';

    var module = angular.module('status', []);

    module.controller('StatusController',
        function($scope) {
            $scope.currentPage = 3;
            $scope.totalItems = 5;
        }
    );

}(window.angular));
