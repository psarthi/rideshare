
package com.digitusrevolution.rideshare.model.ride.dto.google;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "destination_addresses",
    "origin_addresses",
    "rows",
    "status"
})
public class GoogleDistance {

    @JsonProperty("destination_addresses")
    private List<String> destinationAddresses = null;
    @JsonProperty("origin_addresses")
    private List<String> originAddresses = null;
    @JsonProperty("rows")
    private List<Row> rows = null;
    @JsonProperty("status")
    private String status;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    @JsonProperty("error_message")
    private String error_message;

    @JsonProperty("destination_addresses")
    public List<String> getDestinationAddresses() {
        return destinationAddresses;
    }

    @JsonProperty("destination_addresses")
    public void setDestinationAddresses(List<String> destinationAddresses) {
        this.destinationAddresses = destinationAddresses;
    }

    @JsonProperty("origin_addresses")
    public List<String> getOriginAddresses() {
        return originAddresses;
    }

    @JsonProperty("origin_addresses")
    public void setOriginAddresses(List<String> originAddresses) {
        this.originAddresses = originAddresses;
    }

    @JsonProperty("rows")
    public List<Row> getRows() {
        return rows;
    }

    @JsonProperty("rows")
    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

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
