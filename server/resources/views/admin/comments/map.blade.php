@extends('layouts.master')
@section("assets")
  <style>
    /* Always set the map height explicitly to define the size of the div
     * element that contains the map. */
    #map {
      height: 900px;
      /*height: 100%;*/
    }

    /* Optional: Makes the sample page fill the window. */
    html, body {
      height: 100%;
      margin: 0;
      padding: 0;
    }
  </style>
@endsection
@section('content')
  <div class="containerr">
    {{--<h1> Safe Zones Map</h1>--}}
    <div id="map"></div>
  </div>
@endsection

@section("scripts")
  <script type="text/javascript">
    var markers = {!! json_encode($safezones) !!};
    function initMap() {
      myLocation();
//      var myLatLng = {lat: -25.745939, lng: 28.210848};
//      var map = new google.maps.Map(document.getElementById('map'), {
//        center: myLatLng,
//        zoom: 13,
//        styles: [{
//          "featureType": "water",
//          "stylers": [{"saturation": 43},
//            {"lightness": -11},
//            {"hue": "#0088ff"}]
//        },
//          {
//            "featureType": "road", "elementType": "geometry.fill", "stylers": [
//            {"hue": "#ff0000"},
//            {"saturation": -100},
//            {"lightness": 99}]
//          },
//          {
//            "featureType": "road", "elementType": "geometry.stroke",
//            "stylers": [{"color": "#808080"},
//              {"lightness": 54}]
//          },
//          {
//            "featureType": "landscape.man_made", "elementType": "geometry.fill",
//            "stylers": [{"color": "#ece2d9"}]
//          },
//          {
//            "featureType": "poi.park", "elementType": "geometry.fill",
//            "stylers": [{"color": "#ccdca1"}]
//          }, {
//            "featureType": "road",
//            "elementType": "labels.text.fill",
//            "stylers": [{"color": "#767676"}]
//          }, {
//            "featureType": "road",
//            "elementType": "labels.text.stroke",
//            "stylers": [{"color": "#ffffff"}]
//          }, {"featureType": "poi", "stylers": [{"visibility": "off"}]}, {
//            "featureType": "landscape.natural",
//            "elementType": "geometry.fill",
//            "stylers": [{"visibility": "on"}, {"color": "#b8cb93"}]
//          }, {"featureType": "poi.park", "stylers": [{"visibility": "on"}]}, {
//            "featureType": "poi.sports_complex",
//            "stylers": [{"visibility": "on"}]
//          }, {"featureType": "poi.medical", "stylers": [{"visibility": "on"}]}, {
//            "featureType": "poi.business",
//            "stylers": [{"visibility": "simplified"}]
//          }]
//      });
//
//      myLocation(map);
//
//      for (var i = 0; i < markers.length; i++) {
//        var pos = markers[i];
//        var pinImage = new google.maps.MarkerImage("http://www.googlemapsmarkers.com/v1/009900/");
//        var latLng = {
//          lat: parseFloat(pos.latitude),
//          lng: parseFloat(pos.longitude)
//        };
//        var marker = new google.maps.Marker({
//          id: pos.name,
//          icon: pinImage,
//          position: latLng,
//          map: map,
//          title: markers[i].name
//        });
//
//        var cityCircle = new google.maps.Circle({
//          strokeColor: '#FF0000',
//          strokeOpacity: 0.8,
//          strokeWeight: 2,
//          fillColor: '#FF0000',
//          fillOpacity: 0.35,
//          map: map,
//          center: latLng,
//          radius: 500//Math.sqrt(citymap[city].population) * 100
//        });
//
//        windowModal(marker);
//      }
    }

    function windowModal(marker) {
      var infowindow = new google.maps.InfoWindow({
        content: marker.title
      });

      marker.addListener('click', function () {
        infowindow.open(marker.get('map'), marker);
      });
    }


    function myLocation() {
      navigator.geolocation.getCurrentPosition(function (position) {
            var myLatLng = {
              lat: position.coords.latitude,
              lng: position.coords.longitude
            };

            // Try HTML5 geolocation.
            if (navigator.geolocation) {
              var map = new google.maps.Map(document.getElementById('map'), {
                center: myLatLng,
                zoom: 12
              });


              var GEOCODING = 'https://maps.googleapis.com/maps/api/geocode/json?latlng=' +
                  position.coords.latitude + '%2C' +
                  position.coords.longitude + '&language=en';

              $.getJSON(GEOCODING).done(function (location) {
                var marker = new google.maps.Marker({
                  position: myLatLng,
                  map: map,
                  title: location.results[0].formatted_address
                });

                map.setCenter(myLatLng);
                map.setCenter(marker.getPosition());
                windowModal(marker);
              });

              for (var i = 0; i < markers.length; i++) {
                var pos = markers[i];
                var pinImage = new google.maps.MarkerImage("http://maps.google.com/mapfiles/ms/icons/blue-dot.png");
                var latLng = {
                  lat: parseFloat(pos.latitude),
                  lng: parseFloat(pos.longitude)
                };
                marker = new google.maps.Marker({
                  id: pos.name,
                  icon: pinImage,
                  position: latLng,
                  map: map,
                  title: markers[i].name
                });

                var cityCircle = new google.maps.Circle({
                  strokeColor: '#FF0000',
                  strokeOpacity: 0.8,
                  strokeWeight: 2,
                  fillColor: '#FF0000',
                  fillOpacity: 0.35,
                  map: map,
                  center: latLng,
                  radius: parseFloat(markers[i].radius)//Math.sqrt(citymap[city].population) * 100
                });
                windowModal(marker);
              }
            }
            else {
              // Browser doesn't support Geolocation
              handleLocationError(false, infoWindow, map.getCenter());
            }
          }, function () {
            handleLocationError(true, infoWindow, map.getCenter());
          }
      )
    }

    function myLocation2(map) {
      // Try HTML5 geolocation.
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
          var pos = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
          };

          var GEOCODING = 'https://maps.googleapis.com/maps/api/geocode/json?latlng=' +
              position.coords.latitude + '%2C' +
              position.coords.longitude + '&language=en';

          $.getJSON(GEOCODING).done(function (location) {
            var marker = new google.maps.Marker({
              position: pos,
              map: map,
              title: location.results[0].formatted_address
            });

            map.setCenter(pos);
            map.setCenter(marker.getPosition());
            windowModal(marker);

          });
        }, function () {
          handleLocationError(true, infoWindow, map.getCenter());
        });
      } else {
        // Browser doesn't support Geolocation
        handleLocationError(false, infoWindow, map.getCenter());
      }
    }

    function handleLocationError(browserHasGeolocation, infoWindow, pos) {
      infoWindow.setPosition(pos);
      infoWindow.setContent(browserHasGeolocation ?
          'Error: The Geolocation service failed.' :
          'Error: Your browser doesn\'t support geolocation.');
    }
  </script>
  <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDk_BZkzSg0UMXtT_R-ijdp8sX8aiu22NY&callback=initMap"
          async defer></script>
@endsection