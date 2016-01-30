(function (angular) {
    'use strict';

    var viewModel,
        gameStateService;

    angular
        .module('crLocation',['crPlayer.GameState'])
        .controller('LocationController', LocationController)
        .directive('backImg', backImg)
        .service('locationService', locationService)
    ;

    LocationController.$inject = ['$scope', 'gameStateService'];

    function LocationController($scope, gameStateService) {
        //var locIndex = gameStateService.currentIndex();
        var locIndex = gameStateService.getLocationIndex();

        viewModel = this;
        $scope.viewModel = viewModel;
        viewModel.gameStateService = gameStateService;

        // TODO: Read these from the Course/Path service (CA-127)
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
        gameStateService.setHistoryLocation(locIndex);
    }

    function getLocationCount() {
        // TODO: CA-127
        //return viewModel.locations.length;
        return 6;
    }

    function locationService() {
        return {
            getLocationCount: getLocationCount
        }
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
