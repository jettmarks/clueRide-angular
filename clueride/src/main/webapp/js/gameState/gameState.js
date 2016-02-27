(function (angular) {
    'use strict';

    var viewModel,
        gameStates = {},
        courseDataResource = {},
        courseLocationDataResource = function () {},
        gameStateResource = {},
    /* Consider user state versus overall game/team state. */
        locationState = {},
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
        .module('crPlayer.GameState', ['common.CourseResource'])
        .controller('GameStateController', GameStateController)
        .service('GameStateService', GameStateService)
        .factory('GameStateResource', GameStateResource)
        .run(gameStateInit)
    ;

    GameStateController.$inject = ['$scope', 'CourseDataResource', 'CourseLocationDataResource'];

    function GameStateController($scope, CourseDataResource, CourseLocationDataResource) {
        $scope.vm = this;

        courseDataResource = CourseDataResource;
        courseLocationDataResource = CourseLocationDataResource;
    }

    function dataToModel(course) {
        viewModel.course = course;
    }

    function courseLocationsToModel(locations) {
        viewModel.locations = locations;
    }

    function setCourseScope(courseScope) {
        viewModel = courseScope;
        /* We 'publish' this state. */
        viewModel.state = state;
        /* We 'subscribe' to this state. */
        locationState = viewModel.locationState;
    }

    /* Events changing Game State: */
    function clueSolve() {
        state.mostRecentClueSolvedFlag = true;
        state.pathIndex++;
        updateGameState('riding');
    }

    function arrived() {
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
            state.currentGameState = gameStates[newState];
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
            /* Cap the history Index to avoid walking over the edge. */
            state.historyIndex = state.pathIndex;
            if (state.mostRecentClueSolvedFlag) {
                updateGameState('riding');
            } else {
                updateGameState('atLocation');
            }
        } else {
            state.currentGameStateKey = 'history';
            state.currentGameState = gameStates['history'];
            state.currentGameState.title = viewModel.locations[state.historyIndex].name;
        }
    }

    function enableGpsBubble() {
        state.currentGameState.bubble2.disabled = false;
    }

    function enablePlay() {
        state.currentGameState.bubble3.disabled = false;
    }

    /**
     * This is based on both the Path Index and walking the History Index as well as taking into account
     * that we'll want to show some details of the initial location prior to "arriving" at that location.
     */
    function getLocationIndex() {
        // Let's see if this works
        if (state.pathIndex < 0) {
            return 0;
        }
        return state.historyIndex;
    }

    function getPathIndex() {
        return state.pathIndex;
    }

    function setPathIndex(newPathIndex) {
        state.pathIndex = newPathIndex;
        state.pathId = getPathId();
        if (newPathIndex === -1) {
            updateGameState('beginPlay');
        }
    }

    function getPathId() {
        /* We don't show a path if the first clue hasn't been solved. */
        if (state.pathIndex < 0) {
            return undefined;
        } else {
            if (viewModel.course.pathIds) {
                return viewModel.course.pathIds[state.pathIndex];
            } else {
                return undefined;
            }
        }
    }

    /**
     * Returns an array of the completed Path IDs, possibly empty.
     * @returns {Array}
     */
    function getCompletedPathIds() {
        var result = [];
        for (var i = state.pathIndex-1; i>=0; i--) {
            if (viewModel.course.pathIds) {
                result.push(viewModel.course.pathIds[i]);
            }
        }
        return result;
    }

    function GameStateService () {
        return {
            // TODO: Find better way to set the scope; link in Controller
            setCourseScope: setCourseScope,

            currentGameState: function () {return state.currentGameState},
            currentGameStateKey: function () {return state.currentGameStateKey},
            updateGameState: updateGameState,

            /* Events triggering change of state. */
            /* May not be the best trigger event. */
            clueSolved: clueSolve,
            arrived: arrived,

            setHistoryLocation: function (newIndex) {
                state.historyIndex = newIndex;
                viewModel.locationState.location = viewModel.locations[newIndex];
            },

            enableGpsBubble: enableGpsBubble,
            enablePlay: enablePlay,

            /* Flags. */
            mostRecentClueSolved: function () {
                return state.mostRecentClueSolvedFlag;
            },

            /* Indices. */
            getLocationIndex: getLocationIndex,
            getPathIndex: getPathIndex,
            setPathIndex: setPathIndex,
            getPathId: getPathId,
            getCompletedPathIds: getCompletedPathIds,

            maxVisibleLocationIndex: function () {
                var candidateIndex = state.pathIndex;
                if (candidateIndex < 0) {
                    return 0;
                } else if (state.mostRecentClueSolvedFlag) {
                    return candidateIndex + 1;
                } else {
                    return candidateIndex;
                }
            }
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
        viewModel.state.pathIndex = data.pathIndex;
        viewModel.state.mostRecentClueSolvedFlag = data.mostRecentClueSolvedFlag;
        if (data.currentGameStateKey) {
            updateGameState(data.currentGameStateKey);
        } else {
            updateGameState('beginPlay');
        }
    }

    function gameStateInit (CourseDataResource, GameStateResource, CourseLocationDataResource) {
        CourseDataResource.getData({
            /* Future: put the course ID here. */
        }, dataToModel);

        CourseLocationDataResource.getData({
            /* Future: put the course ID here too. */
        }, courseLocationsToModel);

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
                    nextState: 'beginPlay',
                    disabled: true
                },
                bubble3: {
                    bid: 'bubble3',
                    title: 'Play',
                    nextView: '',
                    nextState: 'atLocation',
                    disabled: true
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
                    title: 'Path to Next Location',
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
                    dialog: 'clueNotSolved',
                    nextState: 'riding',
                    disabled: true
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

        state.currentGameState = gameStates[state.currentGameStateKey];
    }

}(window.angular));