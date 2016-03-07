(function () {
    "use strict";

    var self = {},
        toTest;

    describe("Badges", function () {
        self = this;

        beforeEach(module('crPlayer.BadgesModule'));

        beforeEach(function () {
            inject(function ($injector) {
                self.badgesService = $injector.get('BadgesService');
                toTest = self.badgesService;
            });
        });

        describe("can accept and retrieve Badges", function () {
            it('should be empty to start', function () {
                expect(toTest.getBadges().length).toEqual(0);
            });
            it('should have entries after storing some', function () {
                toTest.saveBadges(
                    [
                        'TEAM_LEAD',
                        'TEAM_MEMBER'
                    ]
                );
                expect(toTest.getBadges().length > 0).toBeTruthy();
            });
            it('should be able to find one that is added', function () {
                expect(toTest.hasBadge('TEAM_LEAD')).toBeTruthy();
            });
            it('should return false for one that has not been added', function () {
                expect(toTest.hasBadge('CREATOR_OF_THE_UNIVERSE')).toBeFalsy();
            });
        });
    });

}())
