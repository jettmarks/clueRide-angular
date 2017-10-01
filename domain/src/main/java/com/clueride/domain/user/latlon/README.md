# Proposed (User-facing) Nodes

Generally, a Node is the end-point of a Segment which is connected 
to the network. However, when proposing new Locations which may not
yet be connected to the network, we need a "latLon" holding the lat/lon
pair so we know where the Location is.  The idea is we'll expand the
network to contain the latLon/location which puts the proposed Location
on the network.

The `user.latLon` package holds the service/DAO for storing these 
proposed Nodes in support of a Location into the database. It 
supports the NodeService for proposed nodes.

Once the latLon is on the network, it is likely the Node will be 
persisted in the locations.geojson covered by the NodeStoreJson 
instead of the one found here.