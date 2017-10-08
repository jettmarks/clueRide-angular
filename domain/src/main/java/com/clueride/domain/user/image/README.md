Images are generally associated with Locations.

Each location may have multiple images, but there 
should be a single "Featured" image.

Generally, there is a one-to-many relationship 
between Locations and Images, but there can be 
a single image that is associated with more than
one location.  A typical example would be an
image for a Location that is also shown to the 
user when it best represents a Location Group --
multiple locations close enough together they
can be grouped on the map .

There are two parts to the image:
- The contents of the file.
- The metadata for the image.

The Contents are placed on a webserver that Apache can handle.

The metadata (for this package) are persisted via JPA.