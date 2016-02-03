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
        .module('crLocation',['crPlayer.GameState'])
        .controller('LocationController', LocationController)
        .directive('backImg', backImg)
        .service('locationService', locationService)
    ;

    LocationController.$inject = ['gameStateService'];

    function LocationController(gameStateService) {
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
    }

    function getLocationCount() {
        return viewModel.locations.length;
    }

    function locationService() {
        return {
            setLocationScope: setLocationScope,
            getLocationCount: getLocationCount
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
