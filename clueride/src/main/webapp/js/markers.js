L.AwesomeMarkers.Icon.prototype.options.prefix = 'ion';

var defaultMarkerIcon = L.AwesomeMarkers.icon({
    icon: 'flag',
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
    icon: 'android-done',   /* Single check-mark */
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
    icon: 'android-done-all',   /* Multiple check-mark */
    markerColor: 'orange'
});

var iconMarkerOffNetwork = L.AwesomeMarkers.icon({
    icon: 'heart-broken',    /* Heart with a crack */
    markerColor: 'red'
});

var iconMarkerPathStart = L.AwesomeMarkers.icon({
    icon: 'unlocked',
    markerColor: 'green'
});

var iconMarkerPathEnd = L.AwesomeMarkers.icon({
    icon: 'key',
    markerColor: 'red'
});

var iconMarkerEditable = L.AwesomeMarkers.icon({
    //icon: 'arrow-move',
    icon: 'arrow-expand',
    markerColor: 'lightBlue'
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
    case 'PATH_START':
        return iconMarkerPathStart;
        break;
    case 'PATH_END':
        return iconMarkerPathEnd;
        break;
    default: 
        return defaultMarkerIcon;
        break;
    }
}


function getEditableIcon(latLng) {
    return {
        lat: latLng.lat,
        lng: latLng.lng,
        message: "Editable Marker",
        icon: {
            type: 'awesomeMarker',
            icon: 'arrow-move',
            markerColor: 'blue'
        }
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
