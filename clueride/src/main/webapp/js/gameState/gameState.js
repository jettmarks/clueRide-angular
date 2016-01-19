(function (angular) {
    'use strict';

    var viewModel,
        gameStates = {},
        gameStatePerKey = [],
        currentLocationIndex = 0,
        courseDataResource,
        currentGameState;

    angular
        .module('gameState', ['common.CourseResource'])
        .controller('GameStateController',GameStateController)
        .service('gameStateService', gameStateService)
        .run(gameStateInit)
    ;

    GameStateController.$inject = ['$scope', 'CourseDataResource'];

    function GameStateController($scope, CourseDataResource) {
        var vm = this;

        courseDataResource = CourseDataResource;
    }

    function dataToModel(course) {
        viewModel.course = course;
    }

    function gameStateInit (CourseDataResource) {
        CourseDataResource.getData({
            /* Future: put the course ID here. */
        }, dataToModel);

        gameStates = {
            beginPlay: {
              title: 'Get Ready',
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
                nextState: 'riding'
              }
            }
        };
        gameStatePerKey = {
            beginPlay: gameStates.beginPlay,
            atLocation: gameStates.atLocation,
            riding: gameStates.riding
        };

        currentGameState = currentGameState || gameStatePerKey['beginPlay'];


    }

    function setCourseScope(courseScope) {
        viewModel = courseScope;
    }

    function gameStateService () {
        return {
            currentGameState: function () {return currentGameState},
            updateGameState: function (stateName) {
                currentGameState = gameStatePerKey[stateName];
            },
            currentLocation: function () {
                return {location: {
                    name: 'BeltLine'
                }};
            },
            setCurrentLocation: function (newIndex) {
                currentLocationIndex = newIndex;
            },
            currentIndex: function () {
                return currentLocationIndex;
            },
            setCourseScope: setCourseScope
        };
    }

}(window.angular));