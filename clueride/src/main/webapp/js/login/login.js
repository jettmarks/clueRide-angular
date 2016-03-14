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
        .run(init)
    ;

    /** Records dependencies used in this scope. */
    function init(
        $window,
        LoginResource,
        BadgesService,
        GameStateService
    ) {
        localModel.window = $window;
        localModel.loginResource = LoginResource;
        localModel.badgesService = BadgesService;
        localModel.gameStateService = GameStateService;
    }

    function LoginController(
    ) {
        var vm = this;

        vm.loginName = "";
        vm.password = "";
        vm.submit = login;
        vm.logout = logout;
        vm.badges = {};

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
        localModel.gameStateService.updateGameState('beginPlay');
        localModel.gameStateService.disableGpsBubble();
        localModel.gameStateService.disablePlay();
        localModel.window.location.href = '/login.html';
    }

    function getBadges() {
        localModel.loginResource.get({}, receiveBadges);
    }

    function receiveBadges(data) {
        localModel.badgesService.saveBadges(data);
        localModel.gameStateService.updateLoginState();
    }

    // TODO: Move over to the Credential Service
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
            },
            get: {
                method: 'GET',
                isArray: true
            }
        });
    }

    function LoginService() {
        // TODO: Move this into the Badges Module
        return {
            getBadges: getBadges
        }
    }

}(window.angular));