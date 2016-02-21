(function (angular) {
    'use strict';

    var deviceCoords = {lat: 0.0, lon: 0.0},
        viewModel = {},
        localModel = {
            locationResource: {},
            locationToEdit: {}
        };

    angular
        .module('crLocEdit',[])
        .controller('LocEditController', LocationEditController)
        .factory('LocationEditor', LocationEditor)
    ;

    LocationEditController.$inject = [
        '$scope',
        '$window',
        'LocationEditor',
        'LocationResource',
        'LocationTypeResource'
    ];

    function LocationEditController(
        $scope,
        $window,
        LocationEditor,
        LocationResource,
        LocationTypeResource
    ) {
        viewModel = $scope;

        localModel.locationResource = LocationResource;
        localModel.locationTypeResource = LocationTypeResource;

        // Position is returned asynchronously, and requires some delay; kick it off now.
        requestGpsLocation();

        // List of Nearby Locations also requires some delay; kick it off too
        LocationEditor.getNearestLocations().$promise.then(function (locations) {
            $scope.locationSelected = locations[0];
            localModel.locationToEdit = locations[0];
            updateSaveImageUrl();
        });

        //LocationEditor.getLocationTypes().$promise.then(populateLocationTypes());
        LocationEditor.getLocationTypes();

        $scope.locationSelected = "Loading Locations ...";

        $scope.locationMap = LocationEditor.locationMap();
        $scope.typeMap = LocationEditor.typeMap();

        $scope.locEdit = {};

        $scope.locEdit.cancel = function () {
            $window.history.back();
        };

        $scope.recordSelection = function (selectedItem) {
            $scope.locationSelected = $scope.locationMap[selectedItem];
            localModel.locationToEdit = $scope.locationSelected;
            updateSaveImageUrl();
        };

        $scope.typeSelection = function (selectedType) {
            $scope.locationSelected.locationType = selectedType;
        };

        $scope.saveLocation = saveLocation;
    }

    function saveLocation() {
        localModel.locationResource.save(localModel.locationToEdit);
    }

    function LocationEditor(LocationNearestResource, LocationTypeResource) {
        var locationMap = {},
            typeMap = {};

        function getLocationTypes() {
            typeMap = {};

            return LocationTypeResource.get({}, function (locationTypes) {
                var locType;
                for (var i= 0, len = locationTypes.length; i<len; i++) {
                    locType = locationTypes[i];
                    /* ID is the same as the name at this time. */
                    typeMap[locType] = locType;
                }
            });
        }

        return {
            getNearestLocations: function () {
                // Clear out previous values
                locationMap = {};

                // Returns the result so the promise is available to the caller
                // TODO: Tie this to a center on the map
                return LocationNearestResource.get( {lat: 33.7, lon: -84.4}, function (locations) {
                    var loc;
                    for (var i= 0, len = locations.length; i<len; i++) {
                        loc = locations[i];
                        locationMap[loc.id] = loc;
                    }
                })
            },
            locationMap: function () {return locationMap},
            getLocationTypes: getLocationTypes,
            typeMap: function () {return typeMap}
        }
    }

    function requestGpsLocation () {
        // Capture the current lat/lon
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                recordPosition,
                showError
            );
        }
    }

    function recordPosition (position) {
        deviceCoords.lat = position.coords.latitude;
        deviceCoords.lon = position.coords.longitude;
        updateSaveImageUrl();
    }

    function updateSaveImageUrl () {
        viewModel.saveImageUrl = "/rest/location/uploadImage?locId=" + localModel.locationToEdit.id +
            "&lat=" + deviceCoords.lat + "&lon=" + deviceCoords.lon;
    }

    function showError(error) {
        switch(error.code) {
            case error.PERMISSION_DENIED:
                x.innerHTML = "User denied the request for Geolocation.";
                break;
            case error.POSITION_UNAVAILABLE:
                x.innerHTML = "Location information is unavailable.";
                break;
            case error.TIMEOUT:
                x.innerHTML = "The request to get user location timed out.";
                break;
            case error.UNKNOWN_ERROR:
                x.innerHTML = "An unknown error occurred.";
                break;
        }
    }

}(window.angular));
