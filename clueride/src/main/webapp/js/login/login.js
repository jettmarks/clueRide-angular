(function (angular) {
    'use strict';

    var viewModel,
        localModel = {};

    angular
        .module('crPlayer.LoginModule', ['ngResource'])
        .controller('LoginController', LoginController)
        .factory('LoginResource', LoginResource)
    ;

    LoginController.$inject = ['LoginResource'];

    function LoginController(LoginResource) {
        var vm = this;

        vm.loginName = "";
        vm.password = "";
        vm.submit = login;
        vm.badges = {};

        localModel.loginResource = LoginResource;

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
        viewModel.badges = data;
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