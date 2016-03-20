(function (angular) {
    'use strict';

    /** Handles the Team Members directive for sharing the list of Team Member. */
    var module;

    function TeamDisplayController() {
        var teamDisplay = this;

        /**
         * Start-up logic for initializing the controller.
         */
        function activate() {
            teamDisplay.team = {
                "id": 2,
                "name": "Spokes Folks",
                "members": [
                    {
                        "id": null,
                        "name": "Jett"
                    },
                    {
                        "id": null,
                        "name": "Member 1"
                    },
                    {
                        "id": null,
                        "name": "Member 2"
                    },
                    {
                        "id": null,
                        "name": "Member 3"
                    }
                ]
            }
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
        'crPlayer.Team',
        [
        ]
    );

    module.directive('teamDisplay', teamDisplay);

}(window.angular));
