## Package com.clueride.io 

Classes in this package provided a couple of services:

- Writing JSON to a file-based "database"
- Providing JSON to return in response to a web-client call.
 
For Data Store features, the `com.clueride.dao` package provides 
a layer of abstraction for reading and persisting.


PojoJsonUtil
====
This is the core piece for persisting Pojo as JSON on disk.  There are a few pieces
needed to be pulled together to make this work for a given POJO:

- JsonStoreType: enumeration for the new POJO.
- JsonStoreLocation: provides the directory paths for the JSON files.
- JsonPrefixMap: text that appears as the start of the file name.

Geometric data will benefit from expanding another class.

- JsonSchemaTypeMap: maps the JsonStoreType to a defined Schema for Geometric objects.

<T> List<T> loadJsonObjects
----
Generic method for loading instances of the desired objects.

This requires adding to the switch statement so we can get the right type passed where it
belongs.  Seems like there is a better way to do this, but haven't hunted it down yet.

Testing
----
At this time, the testing is not automated, but there are some service module examples 
which are helpful.