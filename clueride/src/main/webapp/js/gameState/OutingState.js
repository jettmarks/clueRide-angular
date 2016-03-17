(function (angular) {
    'use strict';

    var viewModel = {
            state: {
                bubble1: {},
                bubble2: {},
                bubble3: {}
            }
        },
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
        bubblesPerStep(1);
    }

    Controller.$inject = ['dependencies'];

    function Controller(dependencies) {
        var vm = this;

        vm.exposedObject = {};
        vm.exposecFunction = exposedFunction;
    }

    function bubblesPerStep(index) {
        var limitedIndex = index < 2 ? 2 : (index > 6 ? 6 : index);
        viewModel.state.bubble1 = stateArray[limitedIndex-1];
        viewModel.state.bubble2 = stateArray[limitedIndex];
        viewModel.state.bubble3 = stateArray[limitedIndex+1]
    }

    function getReverseTitle() {
        return viewModel.state.bubble1.title;
    }

    function getReverseDisabled() {
        return viewModel.state.bubble1.disabled;
    }

    function getNeutralTitle() {
        return viewModel.state.bubble2.title;
    }

    function getNeutralDisabled() {
        return viewModel.state.bubble2.disabled;
    }

    function getForwardTitle() {
        return viewModel.state.bubble3.title;
    }

    function getForwardDisabled() {
        return viewModel.state.bubble3.disabled;
    }

    function login() {
        if (stepIndex === 1) {
            stepIndex = 2;
        }
        bubblesPerStep(stepIndex);
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

            getPathIndex: function () {return outingState.pathIndex},

            /* Events. */
            login: login
        };
    }


}(window.angular));