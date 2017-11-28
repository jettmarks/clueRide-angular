Three fields from the BadgeEvent are stored in the database in fields that 
 - require expansion once brought into memory
 - require conversion to store
The service layer is responsible for mapping across these instances.
 
Inbound during capture:
- Principal is stored as Member ID
- Class is presented as String
- Return Value is stored as String

Outbound when retrieving:
- Principal is looked up from Member ID
- Class is looked up from ClassName (maybe)
- Return Value is presented as String

Only Principal lookup is implemented in both directions at this time 
(until we feel there is an actual use of the Class rather than just the name)

Considered: Return Value is stored as string, but it may be
worthwhile turning into something else.

The annotation @DbSourced is used for test instances that hold the persisted fields.
The annotation @ServiceSourced is used for fully-populated instances.
The annotation @ClientSourced is used for inbound capture instances.