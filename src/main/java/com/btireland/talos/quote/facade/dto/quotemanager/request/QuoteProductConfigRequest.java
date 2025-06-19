package com.btireland.talos.quote.facade.dto.quotemanager.request;

import com.btireland.talos.quote.facade.base.enumeration.internal.ActionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.NetworkType;
import com.btireland.talos.quote.facade.base.enumeration.internal.ProfileType;
import com.btireland.talos.quote.facade.base.enumeration.internal.SlaType;
import javax.annotation.Nullable;

public class QuoteProductConfigRequest {

    private Float bandwidth;
    private Long ipRange;
    private ProfileType profile;
    private ActionType action;
    private NetworkType accessSupplier;
    private SlaType sla;

    private QuoteProductConfigRequest() {
    }//For Json Serialization

    public QuoteProductConfigRequest(@Nullable Float bandwidth, @Nullable ProfileType profile,
                                     @Nullable ActionType action, @Nullable Long ipRange) {
        this.bandwidth = bandwidth;
        this.profile = profile;
        this.action = action;
        this.ipRange = ipRange;
    }

    public QuoteProductConfigRequest(@Nullable Float bandwidth,
                                     @Nullable ActionType action,
                                     @Nullable NetworkType accessSupplier, @Nullable SlaType sla) {
        this.bandwidth = bandwidth;
        this.action = action;
        this.accessSupplier = accessSupplier;
        this.sla = sla;
    }

    public QuoteProductConfigRequest(@Nullable ActionType action) {
        this.action = action;
    }

    @Nullable
    public Float getBandwidth() {
        return bandwidth;
    }

    @Nullable
    public Long getIpRange() {
        return ipRange;
    }

    @Nullable
    public ProfileType getProfile() {
        return profile;
    }

    @Nullable
    public ActionType getAction() {
        return action;
    }

    @Nullable
    public NetworkType getAccessSupplier() {
        return accessSupplier;
    }

    @Nullable
    public SlaType getSla() {
        return sla;
    }

    @Override
    public String toString() {
        return "QuoteProductConfigRequest{" +
                "bandwidth='" + bandwidth + '\'' +
                ", ipRange='" + ipRange + '\'' +
                ", profile='" + profile + '\'' +
                ", action='" + action + '\'' +
                ", accessSupplier='" + accessSupplier + '\'' +
                ", sla='" + sla + '\'' +
                '}';
    }
}
