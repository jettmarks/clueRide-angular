(function (angular) {
    'use strict';

    var mapScope = {},
        editMarkers = {}
    ;

    angular
        .module('crNetEdit.MapModule', [])
        .controller('MapController', MapController)
        .service('MapService', MapService)
        //.directive('crEditMode', crEditMode)
    ;

    MapController = ['$scope'];

    function MapController($scope) {
        var vm = this;

        //vm.exposedObject = {};
        //vm.exposecFunction = exposedFunction;
    }

    function MapService() {
        return {
            getMapScope: getMapScope,
            setMapScope: setMapScope,
            getEditMarkers: getEditMarkers,
            setEditMarkers: setEditMarkers
        };
    }

    function getMapScope () {
        return mapScope;
    }

    function setMapScope (scope) {
        mapScope = scope;
    }

    function getEditMarkers () {
        return editMarkers;
    }

    function setEditMarkers (markers) {
        editMarkers = markers;
    }

    //function crEditMode() {
    //    return {
    //        transclude: true,
    //        scope: {
    //            bindVar: '=',
    //            bindFcn: '&'
    //        },
    //        templateUrl: 'js/editMode/editMode.html'
    //    }
    //}

}(window.angular));