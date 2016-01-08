(function (angular) {
    'use strict';

    angular
        .module('crNetEdit.EditModeModule', ['ui.bootstrap'])
        .controller('Controller', Controller)
        //.service('Service', Service)
        .directive('crEditMode', crEditMode)
    ;

    //Controller.$inject = ['dependencies'];

    function Controller() {
        var vm = this;

        vm.editModes = [];
        vm.selectedEditMode = {
            id: 1,
            name: 'Add Node to Network'
        }

        vm.select = select;
        init(vm);

        function select(editMode) {
            vm.selectedEditMode = editMode;
        }
    }

    function init(vm) {
        vm.editModes = [
            {
                id: 1,
                name: 'Add Node to Network'
            },
            {
                id: 2,
                name: 'Adjust Nodes'
            }
        ];
    }

    //function Service() {
    //    return {
    //        request: requestFunction,
    //        confirm: confirmFunction
    //    };
    //}

    function crEditMode() {
        return {
            //transclude: true,
            //scope: {
            //    bindVar: '=',
            //    bindFcn: '&'
            //},
            templateUrl: 'js/editMode/editMode.html'
        }
    }

}(window.angular));