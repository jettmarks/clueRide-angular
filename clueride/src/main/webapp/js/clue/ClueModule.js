(function (angular) {
    'use strict';

    var self = {};

    angular
        .module('crPlayer.ClueModule', ['ui.bootstrap', 'crPlayer.GameState','common.ClueResource'])
        .controller('ClueController', ClueController)
    ;

    ClueController.$inject = ['$scope', 'GameStateService','ClueResource'];

    function ClueController($scope, GameStateService, ClueResource) {
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
            window.alert("Correct!");
        } else {
            console.log("Incorrect answer: " + self.clue.selected);
        }
    }

    function cancel() {}

}(window.angular));