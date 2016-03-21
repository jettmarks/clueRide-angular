(function (angular) {
    'use strict';

    /** Handles the Team Members directive for sharing the list of Team Member. */
    var module;

    function TeamDisplayController(TeamResource) {
        var teamDisplay = this;

        /**
         * Start-up logic for initializing the controller.
         */
        function activate() {

            // TODO: replace hard-coded teamId
            TeamResource.get({teamId: 2}, function (data) {
                teamDisplay.team = data;
            });

        }

        /* Initialize the controller. */
        activate();
    }

    function teamDisplay() {
        return {
            scope: {},
            restrict: 'EA',
            bindToController: {
                team: '='
            },
            templateUrl: 'js/team/team.html',
            controller: TeamDisplayController,
            controllerAs: 'teamDisplay'
        };
    }

    module = angular.module(
        'crPlayer.Team'
    );

    module.directive('teamDisplay', teamDisplay);

}(window.angular));
