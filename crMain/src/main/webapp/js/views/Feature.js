(function (angular) {
    'use strict';

    angular
        .module('crNetEdit.Feature', [])
        .controller('Controller', Controller)
        //.service('Service', Service)
        .directive('crFeature', crFeature)
    ;

    Controller. $inject= [];

    function Controller() {
        var vm = this;
    }

    //function Service() {
    //    return {
    //        request: requestFunction,
    //        confirm: confirmFunction
    //    };
    //}

    function crFeature() {
        return {
            transclude: true,
            scope: {
                feature: '='
            },
            templateUrl: 'js/views/feature.html'
        }
    }

}(window.angular));