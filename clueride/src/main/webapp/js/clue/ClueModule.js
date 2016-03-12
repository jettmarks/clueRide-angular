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

        self = $scope;

        ClueResource.get({clueId: clueId}, function (data) {
            $scope.clue = data;
            $scope.clue.selected = 'A';
        });

        $scope.submit = checkAnswer;
        $scope.cancel = cancel;

        self.gameStateService = GameStateService;
    }

    function checkAnswer() {
        if (self.clue.selected === self.clue.correctAnswer) {
            self.gameStateService.clueSolved();
        } else {
            console.log("Incorrect answer: " + self.clue.selected);
        }
    }

    function cancel() {}

}(window.angular));