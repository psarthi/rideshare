
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
    "txnGuid",
    "txnAmount",
    "status",
    "message",
    "txnErrorCode",
    "ssoId",
    "txnType",
    "merchantOrderId",
    "pgTxnId",
    "pgRefundId",
    "cashbackTxnId",
    "isLimitPending"
})
public class TxnList {

    @JsonProperty("txnGuid")
    private String txnGuid;
    @JsonProperty("txnAmount")
    private Integer txnAmount;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("txnErrorCode")
    private Object txnErrorCode;
    @JsonProperty("ssoId")
    private String ssoId;
    @JsonProperty("txnType")
    private String txnType;
    @JsonProperty("merchantOrderId")
    private String merchantOrderId;
    @JsonProperty("pgTxnId")
    private String pgTxnId;
    @JsonProperty("pgRefundId")
    private String pgRefundId;
    @JsonProperty("cashbackTxnId")
    private Object cashbackTxnId;
    @JsonProperty("isLimitPending")
    private Boolean isLimitPending;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("txnGuid")
    public String getTxnGuid() {
        return txnGuid;
    }

    @JsonProperty("txnGuid")
    public void setTxnGuid(String txnGuid) {
        this.txnGuid = txnGuid;
    }

    @JsonProperty("txnAmount")
    public Integer getTxnAmount() {
        return txnAmount;
    }

    @JsonProperty("txnAmount")
    public void setTxnAmount(Integer txnAmount) {
        this.txnAmount = txnAmount;
    }

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("txnErrorCode")
    public Object getTxnErrorCode() {
        return txnErrorCode;
    }

    @JsonProperty("txnErrorCode")
    public void setTxnErrorCode(Object txnErrorCode) {
        this.txnErrorCode = txnErrorCode;
    }

    @JsonProperty("ssoId")
    public String getSsoId() {
        return ssoId;
    }

    @JsonProperty("ssoId")
    public void setSsoId(String ssoId) {
        this.ssoId = ssoId;
    }

    @JsonProperty("txnType")
    public String getTxnType() {
        return txnType;
    }

    @JsonProperty("txnType")
    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    @JsonProperty("merchantOrderId")
    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    @JsonProperty("merchantOrderId")
    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    @JsonProperty("pgTxnId")
    public String getPgTxnId() {
        return pgTxnId;
    }

    @JsonProperty("pgTxnId")
    public void setPgTxnId(String pgTxnId) {
        this.pgTxnId = pgTxnId;
    }

    @JsonProperty("pgRefundId")
    public String getPgRefundId() {
        return pgRefundId;
    }

    @JsonProperty("pgRefundId")
    public void setPgRefundId(String pgRefundId) {
        this.pgRefundId = pgRefundId;
    }

    @JsonProperty("cashbackTxnId")
    public Object getCashbackTxnId() {
        return cashbackTxnId;
    }

    @JsonProperty("cashbackTxnId")
    public void setCashbackTxnId(Object cashbackTxnId) {
        this.cashbackTxnId = cashbackTxnId;
    }

    @JsonProperty("isLimitPending")
    public Boolean getIsLimitPending() {
        return isLimitPending;
    }

    @JsonProperty("isLimitPending")
    public void setIsLimitPending(Boolean isLimitPending) {
        this.isLimitPending = isLimitPending;
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
