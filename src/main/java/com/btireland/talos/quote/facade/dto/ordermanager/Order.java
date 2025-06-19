package com.btireland.talos.quote.facade.dto.ordermanager;

public class Order {

    private Long orderId;

    private String changedAt;

    private String createdAt;

    private String oao;

    private String obo;

    private String operatorName;

    private String operatorCode;

    private String dataContractName;

    private String originatorCode;

    private String transactionId;

    private String orderRequestDateTime;

    private String orderNumber;

    private String orderServiceType;

    private String orderStatus;

    private String workflowStatus;

    private String projectKey;

    public Order() {
    }

    public static final class OrderBuilder {
        private Long orderId;
        private String changedAt;
        private String createdAt;
        private String oao;
        private String obo;
        private String operatorName;
        private String operatorCode;
        private String dataContractName;
        private String originatorCode;
        private String transactionId;
        private String orderRequestDateTime;
        private String orderNumber;
        private String orderServiceType;
        private String orderStatus;
        private String workflowStatus;
        private String projectKey;

        private OrderBuilder() {
        }

        public static OrderBuilder orderBuilder() {
            return new OrderBuilder();
        }

        public OrderBuilder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderBuilder changedAt(String changedAt) {
            this.changedAt = changedAt;
            return this;
        }

        public OrderBuilder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public OrderBuilder oao(String oao) {
            this.oao = oao;
            return this;
        }

        public OrderBuilder obo(String obo) {
            this.obo = obo;
            return this;
        }

        public OrderBuilder operatorName(String operatorName) {
            this.operatorName = operatorName;
            return this;
        }

        public OrderBuilder operatorCode(String operatorCode) {
            this.operatorCode = operatorCode;
            return this;
        }

        public OrderBuilder dataContractName(String dataContractName) {
            this.dataContractName = dataContractName;
            return this;
        }

        public OrderBuilder originatorCode(String originatorCode) {
            this.originatorCode = originatorCode;
            return this;
        }

        public OrderBuilder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public OrderBuilder orderRequestDateTime(String orderRequestDateTime) {
            this.orderRequestDateTime = orderRequestDateTime;
            return this;
        }

        public OrderBuilder orderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
            return this;
        }

        public OrderBuilder orderServiceType(String orderServiceType) {
            this.orderServiceType = orderServiceType;
            return this;
        }

        public OrderBuilder orderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public OrderBuilder workflowStatus(String workflowStatus) {
            this.workflowStatus = workflowStatus;
            return this;
        }

        public OrderBuilder projectKey(String projectKey) {
            this.projectKey = projectKey;
            return this;
        }

        public Order build() {
            Order order = new Order();
            order.projectKey = this.projectKey;
            order.transactionId = this.transactionId;
            order.obo = this.obo;
            order.orderStatus = this.orderStatus;
            order.orderRequestDateTime = this.orderRequestDateTime;
            order.oao = this.oao;
            order.originatorCode = this.originatorCode;
            order.orderServiceType = this.orderServiceType;
            order.changedAt = this.changedAt;
            order.operatorName = this.operatorName;
            order.orderNumber = this.orderNumber;
            order.workflowStatus = this.workflowStatus;
            order.createdAt = this.createdAt;
            order.orderId = this.orderId;
            order.operatorCode = this.operatorCode;
            order.dataContractName = this.dataContractName;
            return order;
        }
    }

    public String getChangedAt() {
        return changedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getOao() {
        return oao;
    }

    public String getObo() {
        return obo;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public String getDataContractName() {
        return dataContractName;
    }

    public String getOriginatorCode() {
        return originatorCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getOrderRequestDateTime() {
        return orderRequestDateTime;
    }

    public String getOrderServiceType() {
        return orderServiceType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getWorkflowStatus() {
        return workflowStatus;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", changedAt='" + changedAt + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", oao='" + oao + '\'' +
                ", obo='" + obo + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", operatorCode='" + operatorCode + '\'' +
                ", dataContractName='" + dataContractName + '\'' +
                ", originatorCode='" + originatorCode + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", orderRequestDateTime='" + orderRequestDateTime + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", orderServiceType='" + orderServiceType + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", workflowStatus='" + workflowStatus + '\'' +
                ", projectKey='" + projectKey + '\'' +
                '}';
    }
}
