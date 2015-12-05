
package com.digitusrevolution.rideshare.ride.dto;

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
    "geocoder_status",
    "place_id",
    "types",
    "partial_match"
})
public class GeocodedWaypoint {

    @JsonProperty("geocoder_status")
    private String geocoderStatus;
    @JsonProperty("place_id")
    private String placeId;
    @JsonProperty("types")
    private List<String> types = new ArrayList<String>();
    @JsonProperty("partial_match")
    private Boolean partialMatch;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The geocoderStatus
     */
    @JsonProperty("geocoder_status")
    public String getGeocoderStatus() {
        return geocoderStatus;
    }

    /**
     * 
     * @param geocoderStatus
     *     The geocoder_status
     */
    @JsonProperty("geocoder_status")
    public void setGeocoderStatus(String geocoderStatus) {
        this.geocoderStatus = geocoderStatus;
    }

    /**
     * 
     * @return
     *     The placeId
     */
    @JsonProperty("place_id")
    public String getPlaceId() {
        return placeId;
    }

    /**
     * 
     * @param placeId
     *     The place_id
     */
    @JsonProperty("place_id")
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * 
     * @return
     *     The types
     */
    @JsonProperty("types")
    public List<String> getTypes() {
        return types;
    }

    /**
     * 
     * @param types
     *     The types
     */
    @JsonProperty("types")
    public void setTypes(List<String> types) {
        this.types = types;
    }

    /**
     * 
     * @return
     *     The partialMatch
     */
    @JsonProperty("partial_match")
    public Boolean getPartialMatch() {
        return partialMatch;
    }

    /**
     * 
     * @param partialMatch
     *     The partial_match
     */
    @JsonProperty("partial_match")
    public void setPartialMatch(Boolean partialMatch) {
        this.partialMatch = partialMatch;
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
