(function (angular) {
    'use strict';

    var viewModel,
        localModel,
        state = [],
        stepIndex = 1;

    angular
        .module('crPlayer.OutingStateModule', [])
        //.controller('Controller', Controller)
        .service('OutingStateService', OutingStateService)
        .run(init)
    ;

    /** Records dependencies used in this scope. */
    function init($location) {
        state = [
            {
                /* Helpful to use this as our zero-index. */
                title: 'Undefined',
                disabled: true
            },
            {
                title: 'Login',
                disabled: false
            },
            {
                title: 'Join Team?',
                disabled: true
            },
            {
                title: 'GPS?',
                disabled: true
            },
            {
                title: 'Play',
                disabled: true
            },
            {
                title: 'Location',
                disabled: true
            },
            {
                title: 'Solve Clue',
                disabled: true
            },
            {
                title: 'Where Am I?',
                disabled: true
            }
        ];
    }

    Controller.$inject = ['dependencies'];

    function Controller(dependencies) {
        var vm = this;

        vm.exposedObject = {};
        vm.exposecFunction = exposedFunction;
    }

    function getReverseTitle() {
        return state[stepIndex].title;
    }

    function getReverseDisabled() {
        return state[stepIndex].disabled;
    }

    function getNeutralTitle() {
        return state[stepIndex+1].title;
    }

    function getNeutralDisabled() {
        return state[stepIndex+1].disabled;
    }

    function getForwardTitle() {
        return state[stepIndex+2].title;
    }

    function getForwardDisabled() {
        return state[stepIndex+2].disabled;
    }

    function OutingStateService() {
        return {
            getStepIndex: function () { return stepIndex },
            getReverseTitle: getReverseTitle,
            getReverseDisabled: getReverseDisabled,
            getNeutralTitle: getNeutralTitle,
            getNeutralDisabled: getNeutralDisabled,
            getForwardTitle: getForwardTitle,
            getForwardDisabled: getForwardDisabled,
        };
    }


}(window.angular));