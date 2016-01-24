(function (angular) {
    'use strict';

    var counter = 0,
        local = {};

    angular
        .module('crPlayer.Bubble', ['gameState'])
        .controller('BubbleController', BubbleController)
        .directive('crBubble', BubbleDirective);

    BubbleController.$inject = ['$scope', 'gameStateService'];

    function BubbleController($scope, gameStateService) {
        local.gsSvc = gameStateService;

        counter++;
        window.console.log("Scope:" + $scope.$id + " Pass:" + counter );
    }

    function BubbleDirective($rootScope, $location) {
        function bubbleClicked(bid) {
            var bubble = local.gsSvc.currentGameState()[bid],
                dialogFlag = !!bubble.dialog,
                pageFlag = !!bubble.nextView;

            if (dialogFlag) {
                $rootScope.Ui.turnOn(bubble.dialog);
            } else if (pageFlag) {
                $location.path(bubble.nextView);
            } else {
                local.gsSvc.updateGameState(bubble.nextState);
            }
        }

        return {
            scope: {
                bid: '@',
                // Have to use this for the two way binding?
                bubble: '='
            },
            link: {
                pre: function ($scope, element, attrs) {
                    // Thought that this could provide two-way binding
                    //$scope.bubble = local.gsSvc.currentGameState()[attrs.bid];
                },
                post: function ($scope) {
                    $scope.bubbleClicked = bubbleClicked;
                }
            },
            templateUrl: 'js/bubbles/bubble.html'
        }
    }


}(window.angular));
