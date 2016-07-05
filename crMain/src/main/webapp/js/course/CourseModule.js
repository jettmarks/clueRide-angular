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
        vm.course = {
            name: 'No course selected',
            description: ''
        };
        vm.courses = [];
        vm.select = select;

        viewModel = vm;

        network = $scope.$parent.mapModel.gjNetwork;

        // Local copies of resources
        courseMapResource = CourseMapResource;
        courseDataResource = CourseDataResource;

        courseDataResource.get({
            /* At this time, we retrieve all courses until we have so many
             * that we need to apply selection criteria. */
        }, dataToModel);

        function select(course) {
            vm.course = course;
        }
    }

    function CourseMapResource ($resource) {
        return $resource('/crMain/rest/course/map', {}, {
            getMap: {
                method: 'GET',
                params: {},
                isArray: false
            }
        });
    }

    function CourseDataResource ($resource) {
        return $resource('/crMain/rest/course/data', {}, {
            getData: {
                method: 'GET',
                params: {},
                isArray: true
            }
        });
    }

    /**
     * Request the course and put it on the map.
     */
    function courseToMap () {
        courseMapResource.getMap({
            courseId: viewModel.course.id
        }, featuresToMap);
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

    function dataToModel(courses) {
        viewModel.courses = courses;
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