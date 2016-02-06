(function (angular) {
    'use strict';

    var gsSvc,
        currentPage,
        vm;

    angular
        .module('status', ['crPlayer.GameState','crLocation'])
        .controller('StatusController', StatusController)
    ;

    StatusController.$inject = ['$scope','gameStateService','locationService'];

    function StatusController($scope, gameStateService, locationService) {
        vm = this;

        gsSvc = gameStateService;
        currentPage = gsSvc.getLocationIndex();
        $scope.onPageChange = onPageChange;
        $scope.onPathChange = onPathChange;
        $scope.getLocationCount = locationService.getLocationCount;

        /* Ask service to "arrive" the team. */
        $scope.arrived = gameStateService.arrived;

        /* pathIndex -1 <=> pageIndex 1 */
        vm.pageForPath = gsSvc.getPathIndex() + 2;
        vm.pathChange = onPathChange
    }

    function onPageChange() {
        gsSvc.setHistoryLocation(currentPage);
    }

    function onPathChange() {
        /* This will trigger a save state. */
        gsSvc.setPathIndex(vm.pageForPath - 2);
    }

}(window.angular));
