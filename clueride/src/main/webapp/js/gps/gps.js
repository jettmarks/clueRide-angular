(function (angular) {
    'use strict';

    var module = angular.module('gps', []),
        gpsMode;

    module.controller('GpsController', ['$scope',
        function ($scope) {
            gpsMode = {
                hasGps: false,
                agreeToTether: false
            }

            $scope.gpsMode = gpsMode;
            gpsMode.hasGps = navigator.geolocation;
        }
    ]);

}(window.angular));