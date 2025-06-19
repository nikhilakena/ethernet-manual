package com.btireland.talos.quote.facade.dto.notcom;

public class NotificationRequest {

    private Long orderId;

    private Long talosOrderId;

    private String oao;

    private String obo;

    private String dataContract;

    private String originatorCode;

    private String resellerTransactionId;

    private String resellerOrderRequestDateTime;

    private String operatorName;

    private String operatorCode;

    private String orderNumber;

    private String createdAt;

    private String serviceType;

    private String serviceClass;

    private String connectionType;

    private String mode;

    private String lastNotificationType;

    private String projectKey;

    private String delayReason;

    private RejectionDetails rejectionDetails;

    private QuoteOrder qbtdc;

    public NotificationRequest() {
    }


    public static final class NotificationRequestBuilder {
        private Long orderId;
        private Long talosOrderId;
        private String oao;
        private String obo;
        private String dataContract;
        private String originatorCode;
        private String resellerTransactionId;
        private String resellerOrderRequestDateTime;
        private String operatorName;
        private String operatorCode;
        private String orderNumber;
        private String createdAt;
        private String serviceType;
        private String serviceClass;
        private String connectionType;
        private String mode;
        private String lastNotificationType;
        private String projectKey;
        private String delayReason;
        private RejectionDetails rejectionDetails;
        private QuoteOrder qbtdc;

        private NotificationRequestBuilder() {
        }

        public static NotificationRequestBuilder notificationRequestBuilder() {
            return new NotificationRequestBuilder();
        }

        public NotificationRequestBuilder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public NotificationRequestBuilder talosOrderId(Long talosOrderId) {
            this.talosOrderId = talosOrderId;
            return this;
        }

        public NotificationRequestBuilder oao(String oao) {
            this.oao = oao;
            return this;
        }

        public NotificationRequestBuilder obo(String obo) {
            this.obo = obo;
            return this;
        }

        public NotificationRequestBuilder dataContract(String dataContract) {
            this.dataContract = dataContract;
            return this;
        }

        public NotificationRequestBuilder originatorCode(String originatorCode) {
            this.originatorCode = originatorCode;
            return this;
        }

        public NotificationRequestBuilder resellerTransactionId(String resellerTransactionId) {
            this.resellerTransactionId = resellerTransactionId;
            return this;
        }

        public NotificationRequestBuilder resellerOrderRequestDateTime(String resellerOrderRequestDateTime) {
            this.resellerOrderRequestDateTime = resellerOrderRequestDateTime;
            return this;
        }

        public NotificationRequestBuilder operatorName(String operatorName) {
            this.operatorName = operatorName;
            return this;
        }

        public NotificationRequestBuilder operatorCode(String operatorCode) {
            this.operatorCode = operatorCode;
            return this;
        }

        public NotificationRequestBuilder orderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
            return this;
        }

        public NotificationRequestBuilder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public NotificationRequestBuilder serviceType(String serviceType) {
            this.serviceType = serviceType;
            return this;
        }

        public NotificationRequestBuilder serviceClass(String serviceClass) {
            this.serviceClass = serviceClass;
            return this;
        }

        public NotificationRequestBuilder connectionType(String connectionType) {
            this.connectionType = connectionType;
            return this;
        }

        public NotificationRequestBuilder mode(String mode) {
            this.mode = mode;
            return this;
        }

        public NotificationRequestBuilder lastNotificationType(String lastNotificationType) {
            this.lastNotificationType = lastNotificationType;
            return this;
        }

        public NotificationRequestBuilder projectKey(String projectKey) {
            this.projectKey = projectKey;
            return this;
        }

        public NotificationRequestBuilder delayReason(String delayReason) {
            this.delayReason = delayReason;
            return this;
        }

        public NotificationRequestBuilder rejectionDetails(RejectionDetails rejectionDetails) {
            this.rejectionDetails = rejectionDetails;
            return this;
        }

        public NotificationRequestBuilder qbtdc(QuoteOrder qbtdc) {
            this.qbtdc = qbtdc;
            return this;
        }

        public NotificationRequest build() {
            NotificationRequest notificationRequest = new NotificationRequest();
            notificationRequest.orderId = this.orderId;
            notificationRequest.projectKey = this.projectKey;
            notificationRequest.resellerTransactionId = this.resellerTransactionId;
            notificationRequest.serviceClass = this.serviceClass;
            notificationRequest.connectionType = this.connectionType;
            notificationRequest.talosOrderId = this.talosOrderId;
            notificationRequest.qbtdc = this.qbtdc;
            notificationRequest.operatorName = this.operatorName;
            notificationRequest.serviceType = this.serviceType;
            notificationRequest.createdAt = this.createdAt;
            notificationRequest.oao = this.oao;
            notificationRequest.originatorCode = this.originatorCode;
            notificationRequest.obo = this.obo;
            notificationRequest.mode = this.mode;
            notificationRequest.resellerOrderRequestDateTime = this.resellerOrderRequestDateTime;
            notificationRequest.dataContract = this.dataContract;
            notificationRequest.orderNumber = this.orderNumber;
            notificationRequest.rejectionDetails = this.rejectionDetails;
            notificationRequest.operatorCode = this.operatorCode;
            notificationRequest.lastNotificationType = this.lastNotificationType;
            notificationRequest.delayReason = this.delayReason;
            return notificationRequest;
        }
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getTalosOrderId() {
        return talosOrderId;
    }

    public String getOao() {
        return oao;
    }

    public String getObo() {
        return obo;
    }

    public String getDataContract() {
        return dataContract;
    }

    public String getOriginatorCode() {
        return originatorCode;
    }

    public String getResellerTransactionId() {
        return resellerTransactionId;
    }

    public String getResellerOrderRequestDateTime() {
        return resellerOrderRequestDateTime;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public String getMode() {
        return mode;
    }

    public String getLastNotificationType() {
        return lastNotificationType;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public String getDelayReason() {
        return delayReason;
    }

    public RejectionDetails getRejectionDetails() {
        return rejectionDetails;
    }

    public QuoteOrder getQbtdc() {
        return qbtdc;
    }

    @Override
    public String toString() {
        return "NotificationRequest{" +
                "orderId=" + orderId +
                ", talosOrderId=" + talosOrderId +
                ", oao='" + oao + '\'' +
                ", obo='" + obo + '\'' +
                ", dataContract='" + dataContract + '\'' +
                ", originatorCode='" + originatorCode + '\'' +
                ", resellerTransactionId='" + resellerTransactionId + '\'' +
                ", resellerOrderRequestDateTime='" + resellerOrderRequestDateTime + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", operatorCode='" + operatorCode + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", serviceClass='" + serviceClass + '\'' +
                ", connectionType='" + connectionType + '\'' +
                ", mode='" + mode + '\'' +
                ", lastNotificationType='" + lastNotificationType + '\'' +
                ", projectKey='" + projectKey + '\'' +
                ", delayReason='" + delayReason + '\'' +
                ", rejectionDetails=" + rejectionDetails +
                ", qbtdc=" + qbtdc +
                '}';
    }
}
