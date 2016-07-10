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
            var tokenFromSearch = $route.current.params.inviteToken;

            /* Turn the URL parameter for the inviteToken into the invite (promise). */
            vm.invite = InviteResource.get({token: tokenFromSearch});
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
