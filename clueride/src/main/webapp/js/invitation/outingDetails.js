(function (angular) {
    'use strict';

    /** Course data for the Invitation Landing Page. */
    var module;

    function OutingDetailsController() {
        var vm = this;

        /**
         * Start-up logic for initializing the controller.
         */
        function activate() {
        }

        /* Initialize the controller. */
        activate();
    }

    function outingDetailsDirective() {
        return {
            scope: {},
            restrict: 'EA',
            bindToController: {
                invite: '='
            },
            templateUrl: 'js/invitation/outingDetails.html',
            controller: OutingDetailsController,
            controllerAs: 'vm'
        };
    }

    module = angular.module(
        'crPlayer.outingDetails',
        ['crPlayer.invite']
    );

    module.directive('outingInvite', outingDetailsDirective);

}(window.angular));
