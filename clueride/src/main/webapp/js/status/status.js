(function (angular) {
    'use strict';

    var gsSvc,
        currentPage;

    angular
        .module('status', ['crPlayer.GameState','crLocation'])
        .controller('StatusController', StatusController)
    ;

    StatusController.$inject = ['$scope','gameStateService','locationService'];

    function StatusController($scope, gameStateService, locationService) {
        gsSvc = gameStateService;
        $scope.vm.pathIndex = gsSvc.currentGameState().pathIndex;
        $scope.currentPage = currentPage;
        currentPage = gsSvc.getLocationIndex();
        $scope.onPageChange = onPageChange;
        $scope.getLocationCount = locationService.getLocationCount;
    }

    function onPageChange() {
        gsSvc.setHistoryLocation(currentPage);
    }

}(window.angular));
