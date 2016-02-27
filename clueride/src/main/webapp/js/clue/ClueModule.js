(function (angular) {
    'use strict';

    var self = {};

    angular
        .module('crPlayer.ClueModule', ['ui.bootstrap', 'crPlayer.GameState'])
        .controller('ClueController', ClueController)
    ;

    ClueController.$inject = ['$scope', 'GameStateService'];

    function ClueController($scope, GameStateService) {
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

        self.gameStateService = GameStateService;
    }

    /* Right now we're accepting everything as a correct answer. */
    function checkAnswer() {
        self.gameStateService.clueSolved();
    }

}(window.angular));