Game State Module
====
There is the State of the _**Game**_ , the state of the _**Outing**_, and the State of the _**Course**_.  Together, these determine
what the user sees as they progress.

Outing State Tracking
----
This keeps track of the following for the Outing.

1. Outing ID (unique record of a mapping between a Team ID and a Course ID)
2. Path Index
3. Team Confirmed flag
4. Most recent clue Solved Flag
5. Specific clue to be presented to the team (when there is more than one clue at a given location)

Some of this may be replaced by the Outing Step (index):

Index | Forward | Reverse
------|---------|--------
1  | Login | Logout
2  | Join Team | [Selected Team's Name]
3  | GPS? | "GPS On" or "Tethered"
4  | Play | Back to Options
5  | [Current Location] | [Previous Location]
6  | Solve Clue | Collapses to nothing (Location)
7  | Where Am I | Collapses to nothing (Location)

Course State Tracking
----
There are two parameters used to track the macro state of the Course:

1. Current Path Index 
2. Clue Solved

Path Index == -1 means the course has not yet started.  Players are gathering at the Start Location
and the first clue has not yet been revealed/solved.

For a given Path Index:

Clue Not Solved | Clue Solved
------|------
"At Location" State | "Riding" State
Path Departure is Revealed | Path Destination also Revealed
Geometry up to Departure | Entire Geometry of Path
Location is Highlighted | Path is Highlighted
Pager is enabled up to Path Index | Pager enabled up to Path Index + 1

Transitions
----
There are three groups of transitions:

1. Team Events, often Server Push Events
2. Course Transitions such as clue solved and destination reached.
3. User clicks to browse the history and related views of the history.

### Team Events
* Course is selected.
* Team Leader reveals clue for current location.
* Clue is solved by fellow Team Member.
* Destination is reached by Team Member/Leader.

### Course Transitions
* Clue is Solved by submitting correct answer.
* Destination is Reached

### User selections
Each of the "Balloons" has an action.  There are the classes of actions that cause 
transitions to other states.

* Last Location (when not at the first location)
* Next Location (when not at the most recent location)

There are also User selections that reveal a view of a point in history.

* View Map
* View Location
* View Clue

## Other aspects of Transitions
### At the start of the game
Assuming Course has been selected and participants arrived, the departure of the first Path is revealed and the Clue State starts out as "Not Solved".
### After Clue is solved
Solving a Clue is an event that triggers a change to the "Solved" state.  The Path Index remains unchanged, but is now revealed to show how to get from the current location to the next location.  The group can begin riding, and is considered to be in the "Riding" state.

Clue for next Location is not yet visible.
### After arrival at Location
Arrival at a Location is an event that triggers a change to the "Not Solved" state and also increments the Path Index.  No changes to what is revealed on the map or in the Locations, but the Clue for the arrived Location is now available to be solved.
### At Conclusion (arrival at last Location)
Player's may review any portion of the course or the clues answered until the state is cleared.

Game State Tracking
----
TODO: Document this more completely.

This is viewed through the Balloons.

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
* Team Arrival at next location (opening the next clue)
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


## QA

## Tethered Location
The timing and refresh rate of a tethered location is different from the timing for pushing/pulling the game state.
Two different channels are setup, but it makes sense to expand the response to many REST calls to include both
a position timestamp as well as a game state timestamp.

* Pushing the updated position on an interval per team.
* Pull occurs on an interval, but only when the position matters to the display (a map).

