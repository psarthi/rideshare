
package com.digitusrevolution.rideshare.ride.dto.google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "geocoded_waypoints",
    "routes",
    "status"
})
public class GoogleDirection {

    @JsonProperty("geocoded_waypoints")
    private List<GeocodedWaypoint> geocodedWaypoints = new ArrayList<GeocodedWaypoint>();
    @JsonProperty("routes")
    private List<Route> routes = new ArrayList<Route>();
    @JsonProperty("status")
    private String status;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    @JsonProperty("error_message")
    private String error_message;

    /**
     * 
     * @return
     *     The geocodedWaypoints
     */
    @JsonProperty("geocoded_waypoints")
    public List<GeocodedWaypoint> getGeocodedWaypoints() {
        return geocodedWaypoints;
    }

    /**
     * 
     * @param geocodedWaypoints
     *     The geocoded_waypoints
     */
    @JsonProperty("geocoded_waypoints")
    public void setGeocodedWaypoints(List<GeocodedWaypoint> geocodedWaypoints) {
        this.geocodedWaypoints = geocodedWaypoints;
    }

    /**
     * 
     * @return
     *     The routes
     */
    @JsonProperty("routes")
    public List<Route> getRoutes() {
        return routes;
    }

    /**
     * 
     * @param routes
     *     The routes
     */
    @JsonProperty("routes")
    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    /**
     * 
     * @return
     *     The status
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
    
    @JsonProperty("error_message")
	public String getError_message() {
		return error_message;
	}

    @JsonProperty("error_message")
	public void setError_message(String error_message) {
		this.error_message = error_message;
	}


}
