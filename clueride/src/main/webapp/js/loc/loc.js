(function (angular) {
    'use strict';

    var viewModel,
        localModel = {
            gsSvc: {},
            lsSvc: {}
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
            zoom: 12,

            /* When tethered, this is used as the "device's" location. */
            tetherCoords: {
                lat: 33.7,
                lon: -84.1
            },
            /* From the device. */
            gpsCoords: {
                lat: 33.8,
                lon: -84.4
            },
            /* True if we use the map as "Close To Me" instead of device. */
            mapOverrideDevice: false,

            /* If GPS not enabled, will need to use tethered or map. */
            gpsEnabled: navigator.geolocation,
            location: {}
        };

    angular
        .module('crPlayer.Location', ['crPlayer.GameState', 'common.CourseResource'])
        .service('LocationService', LocationService)
        .directive('backImg', backImg)
        .run(getGpsPosition())
        .run(['GameStateService', 'CourseLocationDataResource',
            function (GameStateService, CourseLocationDataResource) {
                localModel.gsSvc = GameStateService;
                localModel.lsSvc = CourseLocationDataResource;
            }])
    ;

    function init() {
        var locIndex = localModel.gsSvc.getLocationIndex();

        localModel.lsSvc.get({
            /* Future: put Course ID here. */
        }, courseLocationsToModel);

        locState.location = viewModel.locations[locIndex];

        // Zero-based index vs. one-based page number
        locState.currentPage = locIndex+1;
    }

    function getGpsPosition() {
        if (locState.gpsEnabled) {
            navigator.geolocation.getCurrentPosition(function(position) {
                locState.gpsCoords.lat = position.coords.latitude;
                locState.gpsCoords.lon = position.coords.longitude;
            });
        }
    }

    function lastLocationIndex() {
        /* Index is zero-based; number of pages is one-based. */
        return localModel.gsSvc.maxVisibleLocationIndex() + 1;
    }

    function courseLocationsToModel(locations) {
        viewModel.locations = locations;
    }

    function pageChanged() {
        var locIndex = locState.currentPage-1;
        locState.location = viewModel.locations[locIndex];
        localModel.gsSvc.setHistoryLocation(locIndex);
    }

    /** One of a few places where the Scope is set manually in the app.js. */
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

    function getMapZoom() {
        return viewModel.locationState.zoom;
    }

    function setMapZoom(zoom) {
        viewModel.locationState.zoom = zoom;
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
        if (viewModel.locationState.mapOverrideDevice) {
            return viewModel.locationState.mapCoords;
        } else {
            return getDeviceCoords();
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
            getMapZoom: getMapZoom,
            setMapZoom: setMapZoom,

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

            trackMap: function () {viewModel.locationState.mapOverrideDevice = true;},
            trackDevice: function () {viewModel.locationState.mapOverrideDevice = false;},

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
