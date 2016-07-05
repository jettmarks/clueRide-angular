Invitation Design Notes
====

Nested Layers
----
Domain Object|Input                          |Output              |Directive
-------------|-------------------------------|--------------------|---------
Invite       |inviteToken from $routeParams  |Invitation (Promise)|Invite Controller
Outing       |Invitation Promise / Outing ID |Outing (Promise)    |outing-invite
Course       |Outing Promise / Course ID     |Course (Promise)    |course-invite