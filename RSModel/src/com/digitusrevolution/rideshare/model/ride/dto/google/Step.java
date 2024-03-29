
package com.digitusrevolution.rideshare.model.ride.dto.google;

import java.util.HashMap;
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
    "distance",
    "duration",
    "end_location",
    "html_instructions",
    "polyline",
    "start_location",
    "travel_mode",
    "maneuver"
})
public class Step {

    @JsonProperty("distance")
    private Distance distance;
    @JsonProperty("duration")
    private Duration duration;
    @JsonProperty("end_location")
    private EndLocation endLocation;
    @JsonProperty("html_instructions")
    private String htmlInstructions;
    @JsonProperty("polyline")
    private Polyline polyline;
    @JsonProperty("start_location")
    private StartLocation startLocation;
    @JsonProperty("travel_mode")
    private String travelMode;
    @JsonProperty("maneuver")
    private String maneuver;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The distance
     */
    @JsonProperty("distance")
    public Distance getDistance() {
        return distance;
    }

    /**
     * 
     * @param distance
     *     The distance
     */
    @JsonProperty("distance")
    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    /**
     * 
     * @return
     *     The duration
     */
    @JsonProperty("duration")
    public Duration getDuration() {
        return duration;
    }

    /**
     * 
     * @param duration
     *     The duration
     */
    @JsonProperty("duration")
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    /**
     * 
     * @return
     *     The endLocation
     */
    @JsonProperty("end_location")
    public EndLocation getEndLocation() {
        return endLocation;
    }

    /**
     * 
     * @param endLocation
     *     The end_location
     */
    @JsonProperty("end_location")
    public void setEndLocation(EndLocation endLocation) {
        this.endLocation = endLocation;
    }

    /**
     * 
     * @return
     *     The htmlInstructions
     */
    @JsonProperty("html_instructions")
    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    /**
     * 
     * @param htmlInstructions
     *     The html_instructions
     */
    @JsonProperty("html_instructions")
    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
    }

    /**
     * 
     * @return
     *     The polyline
     */
    @JsonProperty("polyline")
    public Polyline getPolyline() {
        return polyline;
    }

    /**
     * 
     * @param polyline
     *     The polyline
     */
    @JsonProperty("polyline")
    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    /**
     * 
     * @return
     *     The startLocation
     */
    @JsonProperty("start_location")
    public StartLocation getStartLocation() {
        return startLocation;
    }

    /**
     * 
     * @param startLocation
     *     The start_location
     */
    @JsonProperty("start_location")
    public void setStartLocation(StartLocation startLocation) {
        this.startLocation = startLocation;
    }

    /**
     * 
     * @return
     *     The travelMode
     */
    @JsonProperty("travel_mode")
    public String getTravelMode() {
        return travelMode;
    }

    /**
     * 
     * @param travelMode
     *     The travel_mode
     */
    @JsonProperty("travel_mode")
    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    /**
     * 
     * @return
     *     The maneuver
     */
    @JsonProperty("maneuver")
    public String getManeuver() {
        return maneuver;
    }

    /**
     * 
     * @param maneuver
     *     The maneuver
     */
    @JsonProperty("maneuver")
    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
