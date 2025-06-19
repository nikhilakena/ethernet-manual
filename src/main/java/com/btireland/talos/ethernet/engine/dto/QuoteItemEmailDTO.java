package com.btireland.talos.ethernet.engine.dto;

public class QuoteItemEmailDTO {

    private Long quoteId;
    private String status;
    private String serviceClass;
    private String connectionType;
    private String ipRange;
    private String term;
    private String aEndEircode;
    private String aEndAddress;
    private String bandwidth;
    private String sla;
    private String aEndTargetAccessSupplier;
    private String handoff;
    private String logicalLinkBandwidth;
    private String logicalLinkProfile;
    private String nonRecurringPrice;
    private String recurringPrice;
    private String recurringFrequency;
    private String reason;
    private String group;

    public QuoteItemEmailDTO() {

    }

    public Long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getIpRange() { return ipRange; }

    public void setIpRange(String ipRange) { this.ipRange = ipRange; }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getAEndEircode() {
        return aEndEircode;
    }

    public void setAEndEircode(String aEndEircode) {
        this.aEndEircode = aEndEircode;
    }

    public String getAEndAddress() {
        return aEndAddress;
    }

    public void setAEndAddress(String aEndAddress) {
        this.aEndAddress = aEndAddress;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getSla() {
        return sla;
    }

    public void setSla(String sla) {
        this.sla = sla;
    }

    public String getAEndTargetAccessSupplier() {
        return aEndTargetAccessSupplier;
    }

    public void setAEndTargetAccessSupplier(String aEndTargetAccessSupplier) {
        this.aEndTargetAccessSupplier = aEndTargetAccessSupplier;
    }

    public String getHandoff() {
        return handoff;
    }

    public void setHandoff(String handoff) {
        this.handoff = handoff;
    }

    public String getLogicalLinkBandwidth() {
        return logicalLinkBandwidth;
    }

    public void setLogicalLinkBandwidth(String logicalLinkBandwidth) {
        this.logicalLinkBandwidth = logicalLinkBandwidth;
    }

    public String getLogicalLinkProfile() {
        return logicalLinkProfile;
    }

    public void setLogicalLinkProfile(String logicalLinkProfile) {
        this.logicalLinkProfile = logicalLinkProfile;
    }

    public String getNonRecurringPrice() {
        return nonRecurringPrice;
    }

    public void setNonRecurringPrice(String nonRecurringPrice) {
        this.nonRecurringPrice = nonRecurringPrice;
    }

    public String getRecurringPrice() {
        return recurringPrice;
    }

    public void setRecurringPrice(String recurringPrice) {
        this.recurringPrice = recurringPrice;
    }

    public String getRecurringFrequency() {
        return recurringFrequency;
    }

    public void setRecurringFrequency(String recurringFrequency) {
        this.recurringFrequency = recurringFrequency;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    private QuoteItemEmailDTO(Builder builder) {
        quoteId = builder.quoteId;
        status = builder.status;
        serviceClass = builder.serviceClass;
        connectionType = builder.connectionType;
        ipRange = builder.ipRange;
        term = builder.term;
        aEndEircode = builder.aEndEircode;
        aEndAddress = builder.aEndAddress;
        bandwidth = builder.bandwidth;
        sla = builder.sla;
        aEndTargetAccessSupplier = builder.aEndTargetAccessSupplier;
        handoff = builder.handoff;
        logicalLinkBandwidth = builder.logicalLinkBandwidth;
        logicalLinkProfile = builder.logicalLinkProfile;
        nonRecurringPrice = builder.nonRecurringPrice;
        recurringPrice = builder.recurringPrice;
        recurringFrequency = builder.recurringFrequency;
        reason = builder.reason;
        group = builder.group;
    }

    public static final class Builder {

        private Long quoteId;
        private String status;
        private String serviceClass;

        private String connectionType;
        private String ipRange;
        private String term;
        private String aEndEircode;
        private String aEndAddress;
        private String bandwidth;
        private String sla;
        private String aEndTargetAccessSupplier;
        private String handoff;
        private String logicalLinkBandwidth;
        private String logicalLinkProfile;
        private String nonRecurringPrice;
        private String recurringPrice;
        private String recurringFrequency;
        private String reason;
        private String group;

        private Builder() {
        }

        public static Builder newQuoteItemEmailDTOBuilder() {
            return new Builder();
        }

        public Builder withQuoteId(Long val) {
            quoteId = val;
            return this;
        }

        public Builder withStatus(String val) {
            status = val;
            return this;
        }

        public Builder withServiceClass(String val) {
            serviceClass = val;
            return this;
        }

        public Builder withConnectionType(String val) {
            connectionType = val;
            return this;
        }

        public Builder withIpRange(String val) {
            ipRange = val;
            return this;
        }

        public Builder withTerm(String val) {
            term = val;
            return this;
        }

        public Builder withAEndEircode(String val) {
            aEndEircode = val;
            return this;
        }

        public Builder withAEndAddress(String val) {
            aEndAddress = val;
            return this;
        }

        public Builder withBandwidth(String val) {
            bandwidth = val;
            return this;
        }

        public Builder withSla(String val) {
            sla = val;
            return this;
        }

        public Builder withAEndTargetAccessSupplier(String val) {
            aEndTargetAccessSupplier = val;
            return this;
        }

        public Builder withHandoff(String val) {
            handoff = val;
            return this;
        }

        public Builder withLogicalLinkBandwidth(String val) {
            logicalLinkBandwidth = val;
            return this;
        }

        public Builder withLogicalLinkProfile(String val) {
            logicalLinkProfile = val;
            return this;
        }

        public Builder withNonRecurringPrice(String val) {
            nonRecurringPrice = val;
            return this;
        }

        public Builder withRecurringPrice(String val) {
            recurringPrice = val;
            return this;
        }

        public Builder withRecurringFrequency(String val) {
            recurringFrequency = val;
            return this;
        }

        public Builder withReason(String val) {
            reason = val;
            return this;
        }

        public Builder withGroup(String val) {
            group = val;
            return this;
        }

        public QuoteItemEmailDTO build() {
            return new QuoteItemEmailDTO(this);
        }
    }

    @Override
    public String toString() {
        return "QuoteItemEmailDTO{" +
            "quoteId=" + quoteId +
            ", status='" + status + '\'' +
            ", serviceClass='" + serviceClass + '\'' +
            ", connectionType='" + connectionType + '\'' +
            ", ipRange='" + ipRange + '\'' +
            ", term='" + term + '\'' +
            ", aEndEircode='" + aEndEircode + '\'' +
            ", aEndAddress='" + aEndAddress + '\'' +
            ", bandwidth='" + bandwidth + '\'' +
            ", sla='" + sla + '\'' +
            ", aEndTargetAccessSupplier='" + aEndTargetAccessSupplier + '\'' +
            ", handoff='" + handoff + '\'' +
            ", logicalLinkBandwidth='" + logicalLinkBandwidth + '\'' +
            ", logicalLinkProfile='" + logicalLinkProfile + '\'' +
            ", nonRecurringPrice='" + nonRecurringPrice + '\'' +
            ", recurringPrice='" + recurringPrice + '\'' +
            ", recurringFrequency='" + recurringFrequency + '\'' +
            ", reason='" + reason + '\'' +
            ", group='" + group + '\'' +
            '}';
    }
}
