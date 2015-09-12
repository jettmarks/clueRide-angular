
var defaultMarkerIcon = L.AwesomeMarkers.icon({
//    icon: 'coffee',
    markerColor: 'darkblue'
});

var selectedMarkerIcon = L.AwesomeMarkers.icon({
//  icon: 'coffee',
  markerColor: 'lightblue'
});

var mouseoverMarkerIcon = L.AwesomeMarkers.icon({
//    icon: 'coffee',
    markerColor: 'blue'
});

var iconMarkerSingleTrack = L.AwesomeMarkers.icon({
//    icon: 'coffee',
    markerColor: 'darkGreen'
});

var iconMarkerWithin = L.AwesomeMarkers.icon({
//    icon: 'coffee',
    markerColor: 'white'
});

var iconMarkerOnSegment = L.AwesomeMarkers.icon({
//    icon: 'coffee',
    markerColor: 'lightGreen'
});

var iconMarkerOnMultiTrack = L.AwesomeMarkers.icon({
//    icon: 'coffee',
    markerColor: 'orange'
});

var iconMarkerOffNetwork = L.AwesomeMarkers.icon({
//    icon: 'coffee',
    markerColor: 'red'
});

function getMarkerIcon(id, state, selected) {
    console.log(id+": "+state+": "+selected);
    switch(state) {
    case 'UNDEFINED': 
        return selected ? selectedMarkerIcon : defaultMarkerIcon;
        break;
    case 'ON_SINGLE_TRACK': 
        return iconMarkerSingleTrack;
        break;
    case 'ON_MULTI_TRACK': 
        return iconMarkerOnMultiTrack;
        break;
    case 'ON_SEGMENT': 
    case 'ON_NETWORK':
        return iconMarkerOnSegment;
        break;
    case 'OFF_NETWORK': 
        return iconMarkerOffNetwork;
        break;
    case 'WITHIN_GROUP':
        return iconMarkerWithin;
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
