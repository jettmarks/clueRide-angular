(function (angular) {
    'use strict';

    var localModel = {},
        gpsMode = {
            hasGps: false,
            agreeToTether: false,
            previousAgreeToTether: false
        };

    angular
        .module('gps', ['crPlayer.GameState'])
        .controller('GpsController', GpsController)
        ;

    GpsController.$inject = ['$scope', 'gameStateService'];

    function GpsController($scope, gameStateService) {
        localModel.gameStateService = gameStateService;

        $scope.gpsMode = gpsMode;
        gpsMode.previousAgreeToTether = gpsMode.agreeToTether;
        gpsMode.hasGps = navigator.geolocation;

        $scope.tetherCancel = function () {
            gpsMode.agreeToTether = gpsMode.previousAgreeToTether;
            localModel.gameStateService.enablePlay();
        }

        $scope.tetherOK = function () {
            gpsMode.agreeToTether = gpsMode.previousAgreeToTether;
            localModel.gameStateService.enablePlay();
        }

    }

}(window.angular));