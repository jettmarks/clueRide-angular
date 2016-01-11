(function (angular) {
    'use strict';

    var viewModel;

    angular
        .module('crNetEdit.NodeAddModule', ['leaflet-directive', 'ngResource'])
        .controller('NodeAddController', NodeAddController)
        .factory('NodeAddResource', NodeAddResource)
    ;

    NodeAddController.$inject = ['$scope'];

    function NodeAddController ($scope) {
        var vm = this;

        $scope.name = {name: "NodeAddModule"};

        viewModel = vm;
    }


    function NodeAddResource ($resource) {
        return $resource('/crMain/rest/nodes/new', {}, {
            check: {
                /* Propose new lat/lon pair, and tell us if it is on the network. */
                method: 'POST',
                params: {},
                isArray: false
            },
            request: {
                /* Given a set of recommendations, get the geometry for a particular rec. */
                method: 'GET',
                params: {},
                isArray: false
            },
            confirm: {
                /* Tell the server which recommendation to accept. */
                method: 'PUT',
                params: {},
                isArray: false
            }
        });
    }

}(window.angular));
