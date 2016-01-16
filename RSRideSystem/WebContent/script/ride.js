var getURL = "http://localhost:8080/RSUserSystem/api/domain/users/1/roles";
var postURL = "http://localhost:8080/RSRideSystem/api/domain/ridesystem/dummypost";
var getJSONFormatURL = "http://localhost:8080/RSRideSystem/api/domain/ridesystem/model/ride";
var getRideJSONFormatURL = "http://localhost:8080/RSRideSystem/api/domain/ridesystem/model/ride";
var postRideURL = "http://localhost:8080/RSRideSystem/api/rides";
var getRideRequestJSONFormatURL = "http://localhost:8080/RSRideSystem/api/domain/ridesystem/model/riderequest";
var postRideRequestURL = "http://localhost:8080/RSRideSystem/api/riderequests";
var postData = { City: 'Bangalore', Age: 25 };
var getAllRidesURL = "http://localhost:8080/RSRideSystem/api/ridesystem/rides/allpoints";
var getAllRideRequestURL = "http://localhost:8080/RSRideSystem/api/ridesystem/riderequests/allpoints";
var searchRidesURL = "http://localhost:8080/RSRideSystem/api/ridesystem/rides/search/{rideRequestId}";
var searchRideRequestsURL = "http://localhost:8080/RSRideSystem/api/riderequests/search/{rideId}/0/0";

/*
 * This function is for reference purpose
 */
function get(url){
	$.ajax({
		url: url,
		type: 'GET',
		dataType: 'json'
	})

	.done(function( response ) {
		$("#alert-success").html("Request Successfull:<br/>"+JSON.stringify(response)).show();
		$.each(response,function(key,val){
//			alert(JSON.stringify(key)+","+JSON.stringify(val));
		})
	})

	.fail(function( jqXHR, textStatus ) {
		$("#alert-danger").html("Request Failed:"+textStatus).show();
	});
}

/*
 * This function is for reference purpose
 */
function post(url, data){
	$.ajax({
		url: url,
		type: 'POST',
		data: JSON.stringify(data),
		contentType: 'application/json; charset=utf-8',
		dataType: 'json'
	})

	.done(function( response ) {
		$("#alert-success").html("Request Successfull:<br/>"+JSON.stringify(response)).show();
	})

	.fail(function( jqXHR, textStatus ) {
		$("#alert-danger").html("Request Failed:"+textStatus).show();
	});
}

function hideAlert(){
	$(".alert").hide();
}

//This function not working as expected, close button is not coming up with alert once its closed
$( "#alert-success-close" ).click(function() {
	alert("Hide");
	$( "#alert-success" ).hide();
});

$("#rideOffer").click(function(){
	var startLatLng = $("#start").val().replace("(","").replace(")","").split(",");
	console.log(startLatLng[0]);
	console.log(startLatLng[1]);
	var endLatLng = $("#end").val().replace("(","").replace(")","").split(",");
	console.log(endLatLng[0]);
	console.log(endLatLng[1]);
	var dateTimeLocal = $("#dateTime").val();
	console.log(dateTimeLocal);	
	//This will get date in UTC, Comment this line if you want local timezone
	var dateTimeLocalWithTimezone = new Date(dateTimeLocal);
	//If you don't convert string to the new format as shown below, then you get UTC timezone and not local
	/* Uncomment this code to take data in local time zone instead of UTC
	var dateTimeLocalWithTimezone = new Date(dateTimeLocal.replace(/-/g,'/').replace('T',' '));
	console.log(dateTimeLocalWithTimezone);
	*/
//	var dateUTC = new Date(dateTimeLocal);
//	console.log(dateUTC);

	$.ajax({
		url: getRideJSONFormatURL,
		type: 'GET',
		dataType: 'json'
	})

	.done(function( ride ) {
		//$("#alert-success").html("Request Successfull:<br/>"+JSON.stringify(ride)).show();
		console.log("Request Successfull: Got Ride Object");
		ride.startTime = dateTimeLocalWithTimezone;
		ride.startPoint.point.coordinates[0] = startLatLng[1];
		ride.startPoint.point.coordinates[1] = startLatLng[0];
		ride.endPoint.point.coordinates[0] = endLatLng[1];
		ride.endPoint.point.coordinates[1] = endLatLng[0];
		console.log(ride);
		console.log(JSON.stringify(ride));

		$.ajax({
			url: postRideURL,
			type: 'POST',
			data: JSON.stringify(ride),
			contentType: 'application/json; charset=utf-8',
			dataType: 'json'
		})

		.done(function( response ) {
			$("#alert-success").html("Request Successfull").show();
			loadGeoJSON(JSON.stringify(response));
			deleteMarkers();
		})

		.fail(function( jqXHR, textStatus ) {
			$("#alert-danger").html("Request Failed:"+textStatus).show();
		});

	})

	.fail(function( jqXHR, textStatus ) {
		$("#alert-danger").html("Request Failed: Unable to get Ride Object"+textStatus).show();
	});
	$("#rideFormButton").click();
});


