
var defaultMarkerIcon = L.AwesomeMarkers.icon({
    icon: 'coffee',
    markerColor: 'darkblue'
});

var mouseoverMarkerIcon = L.AwesomeMarkers.icon({
    icon: 'coffee',
    markerColor: 'blue'
});

var iconMarkerSingleTrack = L.AwesomeMarkers.icon({
    icon: 'coffee',
    markerColor: 'darkGreen'
});

var iconMarkerOnSegment = L.AwesomeMarkers.icon({
    icon: 'coffee',
    markerColor: 'lightGreen'
});

var iconMarkerOnMultiTrack = L.AwesomeMarkers.icon({
    icon: 'coffee',
    markerColor: 'orange'
});

var iconMarkerOffNetwork = L.AwesomeMarkers.icon({
    icon: 'coffee',
    markerColor: 'red'
});

function getMarkerIcon(state) {
    switch(state) {
    case 'UNDEFINED': 
        return defaultMarkerIcon;
        break;
    case 'ON_SINGLE_TRACK': 
        return iconMarkerSingleTrack;
        break;
    case 'ON_MULTI_TRACK': 
        return iconMarkerOnMultiTrack;
        break;
    case 'ON_SEGMENT': 
        return iconMarkerOnSegment;
        break;
    case 'OFF_NETWORK': 
        return iconMarkerOffNetwork;
        break;
    default: 
        return defaultMarkerIcon;
        break;
    }
}



function pointMouseover(leafletEvent) {
    var layer = leafletEvent.target;
    layer.setIcon(mouseoverMarkerIcon);
//    $scope.$apply(function () {
//        $scope.hoveritem = layer.feature.properties.id;
//    })
}

function pointMouseout(leafletEvent) {
    var layer = leafletEvent.target;
    layer.setIcon(defaultMarkerIcon);

//    $scope.$apply(function () {
//        $scope.hoveritem = {};
//    })
}
