There is a set of unit tests for the LocationService which covers
the three different sets of coordinates that are interesting to this
application:

 1. GPS coordinates as received by the devices positioning system.
 2. Tether coordinates which are received from the server for a given team.  
 Generally, this will be the coordinates of the Team's Leader, but it can be
 any value that the server supplies for the team.  This allows simulations as well.
 3. When editing locations from a desk instead of in the field, it's convenient to
 treat the map's center as the current location.  
 
Each of these three can be used for whatever purpose is appropriate.  Care must be 
taken however to understand what you're asking for.  The tests define the expected
behavior.