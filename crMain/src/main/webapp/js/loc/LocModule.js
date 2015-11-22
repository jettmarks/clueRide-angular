'use strict';

var LocModule = angular
    .module('crNetEdit.LocModule', [
        'leaflet-directive',
//        'LocDiagResource',
        'ngResource'
    ]);

var locallyScopedVars;

LocModule
    .factory('crNetEdit.LocSvc', function () {
        
    })
    .directive('showNodes', function() {
        return {
            templateUrl: 'js/loc/showNodes.html'
        }
    });


