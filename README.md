Clue Ride
====

What is Clue Ride?
----
Clue Ride is a scavenger hunt on bicycles, with strong leanings toward crowd-sourced community involvement.

There is one application for playing the game and another application for adding and maintaining the network of paths
and attractions that guide cyclists safely around the city.

Badges are awarded as "seekers" and "guides" develop their skills in maintaining the games paths and attractions.

Technologies
====

Main layers
----
* Java-based Web Application running under Tomcat
* Angular front-end for mobile browsers
* Currently using JSON and XML for storage of routes and data; plan to move toward POSTGreSQL with Geo-Spatial extensions.

Back-end Libraries (Java)
----
* Guice for Dependency Injection
* Jersey for REST API
* GeoTools for handling route geometries

Front-end Libraries
----
* AngularJS for overall structure
* UI-Angular for Bootstrap look-and-feel within Angular
* Leaflet for Maps (Open Street Maps for layers)
* Library for using camera from within mobile browser

Infrastructure
----
* Maven (considering gradle)
* Git / GitHub
* TestNG for Java Unit Testing
* Jasmine for JavaScript Unit Tests
* HTTPS for encrypted sessions
