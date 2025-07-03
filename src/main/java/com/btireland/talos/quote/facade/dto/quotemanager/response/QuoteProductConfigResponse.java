package com.btireland.talos.quote.facade.dto.quotemanager.response;


import com.btireland.talos.quote.facade.base.enumeration.internal.ActionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.SlaType;
import com.btireland.talos.quote.facade.base.enumeration.internal.NetworkType;
import com.btireland.talos.quote.facade.base.enumeration.internal.ProfileType;

import javax.annotation.Nullable;

public class QuoteProductConfigResponse {
    private Float bandwidth;
    private Long ipRange;
    private ProfileType profile;
    private ActionType action;
    private NetworkType accessSupplier;
    private NetworkType targetAccessSupplier;
    private SlaType sla;
    private NetworkType networkStatus;
    private String ceSwitch;

    private QuoteProductConfigResponse() {
    }//For Json Deserialization

    public QuoteProductConfigResponse(@Nullable Float bandwidth, @Nullable Long ipRange, @Nullable ProfileType profile,
                                      @Nullable ActionType action,
                                      @Nullable NetworkType accessSupplier, @Nullable NetworkType targetAccessSupplier,
                                      @Nullable SlaType sla, @Nullable NetworkType networkStatus, @Nullable String ceSwitch) {
        this.bandwidth = bandwidth;
        this.ipRange = ipRange;
        this.profile = profile;
        this.action = action;
        this.accessSupplier = accessSupplier;
        this.targetAccessSupplier = targetAccessSupplier;
        this.sla = sla;
        this.networkStatus = networkStatus;
        this.ceSwitch = ceSwitch; // Initialize CE Switch

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
    public NetworkType getTargetAccessSupplier() {
        return targetAccessSupplier;
    }

    @Nullable
    public SlaType getSla() {
        return sla;
    }

    @Nullable
    public NetworkType getNetworkStatus() {
        return networkStatus;
    }

    @Nullable
    public String getCeSwitch() { // Getter for CE Switch
        return ceSwitch;
    }

    public void setCeSwitch(@Nullable String ceSwitch) { // Setter for CE Switch
        this.ceSwitch = ceSwitch;
    }


    @Override
    public String toString() {
        return "QuoteProductConfigResponse{" +
                "bandwidth='" + bandwidth + '\'' +
                ", ipRange='" + ipRange + '\'' +
                ", profile='" + profile + '\'' +
                ", action='" + action + '\'' +
                ", accessSupplier='" + accessSupplier + '\'' +
                ", sla='" + sla + '\'' +
                ", networkStatus ='" + '\'' +
                ", ceSwitch='" + ceSwitch + '\'' + // Include CE Switch in toString
                '}';
    }
}
