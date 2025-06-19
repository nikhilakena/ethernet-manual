package com.btireland.talos.quote.facade.dto.quotemanager.response;

import javax.annotation.Nullable;


public class QuoteProductPlaceResponse {
    private String eircode;
    private String handoverNode;
    private String fullAddress;
    private String multiEircode;

    public QuoteProductPlaceResponse() {

    }

    @Nullable
    public String getEircode() {
        return eircode;
    }

    @Nullable
    public String getHandoverNode() {
        return handoverNode;
    }

    @Nullable
    public String getFullAddress() {
        return fullAddress;
    }

    @Nullable
    public String getMultiEircode() {
        return multiEircode;
    }

    public void setEircode(String eircode) {
        this.eircode = eircode;
    }

    public void setHandoverNode(String handoverNode) {
        this.handoverNode = handoverNode;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public void setMultiEircode(String multiEircode) {
        this.multiEircode = multiEircode;
    }

    @Override
    public String toString() {
        return "QuoteProductPlaceResponse{" +
                "eircode='" + eircode + '\'' +
                ", handoverNode='" + handoverNode + '\'' +
                ", fullAddress='" + fullAddress + '\'' +
                ", multiEircode='" + multiEircode + '\'' +
                '}';
    }
}
