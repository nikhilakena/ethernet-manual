package com.btireland.talos.quote.facade.dto.ordermanager;

public class Quote {

    private Long id;

    private String oao;

    private String group;

    private String product;

    private String connectionType;

    private Integer term;

    private Integer ipRange;

    private String logicalActionFlag;

    private String logicalBandwidth;

    private String logicalProfile;

    private String aendNetworkStatus;

    private String aendMultiEircode;

    private String aendAddress;

    private String aendEircode;

    private String aendActionFlag;

    private String aendTargetAccessSupplier;

    private String aendReqAccessSupplier;

    private String aendBandwidth;

    private String aendSla;

    private String aendCeSwitch;

    private String bendHandOverNode;

    private String bendActionFlag;

    public Quote() {
    }

    public static final class QuoteBuilder {
        private String oao;
        private String group;
        private String product;
        private String connectionType;
        private Integer term;
        private Integer ipRange;
        private String logicalActionFlag;
        private String logicalBandwidth;
        private String logicalProfile;
        private String aendNetworkStatus;
        private String aendMultiEircode;
        private String aendAddress;
        private String aendEircode;
        private String aendActionFlag;
        private String aendTargetAccessSupplier;
        private String aendReqAccessSupplier;
        private String aendBandwidth;
        private String aendSla;
        private String aendCeSwitch;
        private String bendHandOverNode;
        private String bendActionFlag;

        private QuoteBuilder() {
        }

        public static QuoteBuilder quoteBuilder() {
            return new QuoteBuilder();
        }

        public QuoteBuilder oao(String oao) {
            this.oao = oao;
            return this;
        }

        public QuoteBuilder group(String group) {
            this.group = group;
            return this;
        }

        public QuoteBuilder product(String product) {
            this.product = product;
            return this;
        }

        public QuoteBuilder connectionType(String connectionType) {
            this.connectionType = connectionType;
            return this;
        }

        public QuoteBuilder term(Integer term) {
            this.term = term;
            return this;
        }

        public QuoteBuilder ipRange(Integer ipRange) {
            this.ipRange = ipRange;
            return this;
        }

        public QuoteBuilder logicalActionFlag(String logicalActionFlag) {
            this.logicalActionFlag = logicalActionFlag;
            return this;
        }

        public QuoteBuilder logicalBandwidth(String logicalBandwidth) {
            this.logicalBandwidth = logicalBandwidth;
            return this;
        }

        public QuoteBuilder logicalProfile(String logicalProfile) {
            this.logicalProfile = logicalProfile;
            return this;
        }

        public QuoteBuilder aendNetworkStatus(String aendNetworkStatus) {
            this.aendNetworkStatus = aendNetworkStatus;
            return this;
        }

        public QuoteBuilder aendMultiEircode(String aendMultiEircode) {
            this.aendMultiEircode = aendMultiEircode;
            return this;
        }

        public QuoteBuilder aendAddress(String aendAddress) {
            this.aendAddress = aendAddress;
            return this;
        }

        public QuoteBuilder aendEircode(String aendEircode) {
            this.aendEircode = aendEircode;
            return this;
        }

        public QuoteBuilder aendActionFlag(String aendActionFlag) {
            this.aendActionFlag = aendActionFlag;
            return this;
        }

        public QuoteBuilder aendTargetAccessSupplier(String aendTargetAccessSupplier) {
            this.aendTargetAccessSupplier = aendTargetAccessSupplier;
            return this;
        }

        public QuoteBuilder aendReqAccessSupplier(String aendReqAccessSupplier) {
            this.aendReqAccessSupplier = aendReqAccessSupplier;
            return this;
        }

        public QuoteBuilder aendBandwidth(String aendBandwidth) {
            this.aendBandwidth = aendBandwidth;
            return this;
        }

        public QuoteBuilder aendSla(String aendSla) {
            this.aendSla = aendSla;
            return this;
        }

        public QuoteBuilder aendCeSwitch(String aendCeSwitch) {
            this.aendCeSwitch = aendCeSwitch;
            return this;
        }

