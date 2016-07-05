(function (angular) {
    'use strict';

    /** Course data for the Invitation Landing Page. */
    var module;

    function OutingDetailsController(OutingResource, InviteResource) {
        //var outingDetails = this;
        var vm = this;

        /**
         * Start-up logic for initializing the controller.
         */
        function activate() {
            /* Turn invite's outingId into the outing. */
            vm.invite.$promise.then(
                function (data) {
                    window.console.log(data);
                    vm.outing = OutingResource.get({id: data.outing.id});
                }
            );
        }

        /* Initialize the controller. */
        activate();
    }

    OutingDetailsController.$inject = [
        'OutingResource',
        'InviteResource'
    ];

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
