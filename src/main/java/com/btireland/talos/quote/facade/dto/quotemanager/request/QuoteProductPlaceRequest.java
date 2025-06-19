package com.btireland.talos.quote.facade.dto.quotemanager.request;


import javax.annotation.Nullable;


public class QuoteProductPlaceRequest {
    private String eircode;
    private String handoverNode;

    public QuoteProductPlaceRequest() {
    }

    @Nullable
    public String getEircode() {
        return eircode;
    }

    @Nullable
    public String getHandoverNode() {
        return handoverNode;
    }

    public void setEircode(String eircode) {
        this.eircode = eircode;
    }

    public void setHandoverNode(String handoverNode) {
        this.handoverNode = handoverNode;
    }

    @Override
    public String toString() {
        return "QuoteProductPlaceRequest{" +
                "eircode='" + eircode + '\'' +
                ", handoverNode='" + handoverNode + '\'' +
                '}';
    }
}
