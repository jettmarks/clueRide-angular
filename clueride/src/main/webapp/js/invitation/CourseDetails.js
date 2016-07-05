(function (angular) {
    'use strict';

    /** Presents the Details of a selected Course (via courseId). */
    var module;

    function CourseDetailsController(CourseDataResource) {
        var vm = this;

        /**
         * Start-up logic for initializing the controller.
         */
        function activate() {
            vm.outingInv.$promise.then(function(data){

                window.console.log(data);

                CourseDataResource.get({courseId : data.courseId })
                    .$promise
                    .then(function(data) {
                        angular.extend(vm, data);
                    })
                    .catch(function(response){
                        window.console.log(response);
                    });

            });
        }

        /* Initialize the controller. */
        activate();
    }

    CourseDetailsController.$inject = ['CourseDataResource'];

    function courseInvite() {
        return {
            scope: {},
            restrict: 'EA',
            bindToController: {
                outingInv: '='
            },
            templateUrl: 'js/invitation/courseDetails.html',
            controller: CourseDetailsController,
            controllerAs: 'vm'
        };
    }

    module = angular.module(
        'crPlayer.CourseDetails',
        ['common.CourseResource']
    );

    module.directive('courseInvite', courseInvite);

}(window.angular));
