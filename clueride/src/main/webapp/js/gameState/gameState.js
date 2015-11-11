(function (angular) {
    'use strict';

    var module = angular.module('gameState', []),
        gameStates = {},
        gameStatePerKey = [],
        currentLocationIndex = 0,
        currentGameState;

    module.run(function () {

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
              },
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
              },
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
                nextView: 'chooseLocation',
                nextState: 'riding'
              },
            }
        };
        gameStatePerKey = {
            beginPlay: gameStates.beginPlay,
            atLocation: gameStates.atLocation,
            riding: gameStates.riding
        };

        currentGameState = currentGameState || gameStatePerKey['beginPlay'];
    });

    module.service('gameStateService', function () {
        return {
            currentGameState: function () {return currentGameState},
            updateGameState: function (stateName) {
                currentGameState = gameStatePerKey[stateName];
            }
        };
    });

}(window.angular));