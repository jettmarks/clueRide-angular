Game State Module
====
There is the State of the _**Game**_ and the State of the _**Course**_.  Together, these determine
what the user sees as they progress.

Course State Tracking
----
There are two parameters used to track the macro state of the Course:

1. Current Location Index which matches a Path (should probably refactor to Current Path Index).
2. Clue Solved

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

