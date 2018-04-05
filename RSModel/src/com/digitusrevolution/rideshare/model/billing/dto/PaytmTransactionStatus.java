
package com.digitusrevolution.rideshare.model.billing.dto;

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
    "TXNID",
    "BANKTXNID",
    "ORDERID",
    "TXNAMOUNT",
    "STATUS",
    "TXNTYPE",
    "GATEWAYNAME",
    "RESPCODE",
    "RESPMSG",
    "BANKNAME",
    "MID",
    "PAYMENTMODE",
    "REFUNDAMT",
    "TXNDATE"
})
public class PaytmTransactionStatus {

    @JsonProperty("TXNID")
    private String tXNID;
    @JsonProperty("BANKTXNID")
    private String bANKTXNID;
    @JsonProperty("ORDERID")
    private String oRDERID;
    @JsonProperty("TXNAMOUNT")
    private String tXNAMOUNT;
    @JsonProperty("STATUS")
    private String sTATUS;
    @JsonProperty("TXNTYPE")
    private String tXNTYPE;
    @JsonProperty("GATEWAYNAME")
    private String gATEWAYNAME;
    @JsonProperty("RESPCODE")
    private String rESPCODE;
    @JsonProperty("RESPMSG")
    private String rESPMSG;
    @JsonProperty("BANKNAME")
    private String bANKNAME;
    @JsonProperty("MID")
    private String mID;
    @JsonProperty("PAYMENTMODE")
    private String pAYMENTMODE;
    @JsonProperty("REFUNDAMT")
    private String rEFUNDAMT;
    @JsonProperty("TXNDATE")
    private String tXNDATE;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("TXNID")
    public String getTXNID() {
        return tXNID;
    }

    @JsonProperty("TXNID")
    public void setTXNID(String tXNID) {
        this.tXNID = tXNID;
    }

    @JsonProperty("BANKTXNID")
    public String getBANKTXNID() {
        return bANKTXNID;
    }

    @JsonProperty("BANKTXNID")
    public void setBANKTXNID(String bANKTXNID) {
        this.bANKTXNID = bANKTXNID;
    }

    @JsonProperty("ORDERID")
    public String getORDERID() {
        return oRDERID;
    }

    @JsonProperty("ORDERID")
    public void setORDERID(String oRDERID) {
        this.oRDERID = oRDERID;
    }

    @JsonProperty("TXNAMOUNT")
    public String getTXNAMOUNT() {
        return tXNAMOUNT;
    }

    @JsonProperty("TXNAMOUNT")
    public void setTXNAMOUNT(String tXNAMOUNT) {
        this.tXNAMOUNT = tXNAMOUNT;
    }

    @JsonProperty("STATUS")
    public String getSTATUS() {
        return sTATUS;
    }

    @JsonProperty("STATUS")
    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

    @JsonProperty("TXNTYPE")
    public String getTXNTYPE() {
        return tXNTYPE;
    }

    @JsonProperty("TXNTYPE")
    public void setTXNTYPE(String tXNTYPE) {
        this.tXNTYPE = tXNTYPE;
    }

    @JsonProperty("GATEWAYNAME")
    public String getGATEWAYNAME() {
        return gATEWAYNAME;
    }

    @JsonProperty("GATEWAYNAME")
    public void setGATEWAYNAME(String gATEWAYNAME) {
        this.gATEWAYNAME = gATEWAYNAME;
    }

    @JsonProperty("RESPCODE")
    public String getRESPCODE() {
        return rESPCODE;
    }

    @JsonProperty("RESPCODE")
    public void setRESPCODE(String rESPCODE) {
        this.rESPCODE = rESPCODE;
    }

    @JsonProperty("RESPMSG")
    public String getRESPMSG() {
        return rESPMSG;
    }

    @JsonProperty("RESPMSG")
    public void setRESPMSG(String rESPMSG) {
        this.rESPMSG = rESPMSG;
    }

    @JsonProperty("BANKNAME")
    public String getBANKNAME() {
        return bANKNAME;
    }

    @JsonProperty("BANKNAME")
    public void setBANKNAME(String bANKNAME) {
        this.bANKNAME = bANKNAME;
    }

    @JsonProperty("MID")
    public String getMID() {
        return mID;
    }

    @JsonProperty("MID")
    public void setMID(String mID) {
        this.mID = mID;
    }

    @JsonProperty("PAYMENTMODE")
    public String getPAYMENTMODE() {
        return pAYMENTMODE;
    }

    @JsonProperty("PAYMENTMODE")
    public void setPAYMENTMODE(String pAYMENTMODE) {
        this.pAYMENTMODE = pAYMENTMODE;
    }

    @JsonProperty("REFUNDAMT")
    public String getREFUNDAMT() {
        return rEFUNDAMT;
    }

    @JsonProperty("REFUNDAMT")
    public void setREFUNDAMT(String rEFUNDAMT) {
        this.rEFUNDAMT = rEFUNDAMT;
    }

    @JsonProperty("TXNDATE")
    public String getTXNDATE() {
        return tXNDATE;
    }

    @JsonProperty("TXNDATE")
    public void setTXNDATE(String tXNDATE) {
        this.tXNDATE = tXNDATE;
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
