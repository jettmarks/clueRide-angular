(function (angular) {
    'use strict';

    angular
        .module('crNetEdit.Feature', [])
        .controller('Controller', Controller)
        .directive('crFeature', crFeature)
    ;

    Controller. $inject= [];

    function Controller() {
        var vm = this;
    }

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