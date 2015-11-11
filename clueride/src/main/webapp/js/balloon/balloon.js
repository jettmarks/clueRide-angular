(function (angular) {
    'use strict';

    var module = angular.module('balloon', []),
        counter = 0,
        gsSvc;

    module.controller('BalloonController', ['$scope','$rootScope','gameStateService',
        function($scope, $rootScope, gameStateService) {
            $scope.currentGameState = gameStateService.currentGameState();
            $scope.setGameState = function (stateName) {
                gameStateService.updateGameState(stateName);
                $scope.currentGameState = gameStateService.currentGameState();
                $rootScope.$broadcast('gameStateChanged');
            }
            gsSvc = gameStateService.updateGameState;

            counter++;
            window.console.log("Scope:"+$scope.$id+" Pass:"+counter+" - "+$scope.setGameState);
        }
    ]);

    module.directive('crBalloon', function() {
        return {
            scope: {
                balloon: '='
            },
            templateUrl: 'js/balloon/balloon.html'
        };
    });

}(window.angular));