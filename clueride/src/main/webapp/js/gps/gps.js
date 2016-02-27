(function (angular) {
    'use strict';

    var localModel = {},
        gpsMode = {
            hasGps: false,
            agreeToTether: false,
            previousAgreeToTether: false
        };

    angular
        .module('crPlayer.Gps', ['crPlayer.GameState'])
        .controller('GpsController', GpsController)
        ;

    GpsController.$inject = ['$scope', 'GameStateService'];

    function GpsController($scope, GameStateService) {
        localModel.gameStateService = GameStateService;

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