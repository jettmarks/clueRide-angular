(function (angular) {
    'use strict';

    angular
        .module('crPlayer.MenuModule', ['crPlayer.BadgesModule'])
        .controller('MenuAuthController', MenuAuthController)
    ;

    MenuAuthController.$inject = ['$scope', 'BadgesService'];

    function MenuAuthController($scope, BadgesService) {
        $scope.badgesService = BadgesService;
    }

}(window.angular));