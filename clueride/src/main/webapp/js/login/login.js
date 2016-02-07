(function (angular) {
    'use strict';

    var viewModel,
        localModel = {};

    angular
        .module('crPlayer.LoginModule', ['ngResource', 'crPlayer.BadgesModule'])
        .controller('LoginController', LoginController)
        .factory('LoginResource', LoginResource)
    ;

    LoginController.$inject = ['LoginResource','BadgesService'];

    function LoginController(LoginResource, BadgesService) {
        var vm = this;

        vm.loginName = "";
        vm.password = "";
        vm.submit = login;
        vm.badges = {};

        localModel.loginResource = LoginResource;
        localModel.badgesService = BadgesService;

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