(function (angular) {
    'use strict';

    var viewModel,
        gameStateService;

    angular
        .module('crLocation',['gameState'])
        .controller('LocationController', LocationController)
        .directive('backImg', backImg)
    ;

    LocationController.$inject = ['$scope', 'gameStateService'];

    function LocationController($scope, gameStateService) {
        var locIndex = gameStateService.currentIndex();

        viewModel = this;
        $scope.viewModel = viewModel;
        viewModel.gameStateService = gameStateService;

        viewModel.locations = [
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

        $scope.location = viewModel.locations[locIndex];

        // Zero-based index vs. one-based page number
        viewModel.currentPage = locIndex+1;
        viewModel.pageChanged = pageChanged;
    }

    function pageChanged(gameStateService) {
        var locIndex = $scope.currentPage-1;
        $scope.location = viewModel.locations[locIndex];
        gameStateService.setCurrentLocation(locIndex);
    }

    function backImg () {
        return function(scope, element, attrs) {
            var url = attrs.backImg;
            element.css({
                'background-image': 'url(' + url +')',
                'background-size' : 'cover'
            });
        };
    }

}(window.angular));
