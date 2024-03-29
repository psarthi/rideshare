var directionsService;
var map;
var stepDisplay;
var geocoder;
var image;
//This markers is for points for events etc.
var markers = [];
//This markers is for route points
var routeMarkers = [];
var start;
var end;
var infowindow;
function initMap() {

	// Instantiate a directions service.
	directionsService = new google.maps.DirectionsService;

	// Create a map and center it to specific location as per coordinates.
	map = new google.maps.Map(document.getElementById('map'), {
		zoom : 11,
		center : {
			lat : 12.971091, 
			lng : 77.596429
		}
	});

	// Instantiate an info window to hold step text.
	stepDisplay = new google.maps.InfoWindow;

	// Instantiate geocoder service
	geocoder = new google.maps.Geocoder();

	setMarkerIcon();

	// Add a listener
	map.addListener('click', function(event){
		deleteMarkers();
		//This is to delete previous marker for click event
		document.getElementById('start').value=event.latLng;
		start=event.latLng;
		addMarker(event.latLng);
//		addPermanentMarker(start, image);
	});
	// Add a listener
	map.addListener('rightclick',function(event){
		document.getElementById('end').value=event.latLng;
		end=event.latLng;
		addMarker(event.latLng);
//		addPermanentMarker(end, image);
	});
	// Add a listener
	/*
	map.addListener('dblclick', function(event){
		deleteMarkers();
		document.getElementById('location').value=event.latLng;
		addPermanentMarker(event.latLng, pickupImage);
	});*/

	// global infowindow
	infowindow = new google.maps.InfoWindow();	

}

function setMarkerIcon(){

	pickupIcon = {
			url: "image/pickup.png",
			origin: new google.maps.Point(0, 0),
			anchor: new google.maps.Point(17, 34),
			scaledSize: new google.maps.Size(50, 40)
	};

	dropIcon = {
			url: "image/drop.png",
			origin: new google.maps.Point(0, 0),
			anchor: new google.maps.Point(17, 34),
			scaledSize: new google.maps.Size(50, 40)
	};

	startIcon = {
			url: "image/start.png",
			origin: new google.maps.Point(0, 0),
			anchor: new google.maps.Point(17, 34),
			scaledSize: new google.maps.Size(30, 30)
	};

	endIcon = {
			url: "image/end.png",
			origin: new google.maps.Point(0, 0),
			anchor: new google.maps.Point(17, 34),
			scaledSize: new google.maps.Size(30, 30)
	};

	ridePickupIcon = {
			url: "image/ridepickup.png",
			origin: new google.maps.Point(0, 0),
			anchor: new google.maps.Point(17, 34),
			scaledSize: new google.maps.Size(50, 70)
	};

	rideDropIcon = {
			url: "image/ridedrop.png",
			origin: new google.maps.Point(0, 0),
			anchor: new google.maps.Point(17, 34),
			scaledSize: new google.maps.Size(50, 70)
	};

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
		map: map,
		title: "Marker"
	});
	markers.push(marker);	
}
//Note in this function, i am not adding the marker in the markers array, 
//so that these markers can be distinguised from click event
function addPermanentMarker(latLng, image) {
	var marker = new google.maps.Marker({
		position: latLng,
		map: map,
		icon: image,
		title: "Permanent Marker"
	});	
}


function createCircle(center, radius, fillColor){
	// Add the circle
	// Opacity of 0.0 is for transparent background and higher value between 0 to 1 would fill the color
	var circle = new google.maps.Circle({
		strokeColor: fillColor,
		strokeOpacity: 0.8,
		strokeWeight: 2,
		fillColor: fillColor,
		fillOpacity: 0.35,
		map: map,
		center: center,
		radius: radius
	});
}

function createPolyLine(points){
	var fullPath = new google.maps.Polyline({
		path: points,
		geodesic: false,
		strokeColor: '#FF0000',
		strokeOpacity: 1.0,
		strokeWeight: 2
	});

	fullPath.setMap(map);
}

function showAllPolyLinePoints(ecodedPolyLine) {

	var points = google.maps.geometry.encoding.decodePath(ecodedPolyLine);
	//createPolyLine(points);
	var radius = 5000;
	for (var i = 0; i < points.length; i++) {	
		addMarker(points[i]);
	}
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
//	alert("deleteMarkers");
	clearMarkers();
	markers = [];
	document.getElementById('start').value="";
	start="";
	document.getElementById('end').value="";
	end="";
}