$("#rideRequest").click(function(){
	var startLatLng = $("#start").val().replace("(","").replace(")","").split(",");
	console.log(startLatLng[0]);
	console.log(startLatLng[1]);
	var endLatLng = $("#end").val().replace("(","").replace(")","").split(",");
	console.log(endLatLng[0]);
	console.log(endLatLng[1]);
	var dateTimeLocal = $("#dateTime").val();
	console.log(dateTimeLocal);	
	//This will get date in UTC, Comment this line if you want local timezone
	var dateTimeLocalWithTimezone = new Date(dateTimeLocal);
	//If you don't convert string to the new format as shown below, then you get UTC timezone and not local
	/* Uncomment this code to take data in local time zone instead of UTC
	var dateTimeLocalWithTimezone = new Date(dateTimeLocal.replace(/-/g,'/').replace('T',' '));
	console.log(dateTimeLocalWithTimezone);
	*/
//	var dateUTC = new Date(dateTimeLocal);
//	console.log(dateUTC);

	$.ajax({
		url: getRideRequestJSONFormatURL,
		type: 'GET',
		dataType: 'json'
	})

	.done(function( rideRequest ) {
		//$("#alert-success").html("Request Successfull:<br/>"+JSON.stringify(ride)).show();
		console.log("Request Successfull: Got RideRequest Object");
		rideRequest.pickupTime = dateTimeLocalWithTimezone;
		rideRequest.pickupPoint.point.coordinates[0] = startLatLng[1];
		rideRequest.pickupPoint.point.coordinates[1] = startLatLng[0];		
		rideRequest.dropPoint.point.coordinates[0] = endLatLng[1];
		rideRequest.dropPoint.point.coordinates[1] = endLatLng[0];
		console.log(rideRequest);
		console.log(JSON.stringify(rideRequest));

		$.ajax({
			url: postRideRequestURL,
			type: 'POST',
			data: JSON.stringify(rideRequest),
			contentType: 'application/json; charset=utf-8',
			dataType: 'json'
		})

		.done(function( response ) {
			$("#alert-success").html("Request Successfull").show();
			loadGeoJSON(JSON.stringify(response));
			deleteMarkers();
		})

		.fail(function( jqXHR, textStatus ) {
			$("#alert-danger").html("Request Failed:"+textStatus).show();
		});

	})

	.fail(function( jqXHR, textStatus ) {
		$("#alert-danger").html("Request Failed: Unable to get Ride Request Object"+textStatus).show();
	});
	$("#rideFormButton").click();

});

function searchRide(){
	var rideRequestId = $("#ride-request-id").val();
	var url = searchRidesURL.replace("{rideRequestId}",rideRequestId);
	getGeoJson(url);
}

function searchRideRequest(){
	var rideId = $("#ride-request-id").val();
	var lastSearchDistance = 0;
	var lastResultIndex = 0;
	//Ideally lastSearchDistance and lastResultIndex should be replaced by actual value, for the time being its marked as 0 i.e. its first search
	var url = searchRideRequestsURL.replace("{rideId}",rideId);
	url = url.replace("{lastSearchDistance}",lastSearchDistance);
	url = url.replace("{lastResultIndex}",lastResultIndex);
	getGeoJson(url);
}

function getGeoJson(url){
	$.ajax({
		url: url,
		type: 'GET',
		dataType: 'json'
	})

	.done(function( response ) {
		$("#alert-success").html("Request Successfull").show();
		loadGeoJSON(JSON.stringify(response));
	})

	.fail(function( jqXHR, textStatus ) {
		$("#alert-danger").html("Request Failed:"+textStatus).show();
	});
}
