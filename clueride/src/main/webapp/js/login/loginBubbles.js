(function (angular) {
    'use strict';

    angular
        .module('login.Bubble', [])
        .controller('LoginBubbleController', LoginBubbleController)
        .directive('crBubble', BubbleDirective)
    ;

    LoginBubbleController.$inject = ['$scope'];

    function LoginBubbleController($scope) {
        var vm = $scope.$parent.$parent;

        vm.exposedObject = {};
    }

    function BubbleDirective($rootScope) {

        function bubbleClicked(bid) {
            $rootScope.Ui.turnOn('creds');
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