//This will load ride pickup and drop point as well as create circle around the ride request pickup and drop point
function loadGeoJSON(geoString) {
	var geojson = JSON.parse(geoString);
	var type;
	var markerIcon;
	var data = new google.maps.Data();
	var features = geojson.features;
	var fillColor;

	data.addGeoJson(geojson);
	//This is very important, as this only draws on specific map
	data.setMap(map);

	// Add some style.
	data.setStyle(function(feature) {
		var geometry = feature.getGeometry();
		var type = feature.getProperty('type');


		if (type=="startpoint"){
			console.log(type);
			markerIcon = startIcon;
		}
		if (type=="endpoint"){
			console.log(type);
			markerIcon = endIcon;
		}
		if (type=="ridepickuppoint"){
			console.log(type);
			markerIcon = ridePickupIcon;
		}
		if (type=="ridedroppoint"){
			console.log(type);
			markerIcon = rideDropIcon;
		}
		if (type=="pickuppoint"){
			console.log(type);
			//This will get cordinates of the point
			var center = geometry.get();
			var radius = feature.getProperty('DistanceVariation');
			//This will get random color for each pickup point and assuming drop point in next item in the collection
			//so drop point would use the same color 
			fillColor = getRandomColor();
			createCircle(center, radius, fillColor);
			markerIcon = pickupIcon;
		}
		if (type=="droppoint"){
			console.log(type);
			//This will get cordinates of the point
			var center = geometry.get();
			var radius = feature.getProperty('DistanceVariation');
			createCircle(center, radius, fillColor);
			markerIcon = dropIcon;
		}

		return /** @type {google.maps.Data.StyleOptions} */({
			strokeColor: getRandomColor(),
			strokeWeight: 3,
			icon: markerIcon
		});
	});
	
	// When the user clicks, open an infowindow
	data.addListener('click', function(event) {
		var type = event.feature.getProperty('type');
		if (type=="ridepickuppoint" || type=="ridedroppoint"){
			console.log("Event Listener for type:"+type);
			var RideId = event.feature.getProperty("RideId");
			var RideRequestId = event.feature.getProperty("RideRequestId");
			var Distance = event.feature.getProperty("Distance").toPrecision(5);
			var DateTimeUTC = event.feature.getProperty("DateTimeUTC");
			var RideRequestTravelDistance = event.feature.getProperty("RideRequestTravelDistance");
			var Sequence = event.feature.getProperty("Sequence");
			infowindow.setContent("<div style='width:500px; height:50px; text-align: center;'>"
					+"[RideId,RideRequestId,DateTimeUTC,Distance,RideRequestTravelDistance,Sequence]:<br>"+"<b>"+RideId+","
					+RideRequestId+"</b>"+","+DateTimeUTC+","+Distance+","+RideRequestTravelDistance+","+Sequence+"</div>");
			infowindow.setPosition(event.feature.getGeometry().get());
			infowindow.setOptions({pixelOffset: new google.maps.Size(0,-30)});
			infowindow.open(map);
		}
		if (type=="pickuppoint" || type=="droppoint"){
			console.log("Event Listener for type:"+type);
			var RideRequestId = event.feature.getProperty("RideRequestId");
			var DateTimeUTC = event.feature.getProperty("DateTimeUTC");
			var DistanceVariation = event.feature.getProperty("DistanceVariation");
			infowindow.setContent("<div style='width:300px; height:50px; text-align: center;'>"
					+"[RideRequestId,DateTimeUTC,DistanceVariation]:<br>"+"<b>"+RideRequestId+"</b>"+","+DateTimeUTC+","+DistanceVariation+"</div>");
			infowindow.setPosition(event.feature.getGeometry().get());
			infowindow.setOptions({pixelOffset: new google.maps.Size(0,-30)});
			infowindow.open(map);
		}
		if (type=="startpoint" || type=="endpoint"){
			console.log("Event Listener for type:"+type);
			var RideId = event.feature.getProperty("RideId");
			var DateTimeUTC = event.feature.getProperty("DateTimeUTC");
			infowindow.setContent("<div style='width:300px; height:50px; text-align: center;'>"
					+"[RideId,DateTimeUTC]:<br>"+"<b>"+RideId+"</b>"+","+DateTimeUTC+"</div>");
			infowindow.setPosition(event.feature.getGeometry().get());
			infowindow.setOptions({pixelOffset: new google.maps.Size(0,-30)});
			infowindow.open(map);
		}


	});

	// Set mouseover event for each feature.
	data.addListener('mouseover', function(event) {
		var type = event.feature.getProperty('type');
		if (type=="route"){
			console.log("Event Listener for type:"+type);
			var RideId = event.feature.getProperty("RideId");
			var StartDateTimeUTC = event.feature.getProperty("StartDateTimeUTC");		
			$("#info-box").html("[RideId,StartDateTimeUTC]:"+"<b>"+RideId+"</b>"+","+StartDateTimeUTC);
		}
	});

	zoom(map, data);
}

/**
 * Update a map's viewport to fit each geometry in a dataset
 * @param {google.maps.Map} map The map to adjust
 */
function zoom(map, data) {
	var bounds = new google.maps.LatLngBounds();
	data.forEach(function(feature) {
		processPoints(feature.getGeometry(), bounds.extend, bounds);
	});
	map.fitBounds(bounds);
}

/**
 * Process each point in a Geometry, regardless of how deep the points may lie.
 * @param {google.maps.Data.Geometry} geometry The structure to process
 * @param {function(google.maps.LatLng)} callback A function to call on each
 *     LatLng point encountered (e.g. Array.push)
 * @param {Object} thisArg The value of 'this' as provided to 'callback' (e.g.
 *     myArray)
 */
function processPoints(geometry, callback, thisArg) {
	if (geometry instanceof google.maps.LatLng) {
		callback.call(thisArg, geometry);
	} else if (geometry instanceof google.maps.Data.Point) {
		callback.call(thisArg, geometry.get());
	}
	//Not sure why its not working, so commented out as it was causing rest of the flow to stop working 
//	else {
//		geometry.getArray().forEach(function(g) {
//			processPoints(g, callback, thisArg);
//		});
//	}
}

