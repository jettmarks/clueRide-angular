(function () {
    "use strict";

    var self = {},
        toTest,
        dummyScope = {};

    describe("Outing State", function () {
        self = this;

        //beforeEach(module('crPlayer.OutingState','common.CourseResource', 'crPlayer.BadgesModule'));
        beforeEach(module('crPlayer.OutingStateModule','common.CourseResource', 'crPlayer.BadgesModule'));

        beforeEach(function () {
            // Simulate beforeAll
            if (!toTest) {
                /* Inject our instance of the service under test. */
                inject(function($injector) {
                    self.badgesService = $injector.get('BadgesService');
                    self.outingStateService = $injector.get('OutingStateService');
                    // Alias for the service
                    toTest = self.outingStateService;
                    // TODO: add these at the appropriate step
                    self.badgesService.addBadge('TEAM_MEMBER');
                    //self.badgesService.addBadge('TEAM_LEAD');
                    //toTest.setCourseScope(dummyScope);
                });
            }
        });

        /* These tests are intended to be run in the order declared here; we're walking the app. */
        describe("initially", function () {
            it('should start at Step 1', function () {
                expect(toTest.getStepIndex()).toBe(1);
            });
            it('should have reverse bubble read "Login"', function () {
                expect(toTest.getReverseTitle()).toBe("Login");
                expect(toTest.getReverseDisabled()).toBeFalsy();
            });
            it('should have neutral bubble read "Join Team?"', function () {
                expect(toTest.getNeutralTitle()).toBe("Join Team?");
                expect(toTest.getNeutralDisabled()).toBeTruthy();
            });
            it('should have forward bubble read "GPS?"', function () {
                expect(toTest.getForwardTitle()).toBe("GPS?");
                expect(toTest.getForwardDisabled()).toBeTruthy();
            });

            it('should have no Path visible', function () {
                expect(toTest.getPathIndex()).toEqual(-1);
            });
        });

        describe("after player has logged in", function () {
            it('should continue with Step 2', function () {
                toTest.login();
                expect(toTest.getStepIndex()).toBe(2);
            });
            //it('should have reverse bubble read "Logout"', function () {
            //    expect(toTest.getReverseTitle()).toBe("Logout");
            //    expect(toTest.getReverseDisabled()).toBeFalsy();
            //});
            //it('should allow choosing Team', function () {
            //    expect(toTest.getNeutralTitle()).toBe("Join Team?");
            //    expect(toTest.getNeutralDisabled()).toBeFalsy();
            //});
            it('should not yet allow setting GPS options', function () {
                expect(toTest.getForwardTitle()).toBe("GPS?");
                expect(toTest.getForwardDisabled()).toBeTruthy();
            });
        });

        describe("after player has chosen a team", function () {
            //it('should allow choosing GPS options', function () {
            //    toTest.updateLoginState();
            //    expect(toTest.currentGameState().bubble2.disabled).toBeFalsy();
            //});
            //it('should still not yet allow beginning Play', function () {
            //    expect(toTest.currentGameState().bubble3.disabled).toBeTruthy();
            //});
            it('should still have no Path visible', function () {
                expect(toTest.getPathIndex()).toEqual(-1);
            });
        });

        //describe("after player has selected GPS Options", function () {
            //it('should still not yet allow beginning Play', function () {
            //    var isTethered = false;
            //    toTest.updateGpsState(isTethered);
            //    expect(toTest.currentGameState().bubble3.disabled).toBeTruthy();
            //});
            //it('should still have no Path visible', function () {
            //    expect(toTest.getPathIndex()).toEqual(-1);
            //});
            //it('should show GPS State as turned on when selected ON', function () {
            //    var isTethered = false;
            //    toTest.updateGpsState(isTethered);
            //    expect(toTest.currentGameState().bubble2.title).toEqual('GPS On');
            //});
            // TODO: CA-225 - get Tethered Mode flag working
            //it('should show GPS State as tethered when selected OFF', function () {
            //    var isTethered = true;
            //    toTest.updateGpsState(isTethered);
            //    expect(toTest.currentGameState().bubble2.title).toEqual('GPS tethered');
            //});
        //});

        //describe("after Team Leader has Confirmed Team", function () {
        //    it('Play button should be enabled', function () {
        //        toTest.confirmTeam();
        //        toTest.digestState({
        //            outingId: 2,
        //            teamConfirmed: true
        //        });
        //        expect(toTest.currentGameState().bubble3.disabled).toBeFalsy();
        //    });
        //});
        //
        //describe("after Play button has been clicked", function () {
        //    it('Clues should be visible', function () {
        //        toTest.updateGameState('atLocation');
        //        expect(toTest.currentGameState().bubble2.dialog).toEqual('solveClue');
        //    });
        //    it('Clue questions should not yet be solved', function () {
        //        expect(toTest.mostRecentClueSolved()).toEqual(false);
        //    });
        //    it('Max Location visible is the first location', function() {
        //        expect(toTest.maxVisibleLocationIndex()).toEqual(0);
        //    });
        //    it('should have no Path visible', function () {
        //        expect(toTest.getPathIndex()).toEqual(-1);
        //    });
        //     TODO: The initial location should probably be available once course is selected
            //it('should allow viewing initial location', function () {
            //    expect(toTest.currentGameState().bubble1.nextView).toEqual('location');
            //    expect(toTest.currentGameState().locationIndex).toEqual(0);
            //});
            //it('should show "unsolved" dialog for "Where are we Going?"', function () {
            //     TODO: Put this back in after we start walking clues
            //    expect(toTest.currentGameState().bubble3.nextState).toBeUndefined();
                //expect(toTest.currentGameState().bubble3.dialog).toEqual('clueNotSolved');
            //});
        //});
        //
        //describe("with first Clue Solved", function () {
        //    it('should have outing state of "riding"', function () {
        //        toTest.clueSolved();
        //        expect(toTest.currentGameStateKey()).toEqual('riding');
        //    });
        //    it('should have first Path visible', function () {
        //        expect(toTest.getPathIndex()).toEqual(0);
        //    });
        //    it('should allow reaching previous location', function () {
        //        expect(toTest.currentGameState().bubble1.nextState).toEqual('history');
        //    });
        //    it('should allow reaching next location', function () {
        //        expect(toTest.currentGameState().bubble3.nextView).toEqual('location');
        //    });
        //});
        //
        //describe("with first Destination Reached", function () {
        //    it('should have outing state of "atLocation"', function () {
        //        toTest.arrived();
        //        expect(toTest.currentGameStateKey()).toEqual('atLocation');
        //    });
        //    it('should have first Path visible', function () {
        //        expect(toTest.getPathIndex()).toEqual(0);
        //        expect(toTest.currentGameState().locationIndex).toEqual(1);
        //    });
        //     TODO: Not sure if I want to use this method or not
        //    it('Max Location visible is the first location', function() {
        //        expect(toTest.maxVisibleLocationIndex()).toEqual(1);
        //    });
            //it('should have Clue questions visible', function () {
            //    expect(toTest.currentGameState().bubble2.dialog).toEqual('solveClue');
            //});
            //it('should have Clue questions unanswered', function () {
            //    expect(toTest.mostRecentClueSolved()).toBeFalsy();
            //});
        //});
        //
        // TODO: Get this back into the stream; doesn't like the dependency on course
        //describe("having set out on second path", function () {
        //    it('should have completed path different from current path', function () {
        //        toTest.clueSolved();
        //        expect(toTest.getPathIndex()).toNotEqual(toTest.getCompletedPathIds()[0]);
        //    });
        //});

    });

}(describe));

