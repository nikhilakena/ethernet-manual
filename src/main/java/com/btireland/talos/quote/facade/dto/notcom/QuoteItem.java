package com.btireland.talos.quote.facade.dto.notcom;

public class QuoteItem {

    private Long orderId;

    private Long notificationId;

    private Long quoteItemId;

    private String group;

    private Integer term;

    private String product;

    private String connectionType;

    private String nonRecurringPrice;

    private String recurringPrice;

    private String status;

    private String notes;

    private String offlineQuoted;

    private Access customerAccess;

    private Access wholesalerAccess;

    private LogicalLink logicalLink;

    private RejectionDetails rejectionDetails;

    private String etherflowRecurringPrice;

    private String etherwayRecurringPrice;

    public QuoteItem() {
    }


    public static final class QuoteItemBuilder {
        private Long orderId;
        private Long notificationId;
        private Long quoteItemId;
        private String group;
        private Integer term;
        private String product;
        private String connectionType;
        private String nonRecurringPrice;
        private String recurringPrice;
        private String status;
        private String notes;
        private String offlineQuoted;
        private Access customerAccess;
        private Access wholesalerAccess;
        private LogicalLink logicalLink;
        private RejectionDetails rejectionDetails;
        private String etherflowRecurringPrice;
        private String etherwayRecurringPrice;

        private QuoteItemBuilder() {
        }

        public static QuoteItemBuilder quoteItemBuilder() {
            return new QuoteItemBuilder();
        }

        public QuoteItemBuilder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public QuoteItemBuilder notificationId(Long notificationId) {
            this.notificationId = notificationId;
            return this;
        }

        public QuoteItemBuilder quoteItemId(Long quoteItemId) {
            this.quoteItemId = quoteItemId;
            return this;
        }

        public QuoteItemBuilder group(String group) {
            this.group = group;
            return this;
        }

        public QuoteItemBuilder term(Integer term) {
            this.term = term;
            return this;
        }

        public QuoteItemBuilder product(String product) {
            this.product = product;
            return this;
        }

        public QuoteItemBuilder connectionType(String connectionType) {
            this.connectionType = connectionType;
            return this;
        }

        public QuoteItemBuilder nonRecurringPrice(String nonRecurringPrice) {
            this.nonRecurringPrice = nonRecurringPrice;
            return this;
        }

        public QuoteItemBuilder recurringPrice(String recurringPrice) {
            this.recurringPrice = recurringPrice;
            return this;
        }

        public QuoteItemBuilder status(String status) {
            this.status = status;
            return this;
        }

        public QuoteItemBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public QuoteItemBuilder offlineQuoted(String offlineQuoted) {
            this.offlineQuoted = offlineQuoted;
            return this;
        }

        public QuoteItemBuilder customerAccess(Access customerAccess) {
            this.customerAccess = customerAccess;
            return this;
        }

        public QuoteItemBuilder wholesalerAccess(Access wholesalerAccess) {
            this.wholesalerAccess = wholesalerAccess;
            return this;
        }

        public QuoteItemBuilder logicalLink(LogicalLink logicalLink) {
            this.logicalLink = logicalLink;
            return this;
        }

        public QuoteItemBuilder rejectionDetails(RejectionDetails rejectionDetails) {
            this.rejectionDetails = rejectionDetails;
            return this;
        }

        public QuoteItemBuilder etherflowRecurringPrice(String etherflowRecurringPrice) {
            this.etherflowRecurringPrice = etherflowRecurringPrice;
            return this;
        }

        public QuoteItemBuilder etherwayRecurringPrice(String etherwayRecurringPrice) {
            this.etherwayRecurringPrice = etherwayRecurringPrice;
            return this;
        }

        public QuoteItem build() {
            QuoteItem quoteItem = new QuoteItem();
            quoteItem.group = this.group;
            quoteItem.etherwayRecurringPrice = this.etherwayRecurringPrice;
            quoteItem.orderId = this.orderId;
            quoteItem.nonRecurringPrice = this.nonRecurringPrice;
            quoteItem.wholesalerAccess = this.wholesalerAccess;
            quoteItem.quoteItemId = this.quoteItemId;
            quoteItem.product = this.product;
            quoteItem.connectionType = this.connectionType;
            quoteItem.customerAccess = this.customerAccess;
            quoteItem.notificationId = this.notificationId;
            quoteItem.recurringPrice = this.recurringPrice;
            quoteItem.logicalLink = this.logicalLink;
            quoteItem.rejectionDetails = this.rejectionDetails;
            quoteItem.etherflowRecurringPrice = this.etherflowRecurringPrice;
            quoteItem.term = this.term;
            quoteItem.status = this.status;
            quoteItem.notes = this.notes;
            quoteItem.offlineQuoted = this.offlineQuoted;
            return quoteItem;
        }
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public Long getQuoteItemId() {
        return quoteItemId;
    }

    public String getGroup() {
        return group;
    }

    public Integer getTerm() {
        return term;
    }

    public String getProduct() {
        return product;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public String getNonRecurringPrice() {
        return nonRecurringPrice;
    }

    public String getRecurringPrice() {
        return recurringPrice;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public String getOfflineQuoted() {
        return offlineQuoted;
    }

    public Access getCustomerAccess() {
        return customerAccess;
    }

    public Access getWholesalerAccess() {
        return wholesalerAccess;
    }

    public LogicalLink getLogicalLink() {
        return logicalLink;
    }

    public RejectionDetails getRejectionDetails() {
        return rejectionDetails;
    }

    public String getEtherflowRecurringPrice() {
        return etherflowRecurringPrice;
    }

    public String getEtherwayRecurringPrice() {
        return etherwayRecurringPrice;
    }

    @Override
    public String toString() {
        return "QuoteItem{" +
                "orderId=" + orderId +
                ", notificationId=" + notificationId +
                ", quoteItemId=" + quoteItemId +
                ", group='" + group + '\'' +
                ", term=" + term +
                ", product='" + product + '\'' +
                ", connectionType='" + connectionType + '\'' +
                ", nonRecurringPrice='" + nonRecurringPrice + '\'' +
                ", recurringPrice='" + recurringPrice + '\'' +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                ", offlineQuoted='" + offlineQuoted + '\'' +
                ", customerAccess=" + customerAccess +
                ", wholesalerAccess=" + wholesalerAccess +
                ", logicalLink=" + logicalLink +
                ", rejectionDetails=" + rejectionDetails +
                ", etherflowRecurringPrice='" + etherflowRecurringPrice + '\'' +
                ", etherwayRecurringPrice='" + etherwayRecurringPrice + '\'' +
                '}';
    }
}
