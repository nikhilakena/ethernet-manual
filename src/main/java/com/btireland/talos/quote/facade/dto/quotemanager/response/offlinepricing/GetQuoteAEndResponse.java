package com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing;

import com.btireland.talos.quote.facade.base.enumeration.internal.ActionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.NetworkType;
import com.btireland.talos.quote.facade.base.enumeration.internal.SlaType;
import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class GetQuoteAEndResponse {

    private String eircode;

    private ActionType action;

    private Float bandwidth;

    private SlaType sla;

    private NetworkType accessSupplier;

    private NetworkType targetAccessSupplier;

    private String fullAddress;

    private NetworkType networkStatus;

    private String multiEircode;

    public GetQuoteAEndResponse() {
        //For json serialization
    }

    public GetQuoteAEndResponse(@NotBlank String eircode, @NotNull ActionType action, @Nullable Float bandwidth,
                                @Nullable SlaType sla,
                                @Nullable NetworkType accessSupplier) {
        this.eircode = eircode;
        this.action = action;
        this.bandwidth = bandwidth;
        this.sla = sla;
        this.accessSupplier = accessSupplier;
    }

    public GetQuoteAEndResponse(@NotBlank String eircode, @NotNull ActionType action, @Nullable Float bandwidth,
        @Nullable SlaType sla, @Nullable NetworkType accessSupplier, @Nullable NetworkType targetAccessSupplier,
        @Nullable String fullAddress, @Nullable NetworkType networkStatus, @Nullable String multiEircode) {
        this.eircode = eircode;
        this.action = action;
        this.bandwidth = bandwidth;
        this.sla = sla;
        this.accessSupplier = accessSupplier;
        this.targetAccessSupplier = targetAccessSupplier;
        this.fullAddress = fullAddress;
        this.networkStatus = networkStatus;
        this.multiEircode = multiEircode;
    }

    @NotBlank
    public String getEircode() {
        return eircode;
    }

    @NotNull
    public ActionType getAction() {
        return action;
    }

    @Nullable
    public Float getBandwidth() {
        return bandwidth;
    }

    @Nullable
    public SlaType getSla() {
        return sla;
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
    public String getFullAddress() {
        return fullAddress;
    }

    @Nullable
    public NetworkType getNetworkStatus() {
        return networkStatus;
    }

    @Nullable
    public String getMultiEircode() {
        return multiEircode;
    }

    @Override
    public String toString() {
        return "GetQuoteAEndResponse{" +
            "eircode='" + eircode + '\'' +
            ", action=" + action +
            ", bandwidth=" + bandwidth +
            ", sla=" + sla +
            ", accessSupplier=" + accessSupplier +
            ", targetAccessSupplier=" + targetAccessSupplier +
            '}';
    }
}
