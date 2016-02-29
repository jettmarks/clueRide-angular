(function (angular) {
    'use strict';

    var viewModel,
        courseMapResource,
        courseDataResource,
        network;

    angular
        .module('crPlayer.CourseModule', ['common.CourseResource','ui.bootstrap'])
        .controller('CourseController', CourseController)
        //.service('Service', Service)
        .directive('crShowCourse', crShowCourse)
    ;

    CourseController.$inject = ['$scope', 'CourseMapResource','CourseDataResource'];

    function CourseController($scope, CourseMapResource, CourseDataResource) {
        var vm = this;

        vm.courseToMap = courseToMap;
        vm.course = {};

        viewModel = vm;

        network = $scope.$parent.mapModel.gjNetwork;

        // Local copies of resources
        courseMapResource = CourseMapResource;
        courseDataResource = CourseDataResource;
    }

    /**
     * Request the course and put it on the map.
     */
    function courseToMap () {
        courseMapResource.getMap({
            /* Future: put the courseId here. */
        }, featuresToMap);
        courseDataResource.getData({
            /* Future: put the courseId here. */
        }, dataToModel);
    }

    function featuresToMap (courseFeatures) {
        angular.extend(network, {
            course: {
                data: courseFeatures,
                pointToLayer: courseMarkers,
                onEachFeature: perFeature
            }
        })
    }

    function courseMarkers (feature, latlng) {
        var marker = new L.marker(latlng, {
            /* Type of icon would be selected here. */
            icon: getMarkerIcon (
                feature.properties.pointId,
                feature.properties.state,
                feature.properties.selected
            ),
            message: 'Node ' + feature.properties.pointId
        });
        /* marker.on response would go here. */
        return marker;
    }

    function perFeature (feature, layer) {
        if (feature.geometry.type === 'LineString') {
            layer.setStyle({
                color: 'blue',
                opacity: 0.4,
                weight: 10
            });
        }
    }

    function dataToModel(course) {
        viewModel.course = course;
    }

    function crShowCourse() {
        return {
            templateUrl: 'js/course/showCourse.html'
        }
    }

}(window.angular));