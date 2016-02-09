(function (angular) {
    'use strict';

    var viewModel,
        courseMapResource,
        courseDataResource,
        network;

    angular
        .module('crNetEdit.CourseModule', ['ngResource','ui.bootstrap'])
        .controller('CourseController', CourseController)
        .factory('CourseMapResource', CourseMapResource)
        .factory('CourseDataResource', CourseDataResource)
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

    function CourseMapResource ($resource) {
        return $resource('/crMain/rest/course/map', {}, {
            getMap: {
                method: 'GET',
                /* Hardcoded until we have more than one course. */
                params: {courseId: 3},
                isArray: false
            }
        });
    }

    function CourseDataResource ($resource) {
        return $resource('/crMain/rest/course/data', {}, {
            getData: {
                method: 'GET',
                /* Hardcoded until we have more than one course. */
                params: {courseId: 3},
                isArray: false
            }
        });
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

    //function Service() {
    //    return {
    //        request: requestFunction,
    //        confirm: confirmFunction
    //    };
    //}

    function crShowCourse() {
        return {
            templateUrl: 'js/course/showCourse.html'
        }
    }

}(window.angular));