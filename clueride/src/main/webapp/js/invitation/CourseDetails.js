(function (angular) {
    'use strict';

    /** Presents the Details of a selected Course (via courseId). */
    var module;

    function CourseDetailsController() {
        var vm = this;

        /**
         * Start-up logic for initializing the controller.
         */
        function activate() {
        }

        /* Initialize the controller. */
        activate();
    }

    function courseInvite() {
        return {
            scope: {},
            restrict: 'EA',
            bindToController: {
                invite: '='
            },
            templateUrl: 'js/invitation/courseDetails.html',
            controller: CourseDetailsController,
            controllerAs: 'vm'
        };
    }

    module = angular.module(
        'crPlayer.CourseDetails',
        []
    );

    module.directive('courseInvite', courseInvite);

}(window.angular));
