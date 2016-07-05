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
        .run(initConfig)
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

        vm.teams = localModel.teams;

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

    function initConfig() {
        localModel = {
            // TODO: Currently hardcoded; will pull from Team Service eventually.
            teams: [
                {
                    id: 42,
                    name: 'Spokes Folks'
                }
            ]
        }
    }

    function selectTeam(team) {
        viewModel.selectedTeam = team;
    }

    function getTeamName() {
        if (viewModel) {
        return viewModel.selectedTeam.name;
        } else {
            return localModel.teams[0].name;
        }
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
            getTeamName: getTeamName
            //confirm: confirmFunction
        };
    }

    function OutingResource($resource) {
        return $resource('/rest/outing/:id', {}, {

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