(function (angular) {
    'use strict';

    angular
        .module('where', [])
        .directive('pointerLocation', crPointerLocation)
    ;

    function crPointerLocation() {
        return {
            scope: {
                pointer: '='
            },
            templateUrl: 'js/where/where.html'
        };
    }

}(window.angular));
