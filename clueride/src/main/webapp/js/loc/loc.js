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
            /* From the map. */
            mapCoords: {
                lat: 33.7,
                lon: -84.1
            },
            /* When tethered, this is used as the "device's" location. */
            tetherCoords: {
                lat: 33.7,
                lon: -84.1
            },
            /* From the device. */
            gpsCoords: {
                lat: 33.7,
                lon: -84.1
            },
            /* Either set to 'device' or 'map' depending on editor's choice. */
            masterCoordSet: 'device',
            /* If GPS not enabled, will need to use tethered or map. */
            gpsEnabled: false,
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

    function getMapCoords() {
        return viewModel.locationState.mapCoords;
    }

    function setMapCoords(mapCoords) {
        viewModel.locationState.mapCoords = mapCoords;
    }

    function getTetherCoords() {
        return viewModel.locationState.tetherCoords;
    }

    function setTetherCoords(tetherCoords) {
        viewModel.locationState.tetherCoords = tetherCoords;
    }

    function getDeviceCoords() {
        if (viewModel.locationState.gpsEnabled) {
            return viewModel.locationState.gpsCoords;
        } else {
            return viewModel.locationState.tetherCoords;
        }
    }

    /**
     * Follows whatever source of coordinates we want to use to pretend we're at
     * a given location.
     * Map when we want map.  Team Lead when we are tethered, and GPS when we are
     * picking up a GPS signal we want to use.
     */
    function getCloseToMeCoords() {
        if (viewModel.locationState.masterCoordSet === 'device') {
            return getDeviceCoords();
        } else {
            return viewModel.locationState.mapCoords;
        }
    }

    /**
     * Knows about Location State from the Game's dynamic perspective as well as
     * where the map is centered for editing locations, and where the device sits,
     * as well as the preferences for which set of coordinates to use.
     * Also holds methods for initialization and can return the list of Locations for the course.
     */
    function LocationService() {
        return {
            setLocationScope: setLocationScope,
            getLocationCount: getLocationCount,
            getLocationId: getLocationId,

            setEditLocation: setEditLocation,
            getEditLocation: getEditLocation,

            /* Where the center of the map resides or has recently resided. */
            getMapCoords: getMapCoords,
            setMapCoords: setMapCoords,

            /* When tracking a tether, the location is updated here. */
            getTetherCoords: getTetherCoords,
            setTetherCoords: setTetherCoords,

            getDeviceCoords: getDeviceCoords,

            getCloseToMeCoords: getCloseToMeCoords,

            /* Which set of coordinates should be used as the master set. */
            isTethered: function () {
                return !viewModel.locationState.gpsEnabled;
            },
            //isLocationSourceMap: function () { return viewModel.locationState. }
            disableGps: function () {
                viewModel.locationState.gpsEnabled = false;
            },
            enableGps: function () {
                viewModel.locationState.gpsEnabled = true;
            },

            trackMap: function () {viewModel.locationState.masterCoordSet = 'map';},
            trackDevice: function () {viewModel.locationState.masterCoordSet = 'device';},

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
