package com.btireland.talos.ethernet.engine.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;

public abstract class OrderProcessVariables {

    protected final DelegateExecution delegateExecution;

    public OrderProcessVariables(DelegateExecution delegateExecution) {
        this.delegateExecution = delegateExecution;
    }

    public Long getOrderId() {
        String orderId = delegateExecution.getProcessBusinessKey();

        if (orderId == null) {
            return null;
        }

        return Long.parseLong(orderId);
    }
}
