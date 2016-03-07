(function (angular) {
    'use strict';

    var viewModel,
        localModel = {};

    angular
        .module('crPlayer.LoginModule', [
            'ngResource',
            'ngRoute',
            'crPlayer.BadgesModule',
            'crPlayer.GameState'
        ])
        .controller('LoginController', LoginController)
        .factory('LoginResource', LoginResource)
        .factory('LoginService', LoginService)
    ;

    LoginController.$inject = [
        '$location',
        'LoginResource',
        'BadgesService',
        'GameStateService'
    ];

    function LoginController(
        $location,
        LoginResource,
        BadgesService,
        GameStateService
    ) {
        var vm = this;

        vm.loginName = "";
        vm.password = "";
        vm.submit = login;
        vm.logout = logout;
        vm.badges = {};

        localModel.loginResource = LoginResource;
        localModel.badgesService = BadgesService;
        localModel.gameStateService = GameStateService;
        localModel.locationService = $location;

        viewModel = vm;
    }

    function login() {
        localModel.loginResource.login(
            {
                name: viewModel.loginName,
                password: viewModel.password
            }
        )
        /* Promise is used to allow retrieving the badge and determine if we can open next bubble. */
            .$promise
            .then(function (data) {
                receiveBadges(data)
            }
        );
    }

    function logout() {
        localModel.badgesService.clearBadges();
        localModel.loginResource.logout();
        localModel.locationService.path("/");
    }

    function receiveBadges(data) {
        localModel.badgesService.saveBadges(data);
        localModel.gameStateService.updateLoginState();
    }

    function LoginResource ($resource) {
        /* Submit credentials and receive badges. */
        return $resource('/rest/login', {}, {
            /* Open session with the server. */
            login: {
                method: 'POST',
                isArray: true
            },
            /* Close out the session with the server. */
            logout: {
                method: 'DELETE',
                isArray: false
            }
        });
    }

    function LoginService() {
        return {
            // Currently unused
        }
    }

}(window.angular));