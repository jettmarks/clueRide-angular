(function (angular) {
    'use strict';

    var localModel = {},
        viewModel;

    angular
        .module('crPlayer.OutingModule', ['ngResource'])
        .controller('OutingController', OutingController)
        .service('OutingService', OutingService)
        .factory('OutingResource', OutingResource)
        .directive('outing', OutingDirective)
    ;

    OutingController.$inject = ['OutingResource'];

    function OutingController(OutingResource) {
        var vm = this;

        localModel.outingResource = OutingResource;

        viewModel = vm;

        /* The currently proposed outing. */
        vm.outing = {
            teamId: {},
            courseId: {}
        };

        vm.selectedTeam = {
            name: 'tap to choose'
        };
        vm.selectTeam = selectTeam;

        // TODO: Currently hardcoded; will pull from Team Service eventually.
        vm.teams = [
            {
                id: 42,
                name: 'Spokes Folks'
            }
        ];

        vm.selectedCourse = {
            name: 'tap to choose'
        };
        vm.selectCourse = selectCourse;

        // TODO: Currently hardcoded, but will pull from the Course Service eventually.
        vm.courses = [
            {
                id: 2,
                name: 'Five Free Things'
            },
            {
                id: 3,
                name: 'Tiny Doors'
            }
        ];

        vm.createOuting = createOuting;
    }

    function selectTeam(team) {
        viewModel.selectedTeam = team;
    }

    function selectCourse(course) {
        viewModel.selectedCourse = course;
    }

    function createOuting() {
        localModel.outingResource.save({
            teamId: viewModel.selectedTeam.id,
            courseId: viewModel.selectedCourse.id
        });
    }

    function OutingService() {
        return {
            //request: requestFunction,
            //confirm: confirmFunction
        };
    }

    function OutingResource($resource) {
        return $resource('/rest/outing', {}, {

        });
    }

    function OutingDirective() {
        return {
            transclude: true,
            scope: {
                bindVar: '=',
                bindFcn: '&'
            },
            templateUrl: 'js/editMode/editMode.html'
        }
    }

}(window.angular));