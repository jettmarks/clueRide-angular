(function (angular) {
    'use strict';

    var self = {};

    angular
        .module('crPlayer.ClueModule', ['ui.bootstrap', 'crPlayer.GameState','common.ClueResource'])
        .controller('ClueController', ClueController)
    ;

    ClueController.$inject = ['$scope', 'GameStateService','ClueResource'];

    function ClueController($scope, GameStateService, ClueResource) {
        //$scope.clue = {
        //    question: "What is the answer to 'Life, the Universe and Everything'?",
        //    answers: [
        //        { A: "Do unto others as you would have them do unto you"},
        //        { B: "Same as the airspeed of an African swallow"},
        //        { C: "42"},
        //        { D: "Crunchy Frog"}
        //    ],
        //    correctAnswer: 'C',
        //    points: 5
        //};

        var clueId = GameStateService.getCurrentClueId();
        ClueResource.get({clueId: clueId}, function (data) {
            $scope.clue = data;
        });

        $scope.submit = checkAnswer;

        self.gameStateService = GameStateService;
    }

    /* Right now we're accepting everything as a correct answer. */
    function checkAnswer() {
        self.gameStateService.clueSolved();
    }

}(window.angular));