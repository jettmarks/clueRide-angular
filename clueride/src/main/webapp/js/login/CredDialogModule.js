(function (angular) {
    'use strict';

    var viewModel,
        localModel;

    angular
    /**
     * Handles presenting the credential dialog and then once credentials are
     * supplied, performs a check and establishes a session if worthy of a session.
     * Upon submission, the URL is redirected back to the main app whereupon the
     * existence of a valid session allows reaching the app and otherwise comes
     * back to this app.
     */
        .module('crPlayer.CredentialsDialogModule', [
            'ngResource',
            'login.Bubble',
            'mobile-angular-ui'
        ])
        .controller('CredentialDialogController', CredentialDialogController)
        .factory('CredentialResource', CredentialResource)
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

    function CredentialDialogController () {
        var vm = this;
        viewModel = vm;
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

    function CredentialResource ($resource) {
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

}(window.angular));