(function (angular) {
    'use strict';

    /** Handles the presentation of individual Team Members. */
    var module;

    function TeamMemberController() {
        var teamMember = this;

        /**
         * Start-up logic for initializing the controller.
         */
        function activate() {
            teamMember.member.lead = (teamMember.member.name === 'Jett');
        }

        /* Initialize the controller. */
        activate();
    }

    function teamMember() {
        return {
            scope: {},
            restrict: 'EA',
            bindToController: {
                member: '='
            },
            templateUrl: 'js/team/member.html',
            controller: TeamMemberController,
            controllerAs: 'teamMember'
        };
    }

    /** This modules defined in teamDisplay.js. */
    module = angular.module(
        'crPlayer.Team'
    );

    module.directive('teamMember', teamMember);

}(window.angular));
