(function (angular) {
    'use strict';

    var self = {};

    angular
        .module('clue', ['ui.bootstrap', 'gameState'])
        .controller('ClueController', ClueController);

    ClueController.$inject = ['$scope', 'gameStateService'];

    function ClueController($scope, gameStateService) {
        $scope.clue = {
            question: "What is the answer to 'Life, the Universe and Everything'?",
            answers: [
                { A: "Do unto others as you would have them do unto you"},
                { B: "Same as the airspeed of an African swallow"},
                { C: "42"},
                { D: "Crunchy Frog"}
            ],
            correctAnswer: 'C',
            points: 5
        };

        $scope.submit = checkAnswer;

        self.gameStateService = gameStateService;
    }

    /* Right now we're accepting everything as a correct answer. */
    function checkAnswer() {
        self.gameStateService.clueSolved();
    }

}(window.angular));