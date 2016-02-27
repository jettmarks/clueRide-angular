(function () {
    "use strict";

    var self = {},
        toTest;

    describe("Location Service", function () {
        self = this;
        self.testScope = {
            locations: []
        };

        beforeEach(module('crPlayer.Location', 'crPlayer.GameState'));

        beforeEach(function () {
            inject(function ($injector) {
                self.gameStateService = $injector.get('GameStateService');
                self.locationService = $injector.get('LocationService');
                toTest = self.locationService;
                toTest.setLocationScope(self.testScope);
                toTest.init(self.gameStateService);
            });
        });

        describe("can provide 3 different locations depending on mode", function () {
            it('should have the same value initially', function () {
                var mapLoc = toTest.getMapCoords();
                expect(toTest.getDeviceCoords()).toEqual(mapLoc);
                toTest.enableGps();
                expect(toTest.getDeviceCoords()).toEqual(mapLoc);
            });
        });

        describe("provides map location when we set the map mode", function () {
            it('should give a map location', function () {
                var originalLoc = toTest.getMapCoords();
                /* This is how setting the center of the map tells the service where it is now. */
                var mapLoc = {lat:23, lon:-75};
                toTest.setMapCoords(mapLoc);

                expect(toTest.getCloseToMeCoords()).toEqual(originalLoc);
                toTest.trackMap();
                expect(toTest.getCloseToMeCoords()).not.toEqual(originalLoc);
                expect(toTest.getCloseToMeCoords()).toEqual(mapLoc);
                toTest.trackDevice();
                expect(toTest.getCloseToMeCoords()).toEqual(originalLoc);
            });
        });

        describe("when Tethered mode is toggled", function () {
            var originalLoc;
            var teamLoc = {lat:23, lon:-75};

            it('should provide the tether source location', function () {
                originalLoc = toTest.getDeviceCoords();

                /* This is how setting the center of the map tells the service where it is now. */
                toTest.setTetherCoords(teamLoc);

                expect(toTest.getDeviceCoords()).toEqual(originalLoc);
                toTest.disableGps();
                expect(toTest.getDeviceCoords()).not.toEqual(originalLoc);
                expect(toTest.getDeviceCoords()).toEqual(teamLoc);
            });

            it('should return to GPS location when GPS is re-enabled', function () {
                toTest.enableGps();
                expect(toTest.getDeviceCoords()).toEqual(originalLoc);
            });
        });
    });

}())
