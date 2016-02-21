(function (angular) {
    'use strict';

    var
    /* TODO: Connect with the proper object within scope. */
        locationToEdit = {},
        viewModel = {
            /* Set in the crLocEdit Module's controller. */
            saveImageUrl: ""
        },

        audioSelect = document.querySelector('select#audioSource'),
        videoSelect = document.querySelector('select#videoSource');

    angular
        .module('crLocEditImage',['camera'])
        .controller('ImageEditController', ImageEditController)
        .factory('LocationEditor', LocationEditor)
    ;

    ImageEditController.$inject = [
        '$scope',
        '$location',
        '$window',
        'FileUploader',
        'LocationEditor'
    ];

    function ImageEditController(
        $scope,
        $location,
        $window,
        FileUploader,
        LocationEditor
    ) {
        viewModel = $scope;

        $scope.imageState = {
            cameraOpen: true,
            cameraImage: false
        };

        $scope.locationSelected = "Loading Locations ...";

        $scope.locationMap = LocationEditor.locationMap();

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

            uploader.addToQueue(file);

            $location.path('location');
        };

        $scope.locEdit.cancel = function () {
            $window.history.back();
        };

        //$scope.cameras = [{label: 'front'}, {label: 'back'}];
        $scope.cameras = [];
        showCameraChoices();
    }

    function LocationEditor(LocationEditResource) {
        var locationMap = {};

        return {
            getNearestLocations: function () {
                // Clear out previous values
                locationMap = {};

                // Returns the result so the promise is available to the caller
                // TODO: Tie this to a center on the map
                return LocationEditResource.get( {lat: 33.7, lon: -84.4}, function (locations) {
                    var loc;
                    for (var i= 0, len = locations.length; i<len; i++) {
                        loc = locations[i];
                        locationMap[loc.id] = loc;
                    }
                })
            },
            locationMap: function () {return locationMap}
        }
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

    function gotSources(sourceInfos) {
        for (var i = 0; i !== sourceInfos.length; ++i) {
            var sourceInfo = sourceInfos[i];
            var option = document.createElement('option');
            option.value = sourceInfo.id;
            if (sourceInfo.kind === 'audio') {
                //option.text = sourceInfo.label || 'microphone ' +
                //    (audioSelect.length + 1);
                //audioSelect.appendChild(option);
            } else if (sourceInfo.kind === 'video') {
                option.text = sourceInfo.label || 'camera ' + (videoSelect.length + 1);
                //videoSelect.appendChild(option);
                viewModel.cameras.push({
                    label: option.text,
                    id: option.value
                });
                if (option.text.indexOf('back') > -1) {
                    console.log("Found back camera");
                    //Webcam.params.constraints.video.optional.sourceId = option.value;
                }
            } else {
                console.log('Some other kind of source: ', sourceInfo);
            }
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

}(window.angular));
