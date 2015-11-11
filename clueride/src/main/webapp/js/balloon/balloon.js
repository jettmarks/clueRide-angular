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
            require: ['$scope','gameStateService'],
            controller: function ($scope, gameStateService) {
                var gs = gameStateService.currentGameState(),
                    blid = {
                        b1: 'balloon1',
                        b2: 'balloon2',
                        b3: 'balloon3'
                    }[$scope.bid];

                // If we want to see this in scope
//                $scope.gs = gs;

                $scope.balloon = gs[blid];
                $scope.showDialog = {};
                $scope.showPage = {};
                $scope.changeState = {};
                window.console.log("scope("+$scope.$id+")  bid: "+$scope.bid);
            },
            templateUrl: 'js/balloon/balloon.html'
        };
    });

}(window.angular));