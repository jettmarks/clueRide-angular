(function (angular) {
    'use strict';

    /* Hardcoded until we have more than one course. */
    var selectedCourse = 3;

    angular
        .module('common.CourseResource', ['ngResource'])
        .factory('CourseMapResource', CourseMapResource)
        .factory('CourseDataResource', CourseDataResource)
        .factory('CourseLocationMapResource', CourseLocationMapResource)
        .factory('CourseLocationDataResource', CourseLocationDataResource)
    ;

    function CourseMapResource ($resource) {
        return $resource('/rest/course/map', {}, {
            getMap: {
                method: 'GET',
                params: {courseId: selectedCourse},
                isArray: false
            }
        });
    }

    function CourseDataResource ($resource) {
        return $resource('/rest/course/data', {}, {
            getData: {
                method: 'GET',
                params: {courseId: selectedCourse},
                isArray: false
            }
        });
    }

    function CourseLocationDataResource ($resource) {
        return $resource('/rest/location/course', {}, {
            getData: {
                method: 'GET',
                params: {courseId: selectedCourse},
                isArray: true
            }
        });
    }

    function CourseLocationMapResource ($resource) {
        return $resource('/rest/location/map', {}, {
            getMap: {
                method: 'GET'
            }
        });
    }

}(window.angular));
