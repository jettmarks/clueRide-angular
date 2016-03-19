(function (angular) {
    'use strict';

    angular
        /** This module loads when we are requesting the credentials for a user's session. */
        .module('crPlayer.CredentialsModule', [
            'ngRoute',
            'mobile-angular-ui',
            'crPlayer.CredentialsDialogModule',
            'login.Bubble'
        ])
        /* Display a limited Bubble Screen with a single action. */
        .config(function($routeProvider) {
            $routeProvider.when('/',
                {
                    templateUrl: 'js/login/loginBubbles.html',
                    reloadOnSearch: false
                }
            )
        })
        .controller('CredentialController', CredentialController)
    ;

    CredentialController.$inject = ['$rootScope'];

    function CredentialController($rootScope) {
        $rootScope.loading = false;
        $rootScope.showHeaderFooter = false;

        $rootScope.loginBubbles = {
            title: 'Get Ready',
            bubble1: {
                bid: 'bubble1',
                title: 'Join a Team?',
                dialog: 'joinTeam',
                nextView: '',
                nextState: 'beginPlay'
            },
            bubble2: {
                bid: 'bubble2',
                title: 'GPS?',
                dialog: 'setGpsMode',
                nextView: '',
                nextState: 'beginPlay',
                disabled: true
            },
            bubble3: {
                bid: 'bubble3',
                title: 'Play',
                nextView: '',
                nextState: 'atLocation',
                disabled: true
            }
        };
    }

}(window.angular));