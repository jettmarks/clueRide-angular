(function () {
    "use strict";

    var self = {},
        toTest;

    describe("Game State", function () {
        self = this;

        beforeEach(module('crPlayer.GameState','common.CourseResource'));

        beforeEach(function () {
            // Simulate beforeAll
            if (!toTest) {
                /* Inject our instance of the service under test. */
                inject(function($injector) {
                    self.gameStateService = $injector.get('GameStateService');
                    // Alias for the service
                    toTest = self.gameStateService;
                });
            }
        });

        describe("initially", function () {
            it('should have initial state of "beginPlay"', function () {
                expect(toTest.currentGameStateKey()).toEqual('beginPlay');
                expect(toTest.currentGameState().bubble3.dialog).toBeUndefined();
            });
            it('should allow choosing Team', function () {
                expect(toTest.currentGameState().bubble1.dialog).toEqual('joinTeam');
            });
            it('should allow setting GPS options', function () {
                expect(toTest.currentGameState().bubble2.dialog).toEqual('setGpsMode');
            });
            it('should have no Path visible', function () {
                expect(toTest.getPathIndex()).toEqual(-1);
            });

            // TODO: Move this to a Map Test
            //it('should have no Paths visible', function () {
            //    expect(toTest.maxVisiblePathIndex()).toEqual(-1);
            //});
        });

        describe("after Team Leader has Started Play", function () {
            it('Clues should be visible', function () {
                toTest.updateGameState('atLocation');
                expect(toTest.currentGameState().bubble2.dialog).toEqual('solveClue');
            });
            it('Clue questions should not yet be solved', function () {
                expect(toTest.mostRecentClueSolved()).toEqual(false);
            });
            it('should have no Path visible', function () {
                expect(toTest.getPathIndex()).toEqual(-1);
            });
            // TODO: The initial location should probably be available once course is selected
            it('should allow viewing initial location', function () {
                expect(toTest.currentGameState().bubble1.nextView).toEqual('location');
                expect(toTest.currentGameState().locationIndex).toEqual(0);
            });
            it('should show "unsolved" dialog for "Where are we Going?"', function () {
                // TODO: Put this back in after we start walking clues
                //expect(toTest.currentGameState().bubble3.nextState).toBeUndefined();
                expect(toTest.currentGameState().bubble3.dialog).toEqual('clueNotSolved');
            });
        });

        describe("with first Clue Solved", function () {
            it('should have game state of "riding"', function () {
                toTest.clueSolved();
                expect(toTest.currentGameStateKey()).toEqual('riding');
            });
            it('should have first Path visible', function () {
                expect(toTest.getPathIndex()).toEqual(0);
            });
            it('should allow reaching previous location', function () {
                expect(toTest.currentGameState().bubble1.nextState).toEqual('history');
            });
            it('should allow reaching next location', function () {
                expect(toTest.currentGameState().bubble3.nextView).toEqual('location');
            });
        });

        describe("with first Destination Reached", function () {
            it('should have game state of "atLocation"', function () {
                toTest.arrived();
                expect(toTest.currentGameStateKey()).toEqual('atLocation');
            });
            it('should have first Path visible', function () {
                expect(toTest.getPathIndex()).toEqual(0);
                expect(toTest.currentGameState().locationIndex).toEqual(1);
            });
            it('should have Clue questions visible', function () {
                expect(toTest.currentGameState().bubble2.dialog).toEqual('solveClue');
            });
            it('should have Clue questions unanswered', function () {
                expect(toTest.mostRecentClueSolved()).toBeFalsy();
            });
        });

        // TODO: Get this back into the stream; doesn't like the dependency on course
        //describe("having set out on second path", function () {
        //    it('should have completed path different from current path', function () {
        //        toTest.clueSolved();
        //        expect(toTest.getPathIndex()).toNotEqual(toTest.getCompletedPathIds()[0]);
        //    });
        //});

    });

}());

