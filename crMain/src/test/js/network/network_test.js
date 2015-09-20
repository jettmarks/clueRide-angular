// TODO: Not yet functioning; sorting out how to run this test.
(function (angular) {
    'use strict';

    var Network,
        $httpBackend;

    describe('clueride.Network', function () {

        /* This is needed to be able to match $resource's response objects */
        beforeEach(function () {
            this.addMatchers({
                toEqualData: function (expected) {
                    return angular.equals(this.actual, expected);
                }
            });
        });

        beforeEach(function () {
            module('NetworkResource');

            inject(function ($injector) {
                $httpBackend = $injector.get('$httpBackend');
                Network = $injector.get('Network');
            });

        });

        describe('get', function () {

            it('should http GET and succeed with result equal to response', function () {
                var getResult,
                    getResponse = {
                        segments: {
                            name: 'Feature Collection'
                        }
                    };

                $httpBackend.expectGET(
                    'rest/network'
                ).respond(
                    getResponse
                );

                getResult = Network.get();

                $httpBackend.flush();
                expect(getResult).toEqualData(getResponse);

            });

            it('should http GET and error code to equal response', function () {
                var getResult,
                    getResponse = 401,
                    successCalled = false,
                    errorCalled = false,
                    errorCode;

                $httpBackend.expectGET(
                    'rest/network'
                ).respond(
                    getResponse
                );

                getResult = Network.get();

                getResult.$promise.then(
                    function (data) {
                        successCalled = true;
                    }
                ).catch(
                    function (error) {
                        errorCalled = true;
                        errorCode = error.status;
                    }
                );

                $httpBackend.flush();
                expect(successCalled).toBe(false);
                expect(errorCalled).toBe(true);
                expect(errorCode).toEqualData(getResponse);

            });

        });


    });

}(window.angular));