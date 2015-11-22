(function (angular) {
    'use strict';

    var module = angular.module('clue', ['ui.bootstrap']);

    module.controller('ClueController', ['$scope',
        function ($scope) {
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
            }
        }
    ]);

}(window.angular));