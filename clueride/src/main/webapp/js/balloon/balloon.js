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
                bid: '@'
            },
            require: [
                '$scope',
                '$rootScope',
                '$location',
                'gameStateService'
            ],
            controller: function (
                $scope,
                $rootScope,
                $location,
                gameStateService
            ) {
                var gs = gameStateService.currentGameState(),
                    unbind = {},
                    bid = $scope.bid,
                    balloon = {},
                    dialogFlag = false,
                    pageFlag = false,
                    nextState = 'beginPlay';

                $scope.balloon = gs[$scope.bid];
                window.console.log("scope("+$scope.$id+")  bid: "+$scope.bid);
                evalFlags();

                $scope.balloonClicked = function () {
                    if (dialogFlag) {
                        $rootScope.Ui.turnOn($scope.balloon.dialog);
                    } else if (pageFlag) {
                        $location.path($scope.balloon.nextView);
                    } else {
                        gameStateService.updateGameState($scope.balloon.nextState);
                        $scope.$emit('gameStateChanged');
                    }
                };

                function evalFlags() {
                    balloon = $scope.balloon;
                    dialogFlag = !!balloon.dialog;
                    pageFlag = !!balloon.nextView;
                    nextState = balloon.nextState;
                };

                unbind = $scope.$on('updateGameState', function () {
                    $scope.balloon = gameStateService.currentGameState()[$scope.bid];
                    evalFlags();
                });

                // Clean up resources
                $scope.$on('$destroy', unbind);
            },
            templateUrl: 'js/balloon/balloon.html'
        };
    });

}(window.angular));