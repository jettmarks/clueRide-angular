(function (angular) {
    'use strict';

    var gsSvc,
        currentPage,
        vm;

    angular
        .module('crPlayer.Status', ['crPlayer.GameState','crPlayer.Location'])
        .controller('StatusController', StatusController)
    ;

    StatusController.$inject = ['$scope','GameStateService','LocationService'];

    function StatusController($scope, GameStateService, LocationService) {
        vm = this;

        gsSvc = GameStateService;
        currentPage = gsSvc.getLocationIndex();
        $scope.onPageChange = onPageChange;
        $scope.onPathChange = onPathChange;
        $scope.getLocationCount = LocationService.getLocationCount;

        /* Ask service to "arrive" the team. */
        $scope.arrived = GameStateService.arrived;

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
