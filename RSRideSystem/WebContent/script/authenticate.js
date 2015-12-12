window.onload = function googleAuthenticate(){
	alert("googleAuthenticate");
	var ref=document.createElement('script')
	ref.setAttribute("type","text/javascript")
	ref.setAttribute("src", "https://maps.googleapis.com/maps/api/js?key=AIzaSyDed4q35hM4sq6Ri1odS9panH1d6exOtcI&callback=initMap");
	ref.setAttribute("async", "");
	ref.setAttribute("defer", "");
	document.body.appendChild(ref);
}

