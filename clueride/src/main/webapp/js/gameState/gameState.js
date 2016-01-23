(function (angular) {
    'use strict';

    var viewModel,
        gameStates = {},
        gameStatePerKey = [],
        courseDataResource = {},
    /* Consider user state versus overall game/team state. */
        state = {
            /* Incremented as clues are solved and destinations reached. */
            pathIndex: -1,
            /* Reviews history of opened Paths -- capped by pathIndex. */
            historyIndex: 0,

            currentGameState: {},
            mostRecentClueSolvedFlag: false,
            maxVisibleLocationIndex: 0,
            currentGameStateKey: 'beginPlay'
        };

    angular
        .module('gameState', ['common.CourseResource'])
        .controller('GameStateController',GameStateController)
        .service('gameStateService', gameStateService)
        .run(gameStateInit)
    ;

    GameStateController.$inject = ['$scope', 'CourseDataResource'];

    function GameStateController($scope, CourseDataResource) {
        var vm = this;

        $scope.vm = vm;

        courseDataResource = CourseDataResource;
    }

    function dataToModel(course) {
        viewModel.course = course;
    }

    function setCourseScope(courseScope) {
        viewModel = courseScope;
        viewModel.state = state;
    }

    /* Events changing Game State: */
    function clueSolve() {
        state.mostRecentClueSolvedFlag = true;
        gameStates['atLocation'].pathIndex++;
        gameStates['riding'].pathIndex++;
        updateGameState('riding');
    }

    function arrived() {
        state.currentGameStateKey = 'atLocation';
        state.mostRecentClueSolvedFlag = false;
        gameStates['riding'].locationIndex++;
    }

    /* Making the change of state. */
    function updateGameState(newState) {
        state.currentGameStateKey = newState;
        state.currentGameState = gameStatePerKey[newState];
    }

    function gameStateService () {
        return {
            currentGameState: function () {return state.currentGameState},
            currentGameStateKey: function () {return state.currentGameStateKey},
            updateGameState: updateGameState,

            // TODO: Bring this closer to the Location view
            currentLocation: function () {
                return {location: {
                    name: 'BeltLine'
                }};
            },

            /* Events triggering change of state. */
            /* May not be the best trigger event. */
            clueSolved: clueSolve,
            arrived: arrived,

            setCurrentLocation: function (newIndex) {
                state.pathIndex = newIndex;
            },

            /* Flags. */
            mostRecentClueSolved: function () {
                return state.mostRecentClueSolvedFlag;
            },

            /* Indices. */
            currentIndex: function () {
                return state.pathIndex;
            },
            maxVisibleLocationIndex: function () {
                return state.maxVisibleLocationIndex;
            },
            maxVisiblePathIndex: function () {
                if (state.mostRecentClueSolvedFlag) {
                    return state.pathIndex;
                } else {
                    return state.pathIndex - 1;
                }
            },
            /* Useful for the Location View. */
            visibleLocationCount: function () {

            },
            // TODO: Find better way to set the scope; link in Controller
            setCourseScope: setCourseScope
        };
    }

    function gameStateInit (CourseDataResource) {
        CourseDataResource.getData({
            /* Future: put the course ID here. */
        }, dataToModel);

        gameStates = {
            beginPlay: {
                title: 'Get Ready',
                pathIndex: -1,
                balloon1: {
                    bid: 'balloon1',
                    title: 'Join a Team?',
                    dialog: 'joinTeam',
                    nextView: '',
                    nextState: 'beginPlay'
                },
                balloon2: {
                    bid: 'balloon2',
                    title: 'GPS?',
                    dialog: 'setGpsMode',
                    nextView: '',
                    nextState: 'beginPlay'
                },
                balloon3: {
                    bid: 'balloon3',
                    title: 'Play',
                    nextView: '',
                    nextState: 'atLocation'
                }
            },
            riding: {
                title: 'Riding',
                pathIndex: 0,
                balloon1: {
                    bid: 'balloon1',
                    title: 'Last Stop',
                    nextView: '',
                    nextState: 'atLocation'
                },
                balloon2: {
                    bid: 'balloon2',
                    title: 'Where are we now?',
                    nextView: 'map',
                    nextState: 'riding'
                },
                balloon3: {
                    bid: 'balloon3',
                    title: 'Next Stop',
                    nextView: 'location',
                    nextState: 'riding'
                }
            },
            atLocation: {
                title: 'Got a Clue?',
                pathIndex: -1,
                locationIndex: 0,
                balloon1: {
                    bid: 'balloon1',
                    title: 'Current Location',
                    nextView: 'location',
                    nextState: 'atLocation'
                },
                balloon2: {
                    bid: 'balloon2',
                    title: 'Solve Clue',
                    dialog: 'solveClue',
                    nextView: '',
                    nextState: 'atLocation'
                },
                balloon3: {
                    bid: 'balloon3',
                    title: 'Where are we going?',
//                nextView: 'chooseLocation',
                    dialog: 'clueNotSolved',
                    nextState: 'riding'
                }
            }
        };

        gameStatePerKey = {
            beginPlay: gameStates.beginPlay,
            atLocation: gameStates.atLocation,
            riding: gameStates.riding
        };

        state.currentGameState = gameStatePerKey[state.currentGameStateKey];
    }

}(window.angular));