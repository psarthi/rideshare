package com.digitusrevolution.rideshare.common.util.external;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/*
 * Reason behind not changing it to LatLng of google, 
 * as that function is using a different naming convention for lat/lng and 
 * apart from that just want to keep this and PolyUtil project separate as its an external project
 */
public class LatLng implements Cloneable {
	public double lat;
	public double lng;
	public LatLng() {
		lat=0;
		lng=0;
	}
	public LatLng(double lat, double lng) {
		this.lat=lat;
		this.lng=lng;
	}

	public double lat() {
		return lat;
	}

	public double lng() {
		return lng;
	}

	public double latRad() {
		return MathUtil.toRad(lat);
	}

	public double lngRad() {
		return MathUtil.toRad(lng);
	}

	
	public String toString() {
		return formatLatOrLong(lat)+","+formatLatOrLong(lng);
	}

	private String formatLatOrLong(double latOrLng) {
		NumberFormat nf;
		DecimalFormatSymbols fts = new DecimalFormatSymbols(Locale.US);
		nf = new DecimalFormat("##0.000000",fts);
		return nf.format(latOrLng);
	}

	public LatLng clone() {
		return new LatLng(lat,lng);
	}
}

