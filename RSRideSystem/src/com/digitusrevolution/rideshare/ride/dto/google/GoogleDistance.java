
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
    "destination_addresses",
    "origin_addresses",
    "rows",
    "status"
})
public class GoogleDistance {

    @JsonProperty("destination_addresses")
    private List<String> destinationAddresses = new ArrayList<String>();
    @JsonProperty("origin_addresses")
    private List<String> originAddresses = new ArrayList<String>();
    @JsonProperty("rows")
    private List<Row> rows = new ArrayList<Row>();
    @JsonProperty("status")
    private String status;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The destinationAddresses
     */
    @JsonProperty("destination_addresses")
    public List<String> getDestinationAddresses() {
        return destinationAddresses;
    }

    /**
     * 
     * @param destinationAddresses
     *     The destination_addresses
     */
    @JsonProperty("destination_addresses")
    public void setDestinationAddresses(List<String> destinationAddresses) {
        this.destinationAddresses = destinationAddresses;
    }

    /**
     * 
     * @return
     *     The originAddresses
     */
    @JsonProperty("origin_addresses")
    public List<String> getOriginAddresses() {
        return originAddresses;
    }

    /**
     * 
     * @param originAddresses
     *     The origin_addresses
     */
    @JsonProperty("origin_addresses")
    public void setOriginAddresses(List<String> originAddresses) {
        this.originAddresses = originAddresses;
    }

    /**
     * 
     * @return
     *     The rows
     */
    @JsonProperty("rows")
    public List<Row> getRows() {
        return rows;
    }

    /**
     * 
     * @param rows
     *     The rows
     */
    @JsonProperty("rows")
    public void setRows(List<Row> rows) {
        this.rows = rows;
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

}
