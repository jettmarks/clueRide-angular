(function (angular) {
    'use strict';

    var module;

    function LoadingController($rootScope) {
        var loadingOverlay = this;

        console.log("'loadingOverlay' is " + loadingOverlay.loading.title + " & " + loadingOverlay.loading.flag);

        /**
         * Start-up logic for initializing the controller.
         */
        function activate() {
            console.log("activating loading directive");
            loadingOverlay.loading.title = 'buckling my helmet';
            loadingOverlay.loading.flag = false;

            //Needed for the loading screen
            $rootScope.$on('$routeChangeStart', function(){
                $rootScope.loading.flag = true;
            });

            $rootScope.$on('$routeChangeSuccess', function(){
                $rootScope.loading.flag = false;
            });
        }

        /* Initialize the controller. */
        activate();
    }

    function loadingOverlay() {
        console.log("initializing directive");
        return {
            scope: {},
            bindToController: {
                loading: "="
            },
            restrict: 'EA',
            templateUrl: 'js/loading/loading.html',
            controller: LoadingController,
            controllerAs: 'loadingOverlay'
        };
    }

    function setTitle(newTitle) {
        loadingOverlay.loading.title = newTitle;
    }

    function LoadingService() {
        return {
            setTitle: setTitle
        }
    }

    module = angular.module(
        'crPlayer.loading',
        [
        ]
    );
    module.directive('loadingOverlay', loadingOverlay);

    module.factory('LoadingService', LoadingService);

}(window.angular));
