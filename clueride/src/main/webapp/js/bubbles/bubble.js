(function (angular) {
    'use strict';

    var counter = 0,
        local = {};

    angular
        .module('crPlayer.Bubble', ['crPlayer.GameState'])
        .controller('BubbleController', BubbleController)
        .directive('crBubble', BubbleDirective);

    BubbleController.$inject = ['$scope', 'GameStateService'];

    function BubbleController($scope, GameStateService) {
        local.gsSvc = GameStateService;

        counter++;
        window.console.log("Scope:" + $scope.$id + " Pass:" + counter );
    }

    function BubbleDirective($rootScope, $location) {

        function bubbleClicked(bid) {
            var bubble = local.gsSvc.currentGameState()[bid],
                dialogFlag = !!bubble.dialog,
                pageFlag = !!bubble.nextView;

            // TODO: There may be a less brittle way to do this CA-130
            if (bubble.title === 'Next Stop') {
                local.gsSvc.setHistoryLocation(local.gsSvc.maxVisibleLocationIndex());
            }

            /* Transition based on the Bubble's data. */
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
