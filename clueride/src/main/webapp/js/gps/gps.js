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
            // TODO: The cancel and the OK are the same and meaningless
            gpsMode.agreeToTether = gpsMode.previousAgreeToTether;
            localModel.gameStateService.updateGpsState(gpsMode.agreeToTether);
        }

        $scope.tetherOK = function () {
            gpsMode.agreeToTether = gpsMode.previousAgreeToTether;
            localModel.gameStateService.updateGpsState(gpsMode.agreeToTether);
        }

    }

}(window.angular));