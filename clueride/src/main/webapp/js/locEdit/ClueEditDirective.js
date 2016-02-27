(function (angular) {
    'use strict';

    angular
        .module('common.ClueEditDirective', [
            'common.ClueResource',
            'ui.bootstrap',
            'ui.bootstrap.buttons'
        ])
        .directive('crClueEdit', ClueEditDirective)
    ;

    function ClueEditDirective() {
        return {
            scope: {
                clue: '='
            },
            templateUrl: 'js/locEdit/clueEdit.html'
        }
    }

}(window.angular));