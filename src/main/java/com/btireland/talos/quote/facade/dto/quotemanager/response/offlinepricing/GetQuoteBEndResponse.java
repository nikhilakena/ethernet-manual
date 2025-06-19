package com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing;

import com.btireland.talos.quote.facade.base.enumeration.internal.ActionType;
import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;

public class GetQuoteBEndResponse {

    private String handOverNode;

    private ActionType action;

    public GetQuoteBEndResponse() {
        //For json serialization
    }

    public GetQuoteBEndResponse(@NotBlank String handOverNode) {
        this.handOverNode = handOverNode;
    }

    public GetQuoteBEndResponse(@NotBlank String handOverNode, @Nullable ActionType action) {
        this.handOverNode = handOverNode;
        this.action = action;
    }

    @NotBlank
    public String getHandOverNode() {
        return handOverNode;
    }

    @Nullable
    public ActionType getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "GetQuoteBEndResponse{" +
            "handOverNode='" + handOverNode + '\'' +
            ", action=" + action +
            '}';
    }
}
