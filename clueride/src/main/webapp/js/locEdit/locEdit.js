(function (angular) {
    'use strict';

    var module = angular.module('crLocEdit',['camera'])
        .controller('LocEditController', [
            '$scope', '$location', '$window', 'FileUploader',
            function ($scope, $location, $window, FileUploader) {
                $scope.imageState = {
                    cameraOpen: true,
                    cameraImage: false
                };

                $scope.locEdit = {};

                $scope.locEdit.save = function () {
                    var file, uploader;

                    file = dataURItoBlob($scope.imageState.cameraImage);

                    uploader = new FileUploader({
                        url: "/rest/location/uploadImage",
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
