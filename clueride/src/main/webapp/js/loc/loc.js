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
            imgUrl: ['./image/loc/10/1.jpg']
        },
        {
            name: "BeltLine - White Rhino",
            description: "We love the BeltLine",
            imgUrl: ['./image/loc/10/2.jpg']
        },
        {
            name: "Gandhi",
            description: "Inspired MLK",
            imgUrl: ['./image/loc/10/3.jpg']
        },
        {
            name: "Sweet Auburn Curb Market",
            description: "Lots under one roof",
            imgUrl: ['./image/loc/10/4.jpg']
        },
        {
            name: "Oakland Cemetery",
            description: "Visit with the residents",
            imgUrl: ['./image/loc/10/5.jpg']
        }
      ];

      $scope.location = locations[locIndex];
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
