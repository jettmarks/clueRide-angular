(function (angular) {
    'use strict';

    var viewModel,
        localModel,
        stateArray = [],
        outingState = {
            id: 2,
            pathIndex: -1,
            teamConfirmed: false,
            mostRecentClueSolved: false,
            selectedClueId: -1
        },
        stepIndex = 1;

    angular
        .module('crPlayer.OutingStateModule', [])
        //.controller('Controller', Controller)
        .service('OutingStateService', OutingStateService)
        .run(init)
    ;

    /** Records dependencies used in this scope. */
    function init($location) {
        stateArray = [
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
        return stateArray[stepIndex].title;
    }

    function getReverseDisabled() {
        return stateArray[stepIndex].disabled;
    }

    function getNeutralTitle() {
        return stateArray[stepIndex+1].title;
    }

    function getNeutralDisabled() {
        return stateArray[stepIndex+1].disabled;
    }

    function getForwardTitle() {
        return stateArray[stepIndex+2].title;
    }

    function getForwardDisabled() {
        return stateArray[stepIndex+2].disabled;
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

            getPathIndex: function () {return outingState.pathIndex}
        };
    }


}(window.angular));