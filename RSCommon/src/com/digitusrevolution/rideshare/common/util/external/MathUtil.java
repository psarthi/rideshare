package com.digitusrevolution.rideshare.common.util.external;

class MathUtil {
	
	/**
	 * Extend the Number object to convert degrees to radians
	 *
	 * @return {Number} Bearing in radians
	 * @ignore
	 */ 
	public static double toRad(double value) {
		return value * Math.PI / 180;
	};

	/**
	 * Extend the Number object to convert radians to degrees
	 *
	 * @return {Number} Bearing in degrees
	 * @ignore
	 */ 
	public static double toDeg(double value) {
		return value * 180 / Math.PI;
	};

	/**
	 * Normalize a heading in degrees to between 0 and +360
	 *
	 * @return {Number} Return 
	 * @ignore
	 */ 
	public static double toBrng(double value) {
		return (toDeg(value) + 360) % 360;
	};
	
	/**
	 * A ‘rhumb line’ (or loxodrome) is a path of constant bearing, which crosses all meridians at the same angle.
	 * see http://www.movable-type.co.uk/scripts/latlong.html
	 * @param brng
	 * @param dist
	 * @return
	 */
	public static LatLng rhumbDestinationPoint(LatLng startPoint, double brng, double dist) {
		double R = 6371;
		double d = dist/R;  // d = angular distance covered on earth’s surface
		double lat1 = startPoint.latRad(), lon1 = startPoint.lngRad();
		brng = MathUtil.toRad(brng);

		double dLat = d*Math.cos(brng);
		// nasty kludge to overcome ill-conditioned results around parallels of latitude:
		if (Math.abs(dLat) < 1e-10) dLat = 0; // dLat < 1 mm

		double lat2 = lat1 + dLat;
		double dPhi = Math.log(Math.tan(lat2/2+Math.PI/4)/Math.tan(lat1/2+Math.PI/4));
		double q = (dPhi!=0) ? dLat/dPhi : Math.cos(lat1);  // E-W line gives dPhi=0
		double dLon = d*Math.sin(brng)/q;

		// check for some daft bugger going past the pole, normalise latitude if so
		if (Math.abs(lat2) > Math.PI/2) lat2 = lat2>0 ? Math.PI-lat2 : -Math.PI-lat2;

		double lon2 = (lon1+dLon+3*Math.PI)%(2*Math.PI) - Math.PI;

		return new LatLng(MathUtil.toDeg(lat2), MathUtil.toDeg(lon2));
	};

	/**
	 * Given a start point and a distance d along constant bearing θ, this will calculate the destination point. 
	 * If you maintain a constant bearing along a rhumb line, you will gradually spiral in towards one of the poles.
	 * see http://www.movable-type.co.uk/scripts/latlong.html
	 * @param dest
	 * @return
	 */
	public static double rhumbBearingTo(LatLng start, LatLng dest) {
		double dLon = MathUtil.toRad(dest.lng() - start.lng());
		double dPhi = Math.log(Math.tan(dest.latRad() / 2 + Math.PI / 4) / Math.tan(start.latRad() / 2 + Math.PI / 4));
		if (Math.abs(dLon) > Math.PI) {
			dLon = dLon > 0 ? -(2 * Math.PI - dLon) : (2 * Math.PI + dLon);
		}
		return MathUtil.toBrng(Math.atan2(dLon, dPhi));
	};
}
