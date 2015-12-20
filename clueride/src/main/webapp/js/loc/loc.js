(function (angular) {
    'use strict';

    var module = angular.module('crLocation',['gameState'])
      .controller('LocationController', [
        '$scope', 'gameStateService',
        function ($scope, gameStateService) {
      var locIndex = gameStateService.currentIndex(),
          locations = [
        {
            name: "Peace Monument, Piedmont Park",
            description: "Long bit of text we'll pull together.",
            imgUrl: [
                'http://img.clueride.com/img/1/1.jpg',
                'http://img.clueride.com/img/1/2.jpg',
                'http://img.clueride.com/img/1/3.jpg'
            ]
        },
        {
            name: "BeltLine - White Rhino",
            description: "We love the BeltLine",
            imgUrl: ['http://img.clueride.com/img/2/1.jpg']
        },
        {
            name: "Gandhi",
            description: "Inspired MLK",
            imgUrl: ['http://img.clueride.com/img/3/1.jpg']
        },
        {
            name: "Sweet Auburn Curb Market",
            description: "Lots under one roof",
            imgUrl: ['http://img.clueride.com/img/4/1.jpg']
        },
        {
            name: "Oakland Cemetery",
            description: "Visit with the residents",
            imgUrl: ['http://img.clueride.com/img/5/1.jpg']
        }
      ];

      $scope.location = locations[locIndex];
      // Zero-based index vs. one-based page number
      $scope.currentPage = locIndex+1;
      $scope.pageChanged = function () {
        var locIndex = $scope.currentPage-1;
        $scope.location = locations[locIndex];
        gameStateService.setCurrentLocation(locIndex);
      }

    }]);

    module.directive('backImg', function() {
        return function(scope, element, attrs) {
            var url = attrs.backImg;
            element.css({
                'background-image': 'url(' + url +')',
                'background-size' : 'cover'
            });
        };
    });

}(window.angular));
