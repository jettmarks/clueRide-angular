(function (angular) {
    'use strict';

    var viewModel,
        gameStates = {},
        gameStatePerKey = [],
        courseDataResource = {},
        gameStateResource = {},
    /* Consider user state versus overall game/team state. */
        state = {
            /* Incremented as clues are solved and destinations reached. */
            pathIndex: -1,
            /* Reviews history of opened Paths -- capped by pathIndex. */
            historyIndex: 0,

            /* Crutch. */
            teamId: 42,

            currentGameState: {},
            mostRecentClueSolvedFlag: false,
            maxVisibleLocationIndex: 0,
            currentGameStateKey: 'beginPlay'
        };

    angular
        .module('gameState', ['common.CourseResource'])
        .controller('GameStateController', GameStateController)
        .service('gameStateService', gameStateService)
        .factory('GameStateResource', GameStateResource)
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
        state.pathIndex++;
        updateGameState('riding');
    }

    function arrived() {
        // TODO: Not sure this is useful
        gameStates['atLocation'].locationIndex++;
        updateGameState('atLocation');
        state.mostRecentClueSolvedFlag = false;
    }

    /* Making the change of state. */
    function updateGameState(newState) {
        if (historyTransition(newState)) {
            updateHistory(newState);
        } else {
            state.currentGameStateKey = newState;
            state.currentGameState = gameStatePerKey[newState];
            /* Only update state on server if we're not browsing history. */
            gameStateResource.updateState(state);
        }
    }

    function historyTransition(newState) {
        return (newState.indexOf('history') > -1);
    }

    function updateHistory(historyState) {
        if (historyState === 'history_back') {
            state.historyIndex--;
        } else if (historyState === 'history_forward') {
            state.historyIndex++;
        }
        if (state.historyIndex < 0) {
            state.historyIndex = 0;
        }
        if (state.historyIndex > state.pathIndex) {
            if (state.mostRecentClueSolvedFlag) {
                updateGameState('riding');
            } else {
                updateGameState('atLocation');
            }
        } else {
            state.currentGameStateKey = 'history';
            state.currentGameState = gameStatePerKey['history'];
            state.currentGameState.title = "Location " + state.historyIndex;
        }
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

    /**
     * Retrieve for a Team and Update for a Team.
     * @returns {*}
     * @constructor
     */
    function GameStateResource($resource) {
        return $resource('/rest/gameState/team', {}, {
            getState: {
                method: 'GET',
                /* Hardcoded until we have more than one team. */
                params: {teamId: 42},
                isArray: false
            },
            updateState: {
                method: 'PUT'
                /* Hardcoded until we have more than one team. */
            }
        });
    }

    function stateToModel(data) {
        //viewModel.state = data;
        viewModel.state.pathIndex = data.pathIndex;
        updateGameState(data.currentGameStateKey);
    }

    function gameStateInit (CourseDataResource, GameStateResource) {
        CourseDataResource.getData({
            /* Future: put the course ID here. */
        }, dataToModel);

        gameStateResource = GameStateResource;
        gameStateResource.get({}, stateToModel);

        gameStates = {
            beginPlay: {
                title: 'Get Ready',
                bubble1: {
                    bid: 'bubble1',
                    title: 'Join a Team?',
                    dialog: 'joinTeam',
                    nextView: '',
                    nextState: 'beginPlay'
                },
                bubble2: {
                    bid: 'bubble2',
                    title: 'GPS?',
                    dialog: 'setGpsMode',
                    nextView: '',
                    nextState: 'beginPlay'
                },
                bubble3: {
                    bid: 'bubble3',
                    title: 'Play',
                    nextView: '',
                    nextState: 'atLocation'
                }
            },
            riding: {
                title: 'Riding',
                bubble1: {
                    bid: 'bubble1',
                    title: 'Last Stop',
                    nextView: '',
                    nextState: 'history'
                },
                bubble2: {
                    bid: 'bubble2',
                    title: 'Where are we now?',
                    nextView: 'map',
                    nextState: 'riding'
                },
                bubble3: {
                    bid: 'bubble3',
                    title: 'Next Stop',
                    nextView: 'location',
                    nextState: 'riding'
                }
            },
            atLocation: {
                title: 'Got a Clue?',
                locationIndex: 0,
                bubble1: {
                    bid: 'bubble1',
                    title: 'Current Location',
                    nextView: 'location',
                    nextState: 'atLocation'
                },
                bubble2: {
                    bid: 'bubble2',
                    title: 'Solve Clue',
                    dialog: 'solveClue',
                    nextView: '',
                    nextState: 'atLocation'
                },
                bubble3: {
                    bid: 'bubble3',
                    title: 'Where are we going?',
//                nextView: 'chooseLocation',
                    dialog: 'clueNotSolved',
                    nextState: 'riding'
                }
            },
            history: {
                title: 'History <Location>',
                locationIndex: 0,
                bubble1: {
                    bid: 'bubble1',
                    title: 'Previous Location',
                    nextState: 'history_back'
                },
                bubble2: {
                    bid: 'bubble2',
                    title: 'View Current Location',
                    nextView: 'location',
                    nextState: 'atLocation'
                },
                bubble3: {
                    bid: 'bubble3',
                    title: 'Next Location',
                    nextState: 'history_forward'
                }
            }
        };

        gameStatePerKey = {
            beginPlay: gameStates.beginPlay,
            atLocation: gameStates.atLocation,
            riding: gameStates.riding,
            history: gameStates.history
        };

        state.currentGameState = gameStatePerKey[state.currentGameStateKey];
    }

}(window.angular));