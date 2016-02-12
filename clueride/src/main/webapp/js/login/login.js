(function (angular) {
    'use strict';

    var viewModel,
        localModel = {};

    angular
        .module('crPlayer.LoginModule', ['ngResource', 'crPlayer.BadgesModule','crPlayer.GameState'])
        .controller('LoginController', LoginController)
        .factory('LoginResource', LoginResource)
    ;

    LoginController.$inject = ['LoginResource','BadgesService', 'gameStateService'];

    function LoginController(LoginResource, BadgesService, gameStateService) {
        var vm = this;

        vm.loginName = "";
        vm.password = "";
        vm.submit = login;
        vm.badges = {};

        localModel.loginResource = LoginResource;
        localModel.badgesService = BadgesService;
        localModel.gameStateService = gameStateService;

        viewModel = vm;
    }

    function login() {
        localModel.loginResource.login({
                name: viewModel.loginName,
                password: viewModel.password
            },
        receiveBadges);
    }

    function receiveBadges(data) {
        localModel.badgesService.saveBadges(data);
        localModel.gameStateService.enableGpsBubble();
    }

    function LoginResource ($resource) {
        return $resource('/rest/login', {}, {
            login: {
                method: 'POST',
                isArray: true
            }
        });
    }

}(window.angular));