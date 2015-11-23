(function (angular) {
    'use strict';

    var module = angular.module('gps', []),
        gpsMode = {
            hasGps: false,
            agreeToTether: false,
            previousAgreeToTether: false
        };

    module.controller('GpsController', ['$scope',
        function ($scope) {
            $scope.gpsMode = gpsMode;
            gpsMode.previousAgreeToTether = gpsMode.agreeToTether;
            gpsMode.hasGps = navigator.geolocation;

            $scope.tetherCancel = function () {
                gpsMode.agreeToTether = gpsMode.previousAgreeToTether;
            }
        }
    ]);

}(window.angular));