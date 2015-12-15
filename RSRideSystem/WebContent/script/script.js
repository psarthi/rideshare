var markerArray;
var directionsService;
var map;
var stepDisplay;
var point;
var geocoder;
function initMap() {
	markerArray = [];

	// Instantiate a directions service.
	directionsService = new google.maps.DirectionsService;

	// Create a map and center it on Manhattan.
	map = new google.maps.Map(document.getElementById('map'), {
		zoom : 5,
		center : {
			lat : 12.970689,
			lng : 77.589043
		}
	});

	// Instantiate an info window to hold step text.
	stepDisplay = new google.maps.InfoWindow;

	// Instantiate geocoder service
	geocoder = new google.maps.Geocoder();
}

function calculateAndDisplayRoute(directionsService, markerArray,
		stepDisplay, map) {

	// *****Create a new renderer for directions and bind it to the map, this will retain old rendered lines/routes etc.****
	var directionsDisplay = new google.maps.DirectionsRenderer({
		map : map,
		polylineOptions: {
			strokeColor: getRandomColor()
		}
	});

	// First, remove any existing markers from the map.
	for (var i = 0; i < markerArray.length; i++) {
		markerArray[i].setMap(null);
	}

	// Retrieve the start and end locations and create a DirectionsRequest using
	// WALKING directions.
	directionsService.route({
		origin : document.getElementById('start').value,
		destination : document.getElementById('end').value,
		travelMode : google.maps.TravelMode.WALKING
	}, function(response, status) {
		// Route the directions and pass the response to a function to create
		// markers for each step.
		if (status === google.maps.DirectionsStatus.OK) {
			document.getElementById('warnings-panel').innerHTML = '<b>'
				+ response.routes[0].warnings + '</b>';
			directionsDisplay.setDirections(response);
			var overview_polyline = response.routes[0].overview_polyline;
			//This will show all points returned by Google which is in overview_polyline
	//		showAllPolyLinePoints(overview_polyline);
			//This will show only high level points which is in the steps
			//	showSteps(response, markerArray, stepDisplay, map);
		} else {
			window.alert('Directions request failed due to ' + status);
		}
	});
}

function showSteps(directionResult, markerArray, stepDisplay, map) {
	// For each step, place a marker, and add the text to the marker's infowindow.
	// Also attach the marker to an array so we can keep track of it and remove it
	// when calculating new routes.
	var myRoute = directionResult.routes[0].legs[0];
	for (var i = 0; i < myRoute.steps.length; i++) {
		var marker = markerArray[i] = markerArray[i]
		|| new google.maps.Marker;
		marker.setMap(map);
		marker.setPosition(myRoute.steps[i].start_location);
		attachInstructionText(stepDisplay, marker,
				myRoute.steps[i].instructions, map);
	}
}

function attachInstructionText(stepDisplay, marker, text, map) {
	google.maps.event.addListener(marker, 'click', function() {
		// Open an info window when the marker is clicked on, containing the text
		// of the step.
		stepDisplay.setContent(text);
		stepDisplay.open(map, marker);
	});
}

function locatePoint() {
	address = document.getElementById('point').value;
	var image = {
			url: "image/pickup.png",
			size: new google.maps.Size(71, 71),
			origin: new google.maps.Point(0, 0),
			anchor: new google.maps.Point(17, 34),
			scaledSize: new google.maps.Size(25, 25)
	};
	geocoder
	.geocode(
			{
				'address' : address
			},
			function(results, status) {
				if (status === google.maps.GeocoderStatus.OK) {
					map
					.setCenter(results[0].geometry.location);
					var marker = new google.maps.Marker({
						map : map,
						position : results[0].geometry.location,
						icon: image
					});
				} else {
					alert('Geocode was not successful for the following reason: '
							+ status);
				}
			});
}

function getRandomColor() {

	return '#'+Math.floor(Math.random()*16777215).toString(16);
}


function loadSampleData(){
	document.getElementById('start').value = "Silk Board Bangalore";
	document.getElementById('end').value = "K R Puram Bangalore";
	calculateAndDisplayRoute(directionsService, markerArray,stepDisplay, map);
	document.getElementById('start').value = "Bannerghatta Road Bangalore";
	document.getElementById('end').value = "Hebbal Bangalore";
	calculateAndDisplayRoute(directionsService, markerArray,stepDisplay, map);
	document.getElementById('start').value = "Bannerghatta Road Bangalore";
	document.getElementById('end').value = "K R Puram Bangalore";
	calculateAndDisplayRoute(directionsService, markerArray,stepDisplay, map);
	document.getElementById('start').value = "BTM Layout Bangalore";
	document.getElementById('end').value = "K R Puram Bangalore";
	calculateAndDisplayRoute(directionsService, markerArray,stepDisplay, map);
	document.getElementById('start').value = "Silk Board Bangalore";
	document.getElementById('end').value = "Manyata Tech Park Bangalore";
	calculateAndDisplayRoute(directionsService, markerArray,stepDisplay, map);
	document.getElementById('start').value = "Electronic City Bangalore";
	document.getElementById('end').value = "M G Road Bangalore";
	calculateAndDisplayRoute(directionsService, markerArray,stepDisplay, map);
	document.getElementById('start').value = "Christ University Bangalore";
	document.getElementById('end').value = "Convent Road Bangalore";
	calculateAndDisplayRoute(directionsService, markerArray,stepDisplay, map);

}


function showAllPolyLinePoints(ecodedPolyLine) {

	var points = google.maps.geometry.encoding.decodePath(ecodedPolyLine);
	var fullPath = new google.maps.Polyline({
		path: points,
		geodesic: false,
		strokeColor: '#FF0000',
		strokeOpacity: 1.0,
		strokeWeight: 2
	});

	for (var i = 0; i < points.length; i++) {
		var marker = new google.maps.Marker({
			position: points[i],
			map: map
		});
	}

	// Below line, would draw a full polyline
//	fullPath.setMap(map);
}
