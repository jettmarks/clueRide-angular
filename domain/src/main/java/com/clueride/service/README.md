
Service Package of Domain Project
====
Instantiating Domain Instances
----
The services defined here are for instantiating the Domain objects.

In particular, to obtain an ID for many of the objects, we've got different
methods for different circumstances:

* Reading from a permanent store (which may not be available until runtime).
* Testing (which will allow a mockup of the permanent store)
* Computational instantiation of temporary objects which need to be uniquely
identified, but may not be persisted.

At this time, only a memory-based method will be implemented within this 
project.  This will allow testing independent of a Data Store.

When used with a Data Store, that Data Store will be responsible not only 
for providing the ID of instances built from that store, but also for creating
new instances which may or may not be persisted, but could be if the need 
arises.

Use Case Summary
----
* Have an ID already
* Need new ID, DataStore not involved
* Need new ID, need to accommodate DataStore

When we have an ID, we expect that ID will be passed in via a SimpleFeature 
which carries the ID.

When the DataStore is not involved, we can use the default memory-based
implementations.

When the DataStore is involved, we can use the implementation supplied by
that project / package.