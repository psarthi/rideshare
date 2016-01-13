package com.digitusrevolution.rideshare.common.util.external;

public class LatLngBounds {
	private LatLng southwest, northeast;

	public LatLngBounds() {
	}

	public LatLngBounds(final LatLng southwest, final LatLng northeast) {
		this.southwest = southwest;
		this.northeast = northeast;
	}

	public LatLng getSouthWest() {
		return southwest;
	}

	public void setSouthWest(LatLng southwest) {
		this.southwest = southwest;
	}

	public LatLng getNorthEast() {
		return northeast;
	}

	public void setNorthEast(LatLng northeast) {
		this.northeast = northeast;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LatLngBounds that = (LatLngBounds) o;

		if (northeast != null ? !northeast.equals(that.northeast) : that.northeast != null) return false;
		if (southwest != null ? !southwest.equals(that.southwest) : that.southwest != null) return false;

		return true;
	}

	public void extend(LatLng latLng) {
		if (southwest==null) {
			southwest=latLng.clone();
			if (northeast==null) northeast=latLng.clone();
			return;
		}
		if (northeast==null) {
			northeast=latLng.clone();
			return;
		}

		if (latLng.lat<southwest.lat) 
			southwest.lat=latLng.lat;
		else if (latLng.lat>northeast.lat) 
			northeast.lat=latLng.lat;
		if (latLng.lng<southwest.lng) 
			southwest.lng=latLng.lng;
		else if (latLng.lng>northeast.lng) 
			northeast.lng=latLng.lng;
	}

	public boolean contains(LatLng latLng) {
		if (southwest==null || northeast==null) return false;
		if (latLng.lat<southwest.lat) return false;
		else if (latLng.lat>northeast.lat) return false;
		else if (latLng.lng<southwest.lng) return false;
		else if (latLng.lng>northeast.lng) return false;
		return true;
	}

	public LatLng getCenter() {
		return new LatLng(southwest.lat+(northeast.lat-southwest.lat)/2,southwest.lng+(northeast.lng-southwest.lng)/2);
	}

	@Override
	public int hashCode() {
		int result = southwest != null ? southwest.hashCode() : 0;
		result = 31 * result + (northeast != null ? northeast.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return northeast.toString() + '|' + southwest.toString();
	}
}