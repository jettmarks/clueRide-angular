Game State Module
====
There multiple States being tracked:

1. Preparation State - Before play begins, different players take different paths to begin playing; this state occurs prior to the game's start; multiple actors involved.
2. Outing State - tracks server-side the team's progress on a course as it is run on a given date.
3. History State - tracks client-side which attraction/location is being presented out of the list of "opened" attractions.

The Course may also have changing state. Early releases of Clue Ride only support static Courses, but future releases will introduce a dynamic course which can be modified as the game progresses.

Preparation State Tracking
----
_Unsure regarding whether to use forward / backward here_

Index | Forward | Reverse
------|---------|--------
1  | Login | Logout
2  | Join Team | [Selected Team's Name]
3  | GPS? | "GPS On" or "Tethered"
4  | Play | Back to Options

Outing State Tracking
----
First, the Outing itself is just the schedule which is created when invitations are sent to guests. Once the guests 
start signing in, the Outing State begins actively tracking the state.

The Outing State keeps track of the following for the Outing:

1. Outing ID - unique record of a mapping between a Team ID and a Course ID -- the Outing and the OutingState share the same unique ID
2. Path Index - Progress along the Course
3. Team Confirmed flag - Set to true from the server once play may begin
4. Most recent clue Solved Flag - Perhaps easier to think of as the "Riding" flag; true when the group is riding from one attraction to the next.
5. currentClueId - Specific clue to be presented to the team (when there is more than one clue at a given location)

NOTE: Currently, no history of the Clues will be kept client-side; only the scores of those clues.


From the OutingState, there are two essential attributes used to track the macro state of progress along the Course:

1. Current Path Index 
2. Clue Solved / Riding

In addition to these attributes, a player may or may not have successfully solved the clue; their state is tracked client-side and provided to the server for computing the team "consensus".

Path Index == -1 means the course has not yet started.  Players are gathering at the Start Attraction and the first clue has not yet been revealed/solved.

For a given Path Index:

Clue Not Solved | Clue Solved
------|------
"At Attraction" State | "Riding" State
Path Departure is Revealed | Path Destination also Revealed
Geometry up to Departure | Entire Geometry of Path
Attraction is Highlighted | Path is Highlighted
Pager is enabled up to Path Index | Pager enabled up to Path Index + 1

History State Tracking
----

Forward | Reverse
---------|--------
[Current Attraction] | [Previous Attraction]
Solve Clue | Collapses to nothing (Attraction)
Where Am I | Collapses to nothing (Attraction)

## Clue Scores
_TODO: Move this to a different README.md_
This was listed under History because although we don't allow players to review past Clues and their answers, we do track how well they did.  Here are some candidates for the history:

Competitive side:

* Points awarded for the question.
* Earn a badge ?
* Time-based (from open clue to correct answer submitted)
* Rank against teammates, personal record, overall record for the question
* Time across all questions on course

Transitions
----
There are three groups of transitions:

1. Team Events (Server "Push" Events) - based on combination of individual players and their Guide for an outing.
2. Player-generated Course Progress transitions - clue solved and destination reached; these are combined across a team to generate the Team Events.
3. Player-generated History transitions - clicks to browse the history and related views of the history; tracked client-side only, and reset when Team Events occur.

### Team Events
* Course is selected.
* Team Leader reveals clue for current attraction.
* Clue is solved by fellow Team Member.
* Destination is reached by Team Member/Leader.

### Player-Generated Course Progress Transitions
* Clue is Solved by submitting correct answer.
* Destination is Reached

### Player-Generated History Transitions
This is reflected client-side on the Home page where each of the "Bubbles" has an action. Within the History, there is "forward" and "backward":

* Back - Previous Attraction (when not at the first attraction)
* Forward - Next Attraction (when not at the most recent attraction)

There are also User selections that reveal a view of a point in history.

* View Map
* View Attraction

## Other aspects of Transitions
### At the start of the game
Assuming Course has been selected and participants arrived, the departure of the first Path is revealed and the Clue State starts out as "Not Solved".
### After Clue is solved
Solving a Clue is an event that triggers a change to the "Solved" state.  The Path Index remains unchanged, but is now revealed to show how to get from the current attraction to the next attraction.  The group can begin riding, and is considered to be in the "Riding" state.

Clue for next Attraction is not yet visible.
### After arrival at Attraction
Arrival at a Attraction is an event that triggers a change to the "Not Solved" state and also increments the Path Index.  No changes to what is revealed on the map or in the Attractions, but the Clue for the arrived Attraction is now available to be solved.
_PathIndex gets you the proper Clue when used this way._
### At Conclusion (arrival at last Attraction)
Player's may review any portion of the course until the state is cleared on the server. 
_**TBD:** Remains a design question how long these objects will be maintained in memory._

Game State Propagation
----
There are a few aspects of Game State that are interesting:

* Events that need to be propagated (what functions)
* How does state get pushed out to the devices (what implementation)
* How do we verify/test the functionality (QA/diagnostics)
* Part of the State is a push/pull of the tethered location

## Events

* Start of game (Team Confirmed)
* Clue Solved (next path revealed)
* Team Arrival at next Attraction (opening the next clue)
* Change of Course Selection after the Team is confirmed (will require merging of state and probably a new course made
up on the fly to account for the history from one course and the plan from another course).

### Supporting State Variables
* pathIndex (Integer starting at -1 for game not yet started)
* mostRecentClueSolvedFlag (true if we're riding and not yet arrived)

Team Confirmation opens play and Path Index is knocked off of -1.  It is possible that courses would be changed mid-stream.

## Push implementation
Risk is in choosing an appropriate solution for devices with intermittent connections such as mobile.

Polled solution isn't unreasonable for the following reasons:

* Viewing a page generally involves a trip to the server anyway.  If a timestamp of the last state change is part of
the response, this can be checked for a difference and serve as a trigger to refresh the state.  The Home Page makes a
good target for viewing updated state.  The Team Leaders Status page is another.

## Two Polling Intervals

1. The Outing State itself
2. GPS position when tethered

### Outing State Interval
* 20-second or so automated pull
* Auto-pull can be turned off
* Can be manually requested by clicking/pressing "Home"

### GPS position
* Only turned on when a) Map is open, b) Riding state and c) tethered.
* short interval for Armchair tether.
* longer interval for field-based tether.
* Manually requested at any time ??

## QA

## Tethered Location
The timing and refresh rate of a tethered location is different from the timing for pushing/pulling the game state.
Two different channels are setup, but it makes sense to expand the response to many REST calls to include both
a position timestamp as well as a game state timestamp.

* Pushing the updated position on an interval per team.
* Pull occurs on an interval, but only when the position matters to the display (a map).

