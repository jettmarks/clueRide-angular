(function (angular) {
    'use strict';

    var viewModel,
        localModel;

    angular
        .module('crPlayer.CredentialsModule', ['ngResource'])
        .factory('CredentialResource', CredentialResource)

        //.controller('Controller', Controller)
        //.service('Service', Service)
        //.directive('crEditMode', crEditMode)
        //.run(init)
    ;

    /** Records dependencies used in this scope. */
    //function init($location) {
    //    localModel.loginResource = LoginResource;
    //}

    //Controller.$inject = ['dependencies'];
    //
    //function Controller(dependencies) {
    //    var vm = this;
    //
    //    vm.exposedObject = {};
    //    vm.exposecFunction = exposedFunction;
    //}

    //function Service() {
    //    return {
    //        request: requestFunction,
    //        confirm: confirmFunction
    //    };
    //}

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