(function (angular, Webcam) {
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
        '$rootScope',
        '$location',
        'LocationEditor',
        'LocationResource',
        'LocationTypeResource',
        'LocationService',
        'ClueResource',
        'FileUploader'
    ];

    function LocationEditController(
        $scope,
        $window,
        $rootScope,
        $location,
        LocationEditor,
        LocationResource,
        LocationTypeResource,
        LocationService,
        ClueResource,
        FileUploader
    ) {
        viewModel = $scope;

        localModel.locationResource = LocationResource;
        localModel.locationTypeResource = LocationTypeResource;
        localModel.locationService = LocationService;
        localModel.clueResource = ClueResource;

        // Position is returned asynchronously, and requires some delay; kick it off now.
        requestGpsLocation();

        if (localModel.locationService.getEditLocation().id) {
            $scope.locationSelected = localModel.locationService.getEditLocation();
            localModel.locationToEdit = $scope.locationSelected;
            updateSaveImageUrl();
        } else {
            // List of Nearby Locations also requires some delay; kick it off too
            $scope.locationSelected = "Loading Locations ...";
            LocationEditor.getNearestLocations().$promise.then(function (locations) {
                $scope.locationSelected = locations[0];
                localModel.locationToEdit = locations[0];
                localModel.locationService.setEditLocation(locations[0]);
                updateSaveImageUrl();
            });
        }

        //LocationEditor.getLocationTypes().$promise.then(populateLocationTypes());
        LocationEditor.getLocationTypes();

        $scope.locationMap = LocationEditor.locationMap();
        $scope.typeMap = LocationEditor.typeMap();

        $scope.locEdit = {};

        /**
         * Performs Save by converting the image data to Blob for the upload code
         * and adding the Location ID and Coordinates via the URL which has already
         * been constructed from reading the device's current location.
         */
        $scope.locEdit.save = function () {
            var file = dataURItoBlob($scope.imageState.cameraImage),
                uploader = new FileUploader({
                    url: viewModel.saveImageUrl,
                    method: 'POST',
                    autoUpload: true
                });

            console.log("Save URL: " + viewModel.saveImageUrl);

            uploader.addToQueue(file);

            $location.path('location');
        };

        $scope.locEdit.cancel = function () {
            $window.history.back();
        };

        $scope.recordSelection = function (selectedItem) {
            $scope.locationSelected = $scope.locationMap[selectedItem];
            localModel.locationToEdit = $scope.locationSelected;
            localModel.locationService.setEditLocation($scope.locationSelected);
            updateSaveImageUrl();
        };

        $scope.typeSelection = function (selectedType) {
            $scope.locationSelected.locationType = selectedType;
        };

        $scope.saveLocation = saveLocation;

        $scope.cameras = [];
        showCameraChoices();
        $scope.cameraSelection = function (camera) {
            $scope.cameraSelected = camera;
        };

        $scope.turnOnCamera = function () {
            $rootScope.showHeaderFooter = false;
            setCamera($scope.cameraSelected.value);
            $location.path("locEdit/newImage");
        }

        viewModel.selectedClue = 0;
        viewModel.setClue = function (clueId) {
            viewModel.selectedClue = clueId;
            $rootScope.Ui.turnOn('clueEdit');
        }

        $scope.loadClueTab = function () {
            var clueIds = viewModel.locationSelected.clueIds;
            /* Clear any previous values. */
            viewModel.clues = [];

            for (var clueId in clueIds) {
                localModel.clueResource.get({clueId: clueId},
                function (clue) {
                    viewModel.clues.push(clue);
                });
            }
        }

    }

    function setCamera(cameraId) {
        /* Records the camera selection. */
        Webcam.params.sourceId = cameraId;
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
                return LocationNearestResource.get( {
                        lat: localModel.locationService.getCloseToMeCoords().lat,
                        lon: localModel.locationService.getCloseToMeCoords().lon
                    },
                    function (locations) {
                        var loc;
                        for (var i= 0, len = locations.length; i<len; i++) {
                            loc = locations[i];
                            locationMap[i] = loc;
                        }
                    }
                )
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

    /**
     * Construct "constraints" that tell Webcam which camera to use.
     * @param sources
     */
    function gotSources(sources) {
        var lastOption;
        for (var i = 0; i !== sources.length; ++i) {
            var sourceInfo = sources[i];
            var option = document.createElement('option');
            option.value = sourceInfo.id;
            if (sourceInfo.kind === 'video') {
                option.text = sourceInfo.label || 'camera ' + (videoSelect.length + 1);
                viewModel.cameras.push({
                    label: option.text,
                    id: option.value
                });
                if (option.text.indexOf('back') > -1) {
                    console.log("Found back camera");
                    viewModel.cameraSelected = option;
                }
            }
            lastOption = option;
        }
        /* Bring in at least some camera if 'back' not found. */
        if (!viewModel.cameraSelected) {
            viewModel.cameraSelected = lastOption;
        }
    }

    function showCameraChoices() {
        if (typeof MediaStreamTrack === 'undefined' ||
            typeof MediaStreamTrack.getSources === 'undefined') {
            alert('This browser does not support MediaStreamTrack.\n\nTry Chrome.');
        } else {
            MediaStreamTrack.getSources(gotSources);
        }
    }

    /*
     * Conversion required to put this over the wire.
     * See java720's response to
     * http://stackoverflow.com/questions/4998908/convert-data-uri-to-file-then-append-to-formdata
     */
    function dataURItoBlob(dataURI) {
        var binary = atob(dataURI.split(',')[1]);
        var array = [];
        for(var i = 0; i < binary.length; i++) {
            array.push(binary.charCodeAt(i));
        }
        return new Blob([new Uint8Array(array)], {type: 'image/jpeg'});
    }

}(window.angular, Webcam));
