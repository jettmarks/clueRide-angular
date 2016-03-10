(function (angular) {
    'use strict';

    var viewModel,
        localModel = {
            badgesService: function () {},
            courseDataResource: {},
            courseLocationDataResource: {}
        },
        gameStates = {},
        gameStateResource = {},
    /* Overall Game State which is shared across an Outing (Team/Course combo). */
        outingState = {
            // TODO: hardcoded selection for the outing
            outingId: 2,
            teamConfirmed: false,
            /* Incremented as clues are solved and destinations reached. */
            pathIndex: -1,
            mostRecentClueSolvedFlag: false
        },

        locationState = {},
        state = {
            /* Reviews history of opened Paths -- capped by pathIndex. */
            historyIndex: 0,

            /* TODO: CA-149 */
            teamId: 42,

            currentGameState: {},
            maxVisibleLocationIndex: 0,
            currentGameStateKey: 'beginPlay'
        };

    angular
        .module('crPlayer.GameState', ['common.CourseResource'])
        .controller('GameStateController', GameStateController)
        .service('GameStateService', GameStateService)
        .factory('GameStateResource', GameStateResource)
        .factory('OutingStateResource', OutingStateResource)
        .run(gameStateInit)
    ;

    GameStateController.$inject = ['$scope',
        'CourseDataResource',
        'CourseLocationDataResource'
    ];

    function GameStateController(
        $scope,
        CourseDataResource,
        CourseLocationDataResource
    ) {
        $scope.vm = this;

        localModel.courseDataResource = CourseDataResource;
        localModel.courseLocationDataResource = CourseLocationDataResource;
    }

    function dataToModel(course) {
        viewModel.course = course;
    }

    function courseLocationsToModel(locations) {
        viewModel.locations = locations;
    }

    function setCourseScope(courseScope) {
        viewModel = courseScope;

        /* We publish these two states; first one to the server and second one on the client. */
        viewModel.outingState = outingState;
        viewModel.state = state;

        /* We 'subscribe' to this state. */
        locationState = viewModel.locationState;
    }

    /* Events changing Game State: */
    function clueSolve() {
        if (localModel.badgesService.hasBadge('TEAM_LEADER')) {
            outingState.mostRecentClueSolvedFlag = true;
            outingState.pathIndex++;
            updateGameState('riding');
        } else {
            // TODO: Post your result to the server
        }
    }

    /** Called when user logs in and badges have been received. */
    function updateLoginState() {
        if (localModel.badgesService.hasBadge('TEAM_MEMBER')) {
            enableGpsBubble();
        }
    }

    /** Called when user logs in and badges have been received. */
    function updateGpsState() {
        if (localModel.badgesService.hasBadge('TEAM_LEADER')) {
            enablePlay();
        }
    }

    function arrived() {
        gameStates['atLocation'].locationIndex++;
        updateGameState('atLocation');
        outingState.mostRecentClueSolvedFlag = false;
    }

    /* Upon all members having arrived and setting up to play, Team Leader gives signal play can begin. */
    function confirmTeam() {
        outingState.teamConfirmed = true;
        localModel.outingStateResource.save(outingState);
        //arrived();
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
        if (state.historyIndex > outingState.pathIndex) {
            /* Cap the history Index to avoid walking over the edge. */
            state.historyIndex = outingState.pathIndex;
            if (outingState.mostRecentClueSolvedFlag) {
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

    function getOutingState() {
        localModel.outingStateResource.get().$promise.then(
            function (data) {
                if (data) {
                    if (data.teamConfirmed) {
                        enablePlay();
                    }
                    viewModel.outingState = data;
                } else {
                    console.log("State not yet set for the session");
                }
            }
        );
    }

    function enableGpsBubble() {
        state.currentGameState.bubble2.disabled = false;
    }

    function disableGpsBubble() {
        state.currentGameState.bubble2.disabled = true;
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
        if (outingState.pathIndex < 0) {
            return 0;
        }
        return state.historyIndex;
    }

    function getPathIndex() {
        return outingState.pathIndex;
    }

    function setPathIndex(newPathIndex) {
        outingState.pathIndex = newPathIndex;
        state.pathId = getPathId();
        if (newPathIndex === -1) {
            updateGameState('beginPlay');
        }
    }

    function getPathId() {
        /* We don't show a path if the first clue hasn't been solved. */
        if (outingState.pathIndex < 0) {
            return undefined;
        } else {
            if (viewModel.course.pathIds) {
                return viewModel.course.pathIds[outingState.pathIndex];
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
        for (var i = outingState.pathIndex-1; i>=0; i--) {
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

            refreshLocationData: refreshLocationData,

            currentGameState: function () {return state.currentGameState},
            currentGameStateKey: function () {return state.currentGameStateKey},
            updateGameState: updateGameState,

            /* Events triggering change of state. */
            /* May not be the best trigger event. */
            updateLoginState: updateLoginState,
            updateGpsState: updateGpsState,
            clueSolved: clueSolve,
            arrived: arrived,
            confirmTeam: confirmTeam,

            setHistoryLocation: function (newIndex) {
                state.historyIndex = newIndex;
                viewModel.locationState.location = viewModel.locations[newIndex];
            },

            enableGpsBubble: enableGpsBubble,
            disableGpsBubble: disableGpsBubble,
            enablePlay: enablePlay,

            /* Queries */
            getOutingState: getOutingState,

            /* Flags. */
            mostRecentClueSolved: function () {
                return outingState.mostRecentClueSolvedFlag;
            },

            /* Indices. */
            getLocationIndex: getLocationIndex,
            getPathIndex: getPathIndex,
            setPathIndex: setPathIndex,
            getPathId: getPathId,
            getCompletedPathIds: getCompletedPathIds,

            maxVisibleLocationIndex: function () {
                var candidateIndex = outingState.pathIndex;
                if (candidateIndex < 0) {
                    return 0;
                } else if (outingState.mostRecentClueSolvedFlag) {
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

    function OutingStateResource($resource) {
        return $resource('/rest/gameState', {}, {

        });
    }

    function stateToModel(data) {
        viewModel.outingState.pathIndex = data.pathIndex;
        viewModel.outingState.mostRecentClueSolvedFlag = data.mostRecentClueSolvedFlag;
        if (data.currentGameStateKey) {
            updateGameState(data.currentGameStateKey);
        } else {
            updateGameState('beginPlay');
        }
    }

    function refreshLocationData() {
        localModel.courseLocationDataResource.getData({
            /* Future: put the course ID here too. */
        }, courseLocationsToModel);
    }

    function gameStateInit (
        CourseDataResource,
        GameStateResource,
        CourseLocationDataResource,
        BadgesService,
        OutingStateResource
    ) {
        localModel.courseLocationDataResource = CourseLocationDataResource;
        localModel.badgesService = BadgesService;
        localModel.outingStateResource = OutingStateResource;

        CourseDataResource.getData({
            /* Future: put the course ID here. */
        }, dataToModel);

        refreshLocationData();

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