(function (angular) {
    'use strict';

    var viewModel,
        localModel;

    angular
        .module('crPlayer.joinTeam', ['crPlayer.CredentialsModule'])
        .controller('JoinTeamController', JoinTeamController)
        .run(init)
    ;

    /** Records dependencies used in this scope. */
    function init(
        $window,
        CredentialResource
    ) {
        localModel = {
            windowLocation: $window.location,
            credentialResource: CredentialResource
        };
    }

    //JoinTeamController.$inject = [''];

    function JoinTeamController() {
        var vm = this;
        viewModel = vm;

        //vm.exposedObject = {};
        vm.login = login;
    }

    function login() {
        localModel.credentialResource.login(
            {
                name: viewModel.loginName,
                password: viewModel.password
            }
        )
            /* Promise is used to allow retrieving the badge and determine if we can open next bubble. */
            .$promise
            .then(function (data) {
                console.log("Received " + data);
                localModel.windowLocation.href = '/index.html';
            }
        );
    }

}(window.angular));