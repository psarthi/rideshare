
package com.digitusrevolution.rideshare.model.billing.dto.paytm;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "requestGuid",
    "orderId",
    "status",
    "statusCode",
    "statusMessage",
    "response",
    "metadata"
})
public class PaytmGratificationStatusResponse {

    @JsonProperty("type")
    private Object type;
    @JsonProperty("requestGuid")
    private Object requestGuid;
    @JsonProperty("orderId")
    private Object orderId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("statusCode")
    private String statusCode;
    @JsonProperty("statusMessage")
    private String statusMessage;
    @JsonProperty("response")
    private Response response;
    @JsonProperty("metadata")
    private Object metadata;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("type")
    public Object getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(Object type) {
        this.type = type;
    }

    @JsonProperty("requestGuid")
    public Object getRequestGuid() {
        return requestGuid;
    }

    @JsonProperty("requestGuid")
    public void setRequestGuid(Object requestGuid) {
        this.requestGuid = requestGuid;
    }

    @JsonProperty("orderId")
    public Object getOrderId() {
        return orderId;
    }

    @JsonProperty("orderId")
    public void setOrderId(Object orderId) {
        this.orderId = orderId;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("statusCode")
    public String getStatusCode() {
        return statusCode;
    }

    @JsonProperty("statusCode")
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty("statusMessage")
    public String getStatusMessage() {
        return statusMessage;
    }

    @JsonProperty("statusMessage")
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @JsonProperty("response")
    public Response getResponse() {
        return response;
    }

    @JsonProperty("response")
    public void setResponse(Response response) {
        this.response = response;
    }

    @JsonProperty("metadata")
    public Object getMetadata() {
        return metadata;
    }

    @JsonProperty("metadata")
    public void setMetadata(Object metadata) {
        this.metadata = metadata;
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
