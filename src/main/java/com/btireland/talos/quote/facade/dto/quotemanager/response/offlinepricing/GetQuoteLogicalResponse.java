package com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing;


import com.btireland.talos.quote.facade.base.enumeration.internal.ActionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.ProfileType;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

public class GetQuoteLogicalResponse {

    private Float bandwidth;

    private ProfileType profile;

    private Long ipRange;

    private ActionType action;

    public GetQuoteLogicalResponse() {
        //For json serialization
    }

    public GetQuoteLogicalResponse(Float bandwidth, @NotNull ProfileType profile, @Nullable Long ipRange) {
        this.bandwidth = bandwidth;
        this.profile = profile;
        this.ipRange = ipRange;
    }

    public GetQuoteLogicalResponse(Float bandwidth, @NotNull ProfileType profile, @Nullable Long ipRange, @NotNull ActionType action) {
        this.bandwidth = bandwidth;
        this.profile = profile;
        this.ipRange = ipRange;
        this.action = action;
    }

    public Float getBandwidth() {
        return bandwidth;
    }

    @NotNull
    public ProfileType getProfile() {
        return profile;
    }

    @Nullable
    public Long getIpRange() {
        return ipRange;
    }

    @NotNull
    public ActionType getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "GetQuoteLogicalResponse{" +
            "bandwidth=" + bandwidth +
            ", profile=" + profile +
            ", ipRange=" + ipRange +
            ", action=" + action +
            '}';
    }
}
