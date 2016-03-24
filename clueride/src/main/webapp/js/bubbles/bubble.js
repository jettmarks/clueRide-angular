(function (angular) {
    'use strict';

    var local = {};

    angular
        .module('crPlayer.Bubble', ['crPlayer.GameState'])
        .directive('crBubble', BubbleDirective)
        .run(init)
    ;

    function init(GameStateService) {
        local.gsSvc = GameStateService;
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
            // TODO: Consider moving this over to BindAsController
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
