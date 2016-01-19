(function (angular) {
    'use strict';

    angular
        .module('common.CourseResource', ['ngResource'])
        .factory('CourseMapResource', CourseMapResource)
        .factory('CourseDataResource', CourseDataResource)
    ;

    function CourseMapResource ($resource) {
        return $resource('/rest/course/map', {}, {
            getMap: {
                method: 'GET',
                /* Hardcoded until we have more than one course. */
                params: {courseId: 2},
                isArray: false
            }
        });
    }

    function CourseDataResource ($resource) {
        return $resource('/rest/course/data', {}, {
            getData: {
                method: 'GET',
                /* Hardcoded until we have more than one course. */
                params: {courseId: 2},
                isArray: false
            }
        });
    }

}(window.angular));
