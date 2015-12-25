var directionsService;
var map;
var stepDisplay;
var geocoder;
var start;
var end;
var address;
var image;
//This markers is for points for events etc.
var markers = [];
//This markers is for route points
var routeMarkers = [];
function initMap() {

	// Instantiate a directions service.
	directionsService = new google.maps.DirectionsService;

	// Create a map and center it to specific location as per coordinates.
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

	image = {
			url: "image/pickup.png",
			size: new google.maps.Size(71, 71),
			origin: new google.maps.Point(0, 0),
			anchor: new google.maps.Point(17, 34),
			scaledSize: new google.maps.Size(40, 40)
	};

	// Add a listener
	map.addListener('click', function(event){
		document.getElementById('start').value=event.latLng;
		start = event.latLng;
		addMarker(event.latLng);
	});
	// Add a listener
	map.addListener('rightclick',function(event){
		document.getElementById('end').value=event.latLng;
		end = event.latLng;
		calculateAndDisplayRoute(directionsService, routeMarkers,
				stepDisplay, map, start, end);
	});
	// Add a listener
	map.addListener('dblclick', function(event){
		document.getElementById('address').value=event.latLng;
		addPermanentMarker(event.latLng, image);
		deleteMarkers();
	});

}

function calculateAndDisplayRoute(directionsService, routeMarkers,
		stepDisplay, map, start, end) {

	// *****Create a new renderer for directions and bind it to the map, this will retain old rendered lines/routes etc.****
	var directionsDisplay = new google.maps.DirectionsRenderer({
		map : map,
		polylineOptions: {
			strokeColor: getRandomColor()
		}
	});

	// First, remove any existing route markers from the map.
	for (var i = 0; i < routeMarkers.length; i++) {
		routeMarkers[i].setMap(null);
	}
	
	// Retrieve the start and end locations and create a DirectionsRequest using
	// WALKING directions.
	directionsService.route({
		origin : start,
		destination : end,
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
			//showAllPolyLinePoints(overview_polyline);
			//This will show only high level points which is in the steps
			//showSteps(response, routeMarkers, stepDisplay, map);
		} else {
			window.alert('Directions request failed due to ' + status);
		}
	});
}

function showSteps(directionResult, routeMarkers, stepDisplay, map) {
	// For each step, place a marker, and add the text to the marker's infowindow.
	// Also attach the marker to an array so we can keep track of it and remove it
	// when calculating new routes.
	var myRoute = directionResult.routes[0].legs[0];
	for (var i = 0; i < myRoute.steps.length; i++) {
		var marker = routeMarkers[i] = routeMarkers[i] || new google.maps.Marker;
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

function geocodeAddress(address) {
	geocoder.geocode({'address' : address},
			function(results, status) {
		if (status === google.maps.GeocoderStatus.OK) {
			map.setCenter(results[0].geometry.location);
			addPermanentMarker(results[0].geometry.location,image);
		} else {
			alert('Geocode was not successful for the following reason: '+ status);
		}
	});
}

function getRandomColor() {

	return '#'+Math.floor(Math.random()*16777215).toString(16);
}

/*
 *  There is no real function overloading in JavaScript since it allows
 *  to pass any number of parameters of any type. You have to check inside
 *  the function how many arguments have been passed and what type they are.
 *  So you can modify this function and add arguments as required,
 *  but you can't have multiple function with same but different arguments like java 
 */
function addMarker(latLng) {
	var marker = new google.maps.Marker({
		position: latLng,
		map: map
	});
	markers.push(marker);	
}
//Note in this function, i am not adding the marker in the markers array, 
//so that these markers can be distinguised from click event
function addPermanentMarker(latLng, image) {
	var marker = new google.maps.Marker({
		position: latLng,
		map: map,
		icon: image
	});	
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
		addMarker(points[i]);
	}

	// Below line, would draw a full polyline
//	fullPath.setMap(map);
}

//Sets the map on all markers in the array.
function setMapOnAll(map) {
	//alert("markers.length"+markers.length);
	for (var i = 0; i < markers.length; i++) {
		markers[i].setMap(map);
	}
}

//Removes the markers from the map, but keeps them in the array.
function clearMarkers() {
	//alert("clearMarkers");
	setMapOnAll(null);
}

//Shows any markers currently in the array.
function showMarkers() {
	//alert("showMarkers");
	setMapOnAll(map);
}

//Deletes all markers in the array by removing references to them.
function deleteMarkers() {
	//alert("deleteMarkers");
	clearMarkers();
	markers = [];
	document.getElementById('start').value="";
	document.getElementById('end').value="";
}

