(function (angular) {
    'use strict';

    var deviceCoords = {lat: 0.0, lon: 0.0},
        saveImageUrl = "",
        module = angular.module('crLocEdit',['camera'])

        .controller('LocEditController', [
            '$scope', '$location', '$window', 'FileUploader',
            function ($scope, $location, $window, FileUploader) {
                $scope.imageState = {
                    cameraOpen: true,
                    cameraImage: false
                };

                // Position is returned asynchronously, and requires some delay; kick it off now.
                requestLocation();

                $scope.locEdit = {};

                /**
                 * Performs Save by converting the image data to Blob for the upload code
                 * and adding the Location ID and Coordinates via the URL which has already
                 * been constructed from reading the device's current location.
                 */
                $scope.locEdit.save = function () {
                    var file = dataURItoBlob($scope.imageState.cameraImage),
                        uploader = new FileUploader({
                            //url: "/rest/location/uploadImage?locId=6&lat="+coords.lat+"&lon="+coords.lon,
                            url: saveImageUrl,
                            method: 'POST',
                            autoUpload: true
                        });

                    uploader.addToQueue(file);

                    $location.path('location');
                };

                $scope.locEdit.cancel = function () {
                    $window.history.back();
                }
            }
        ]);

    function requestLocation () {
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
        saveImageUrl = "/rest/location/uploadImage?locId=6&lat="+deviceCoords.lat+"&lon="+deviceCoords.lon
    }

    function showError(error) {
        switch(error.code) {
            case error.PERMISSION_DENIED:
                x.innerHTML = "User denied the request for Geolocation."
                break;
            case error.POSITION_UNAVAILABLE:
                x.innerHTML = "Location information is unavailable."
                break;
            case error.TIMEOUT:
                x.innerHTML = "The request to get user location timed out."
                break;
            case error.UNKNOWN_ERROR:
                x.innerHTML = "An unknown error occurred."
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

}(window.angular));
