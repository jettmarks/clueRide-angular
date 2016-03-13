(function (angular) {
    'use strict';

    angular
        .module('cluerideLogin', [
            'ngRoute',
            'login.Bubble',
            'crPlayer.joinTeam',
            'mobile-angular-ui'
        ])
        .controller('CredentialController', CredentialController)
        .config(function($routeProvider) {
            $routeProvider.when('/',
                {
                    templateUrl: 'js/login/loginBubbles.html',
                    reloadOnSearch: false
                }
            );
        })
    ;

    CredentialController.$inject = ['$rootScope'];

    function CredentialController($rootScope) {
        var vm = this;

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

        vm.exposedObject = {};
        //vm.exposedFunction = exposedFunction;
    }

}(window.angular));