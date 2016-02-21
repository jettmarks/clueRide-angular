(function (angular) {
    'use strict';

    var viewModel,
        localModel = {
            gsSvc: {}
        },
        locState = {
            currentPage: 1,
            pageChanged: pageChanged,
            lastLocationIndex: lastLocationIndex,
            location: {}
        };

    angular
        .module('crPlayer.Location', ['crPlayer.GameState'])
        .controller('LocationController', LocationController)
        .service('LocationService', LocationService)
        .directive('backImg', backImg)
    ;

    LocationController.$inject = ['gameStateService'];

    function LocationController(gameStateService) {
        init(gameStateService);
    }

    function init(gameStateService) {
        var locIndex = gameStateService.getLocationIndex();

        localModel.gsSvc = gameStateService;

        locState.location = viewModel.locations[locIndex];

        // Zero-based index vs. one-based page number
        locState.currentPage = locIndex+1;
    }

    function lastLocationIndex() {
        /* Index is zero-based; number of pages is one-based. */
        return localModel.gsSvc.maxVisibleLocationIndex() + 1;
    }

    function pageChanged() {
        var locIndex = locState.currentPage-1;
        locState.location = viewModel.locations[locIndex];
        localModel.gsSvc.setHistoryLocation(locIndex);
    }

    function setLocationScope(locationScope) {
        viewModel = locationScope;
        viewModel.locationState = locState;
        viewModel.editLocation = {
            name: 'No Edit Location Selected'
        };
    }

    function getLocationCount() {
        return viewModel.locations.length;
    }

    function getLocationId() {
        return viewModel.locations[localModel.gsSvc.getLocationIndex()].id;
    }

    function setEditLocation(location) {
        viewModel.editLocation = location;
    }

    function getEditLocation() {
        return viewModel.editLocation;
    }

    function LocationService() {
        return {
            setLocationScope: setLocationScope,
            getLocationCount: getLocationCount,
            getLocationId: getLocationId,

            setEditLocation: setEditLocation,
            getEditLocation: getEditLocation,

            locations: function () {return viewModel.locations;},
            init: init
        }
    }

    function backImg () {
        return function(scope, element, attrs) {
            var url = attrs.backImg;
            element.css({
                'background-image': 'url(' + url +')',
                'background-size' : 'cover'
            });
        };
    }

}(window.angular));
