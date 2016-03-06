(function (angular) {
    'use strict';

    var viewModel,
        localModel = {};

    angular
        .module('crPlayer.LoginModule', [
            'ngResource',
            'ngCookies',
            'ngRoute',
            'crPlayer.BadgesModule',
            'crPlayer.GameState'
        ])
        .controller('LoginController', LoginController)
        .factory('LoginResource', LoginResource)
        .factory('LoginService', LoginService)
    ;

    LoginController.$inject = [
        '$cookies',
        '$location',
        'LoginResource',
        'BadgesService',
        'GameStateService'
    ];

    function LoginController(
        $cookies,
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

        // TODO: Won't need cookies anymore for authentication
        localModel.cookieService = $cookies;
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
        if (localModel.badgesService.hasBadge('TEAM_MEMBER')) {
            localModel.gameStateService.enableGpsBubble();
        }
    }

    /**
     * Using the credentials stored in a cookie, retrieve the badges.
     * TODO: CA-186 Make this use an authentication token instead of plain text.
     */
    function checkLogin() {
        var loginName, password;
        loginName = localModel.cookieService.get('loginName');
        password = localModel.cookieService.get('password');

        /* Only retrieve if we know what to retrieve (and the cookie hasn't expired). */
        if (loginName) {
            localModel.loginResource.login({
                name: loginName,
                password: password
            }, receiveBadges);
        }
    }

    /**
     * After authenticating the user, save the credentials as cookies.
     * TODO: CA-186 Turn this into a token that is used to record an authenticated session.
     * @param login - object containing credentials.
     */
    function saveLogin(login) {
        // TODO: CA-187 Add options for expiring the cookies after 24 hours
        localModel.cookieService.put('loginName', login.loginName);
        localModel.cookieService.put('password', login.password)
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

    // TODO: May be able to refactor setting up of the dependencies into a single spot
    function LoginService($cookies, LoginResource, GameStateService, BadgesService) {
        localModel.cookieService = $cookies;
        localModel.loginResource = LoginResource;
        localModel.gameStateService = GameStateService;
        localModel.badgesService = BadgesService;

        return {
            checkLogin: checkLogin
        }
    }

}(window.angular));