(function (angular) {
    'use strict';

    /** Handles setting up the Landing page with the data needed to present the Invitation details. */
    var module;

    function InvitationLandingController($route, InviteResource) {
        var vm = this;

        /**
         * Start-up logic for initializing the controller.
         */
        function activate() {
            /* Turn the URL parameter for the inviteToken into the invite (promise). */
            vm.tokenFromSearch = $route.current.params
            vm.invite = InviteResource.get({token: $route.current.params.inviteToken});
        }

        /* Initialize the controller. */
        activate();
    }

    InvitationLandingController.$inject = [
        '$route',
        'InviteResource'
    ];

    module = angular.module(
        'crPlayer.Invitation',
        [
            'crPlayer.invite',
            'ngRoute'
        ]
    );

    module.controller('InvitationLandingController', InvitationLandingController);

}(window.angular));