        public QuoteBuilder bendHandOverNode(String bendHandOverNode) {
            this.bendHandOverNode = bendHandOverNode;
            return this;
        }

        public QuoteBuilder bendActionFlag(String bendActionFlag) {
            this.bendActionFlag = bendActionFlag;
            return this;
        }

        public Quote build() {
            Quote quote = new Quote();
            quote.group = this.group;
            quote.ipRange = this.ipRange;
            quote.aendAddress = this.aendAddress;
            quote.term = this.term;
            quote.oao = this.oao;
            quote.aendSla = this.aendSla;
            quote.logicalProfile = this.logicalProfile;
            quote.aendMultiEircode = this.aendMultiEircode;
            quote.aendEircode = this.aendEircode;
            quote.aendTargetAccessSupplier = this.aendTargetAccessSupplier;
            quote.logicalActionFlag = this.logicalActionFlag;
            quote.product = this.product;
            quote.connectionType = this.connectionType;
            quote.bendActionFlag = this.bendActionFlag;
            quote.aendReqAccessSupplier = this.aendReqAccessSupplier;
            quote.aendNetworkStatus = this.aendNetworkStatus;
            quote.aendActionFlag = this.aendActionFlag;
            quote.logicalBandwidth = this.logicalBandwidth;
            quote.bendHandOverNode = this.bendHandOverNode;
            quote.aendBandwidth = this.aendBandwidth;
            quote.aendCeSwitch=this.aendCeSwitch;
            return quote;
        }
    }

    public String getOao() {
        return oao;
    }

    public String getGroup() {
        return group;
    }

    public String getProduct() {
        return product;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public Integer getTerm() {
        return term;
    }

    public Integer getIpRange() {
        return ipRange;
    }

    public String getLogicalActionFlag() {
        return logicalActionFlag;
    }

    public String getLogicalBandwidth() {
        return logicalBandwidth;
    }

    public String getLogicalProfile() {
        return logicalProfile;
    }

    public String getAendNetworkStatus() {
        return aendNetworkStatus;
    }

    public String getAendMultiEircode() {
        return aendMultiEircode;
    }

    public String getAendAddress() {
        return aendAddress;
    }

    public String getAendEircode() {
        return aendEircode;
    }

    public String getAendActionFlag() {
        return aendActionFlag;
    }

    public String getAendTargetAccessSupplier() {
        return aendTargetAccessSupplier;
    }

    public String getAendReqAccessSupplier() {
        return aendReqAccessSupplier;
    }

    public String getAendBandwidth() {
        return aendBandwidth;
    }

    public String getAendSla() {
        return aendSla;
    }

    public String getBendHandOverNode() {
        return bendHandOverNode;
    }

    public String getBendActionFlag() {
        return bendActionFlag;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "oao='" + oao + '\'' +
                ", group='" + group + '\'' +
                ", product='" + product + '\'' +
                ", connectionType='" + connectionType + '\'' +
                ", term=" + term +
                ", ipRange=" + ipRange +
                ", logicalActionFlag='" + logicalActionFlag + '\'' +
                ", logicalBandwidth='" + logicalBandwidth + '\'' +
                ", logicalProfile='" + logicalProfile + '\'' +
                ", aendNetworkStatus='" + aendNetworkStatus + '\'' +
                ", aendMultiEircode='" + aendMultiEircode + '\'' +
                ", aendAddress='" + aendAddress + '\'' +
                ", aendEircode='" + aendEircode + '\'' +
                ", aendActionFlag='" + aendActionFlag + '\'' +
                ", aendTargetAccessSupplier='" + aendTargetAccessSupplier + '\'' +
                ", aendReqAccessSupplier='" + aendReqAccessSupplier + '\'' +
                ", aendBandwidth='" + aendBandwidth + '\'' +
                ", aendSla='" + aendSla + '\'' +
                ", bendHandOverNode='" + bendHandOverNode + '\'' +
                ", bendActionFlag='" + bendActionFlag + '\'' +
                '}';
    }
}
