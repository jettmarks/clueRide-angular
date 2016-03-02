(function (angular, Webcam) {
    'use strict';

    var deviceCoords = {lat: 0.0, lon: 0.0},
        viewModel = {},
        localModel = {
            locationResource: {},
            locationToEdit: {}
        };

    angular
        .module('crLocEdit',['common.ClueEditDirective', 'crPlayer.GameState'])
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
        'GameStateService',
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
        GameStateService,
        ClueResource,
        FileUploader
    ) {
        viewModel = $scope;

        localModel.locationResource = LocationResource;
        localModel.locationTypeResource = LocationTypeResource;
        localModel.locationService = LocationService;
        localModel.gameStateService = GameStateService;
        localModel.clueResource = ClueResource;
        localModel.FileUploader = FileUploader;
        localModel.$location = $location;
        localModel.root = $rootScope;

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

        LocationEditor.getLocationTypes();

        $scope.locationMap = LocationEditor.locationMap();
        $scope.typeMap = LocationEditor.typeMap();

        $scope.locEdit = {};

        /* Location. */
        $scope.recordSelection = selectLocation;
        $scope.typeSelection = function (selectedType) {
            $scope.locationSelected.locationType = selectedType;
        };

        $scope.saveLocation = saveLocation;

        /* Cameras & Images. */
        viewModel.cameras = [];
        viewModel.imageState = {};
        viewModel.locEdit.save = imageSave;
        viewModel.locEdit.cancel = function () {
            $window.history.back();
        };
        showCameraChoices();
        viewModel.cameraSelection = function (camera) {
            viewModel.cameraSelected = camera;
        };

        viewModel.showHeaderFooter = $rootScope.showHeaderFooter;
        viewModel.turnOnCamera = turnOnCamera;

        /* Clues */
        viewModel.selectedClueIndex = 0;
        viewModel.setClue = setClue;
        viewModel.addClue = addClue;
        viewModel.removeClue = removeClue;
        viewModel.loadClueTab = loadClueTab;
        viewModel.clueEditOK = clueEditOK;
        viewModel.clueEditCancel = clueEditCancel;
    }

    /* Clue Functions. */

    function loadClueTab() {
        localModel.clueResource.query({locId: viewModel.locationSelected.id},
            function (clues) {
                viewModel.clues = clues;
            }
        );
    }

    /**
     * Selects a given clue for editing and then presents that clue for editing.
     * @param clueIndex
     */
    function setClue(clueIndex) {
        viewModel.selectedClueIndex = clueIndex;
        viewModel.selectedClue = viewModel.clues[clueIndex];
        localModel.root.Ui.turnOn('clueEdit');
    }

    /**
     * Creates a 'blank' clue for entering a new clue and then presents for editing.
     */
    function addClue () {
        viewModel.selectedClue = {
            name: 'New Clue',
            question: ' ',
            answers: [
                { key: 'A', answer: ' '},
                { key: 'B', answer: ' '},
                { key: 'C', answer: ' '},
                { key: 'D', answer: ' '}
            ],
            correctAnswer: 'A'
        };
        viewModel.clues.push(viewModel.selectedClue);
        viewModel.selectedClueIndex = viewModel.clues.length;
        localModel.root.Ui.turnOn('clueEdit');
    }

    function removeClue(clueIndex) {
        if (clueIndex) {
            var clueId = viewModel.locationSelected.clueIds[clueIndex],
                locId = viewModel.locationSelected.id;

            window.console.log("Clue Index: " + clueIndex);
            window.console.log("Clue ID: " + clueId);
            /* Update client. */
            viewModel.clues.splice(clueIndex, 1);
            viewModel.locationSelected.clueIds.splice(clueIndex, 1);
            /* Update server. */
            localModel.clueResource.remove({locId: locId, clueId: clueId},
                function (dontcare) {}
            );
        }
    }

    function clueEditOK() {
        /* Persist the new clue and add the new ID to the location's list. */
        localModel.clueResource.create(viewModel.selectedClue,
            function (newClue) {
                viewModel.selectedClue = newClue;
                /* Don't have the new Clue ID until after it has been sent to the server. */
                viewModel.locationSelected.clueIds.push(newClue.id);
                localModel.locationResource.save(viewModel.locationSelected);
                loadClueTab();
            }
        );
    }

    function clueEditCancel() {

    }

    /* Location Edit functions. */

    function selectLocation (selectedItem) {
        viewModel.locationSelected = selectedItem;
        localModel.locationToEdit = viewModel.locationSelected;
        localModel.locationService.setEditLocation(viewModel.locationSelected);
        updateSaveImageUrl();
    }

    function saveLocation() {
        localModel.locationResource.save(localModel.locationToEdit);
    }

    /**
     * Service that populates the Nearest Location and Location Type dropdowns.
     * @param LocationNearestResource
     * @param LocationTypeResource
     * @returns {{getNearestLocations: getNearestLocations, locationMap: Function, getLocationTypes: getLocationTypes, typeMap: Function}}
     * @constructor
     */
    function LocationEditor(LocationNearestResource, LocationTypeResource) {
        var locationMap = {},
            typeMap = {};

        function getNearestLocations () {
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
        }

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
            getNearestLocations: getNearestLocations,
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

    /* Camera functions. */

    /**
     * Performs Save by converting the image data to Blob for the upload code
     * and adding the Location ID and Coordinates via the URL which has already
     * been constructed from reading the device's current location.
     */
    function imageSave () {
        var file = dataURItoBlob(viewModel.imageState.cameraImage),
            uploader = new localModel.FileUploader({
                url: viewModel.saveImageUrl,
                method: 'POST',
                autoUpload: true
            });

        console.log("Save URL: " + viewModel.saveImageUrl);

        uploader.addToQueue(file);

        localModel.gameStateService.refreshLocationData();

        localModel.$location.path('location');
    }

    function turnOnCamera () {
        viewModel.showHeaderFooter = false;
        setCamera(viewModel.cameraSelected.value);
        localModel.$location.path("locEdit/newImage");
    }

    function setCamera(cameraId) {
        /* Records the camera selection. */
        Webcam.params.sourceId = cameraId;
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
    function dataURItoBlob(dataUri) {
        var binary, array = [];

        binary = atob(dataUri.split(',')[1]);
        for(var i = 0; i < binary.length; i++) {
            array.push(binary.charCodeAt(i));
        }
        return new Blob([new Uint8Array(array)], {type: 'image/jpeg'});
    }

}(window.angular, Webcam));
