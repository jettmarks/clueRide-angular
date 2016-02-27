(function (angular) {
    'use strict';

    var viewModel,
        localModel = {};

    angular
        .module('crPlayer.LoginModule', [
            'ngResource',
            'ngCookies',
            'crPlayer.BadgesModule',
            'crPlayer.GameState'
        ])
        .controller('LoginController', LoginController)
        .factory('LoginResource', LoginResource)
        .factory('LoginService', LoginService)
    ;

    LoginController.$inject = ['$cookies', 'LoginResource','BadgesService', 'GameStateService'];

    function LoginController($cookies, LoginResource, BadgesService, GameStateService) {
        var vm = this;

        vm.loginName = "";
        vm.password = "";
        vm.submit = login;
        vm.badges = {};

        localModel.cookieService = $cookies;
        localModel.loginResource = LoginResource;
        localModel.badgesService = BadgesService;
        localModel.gameStateService = GameStateService;

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
        /* The viewModel exists when we are logging in, but not when we refresh the page. */
        if (viewModel) {
            saveLogin(viewModel);
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
        return $resource('/rest/login', {}, {
            login: {
                method: 'POST',
                isArray: true
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