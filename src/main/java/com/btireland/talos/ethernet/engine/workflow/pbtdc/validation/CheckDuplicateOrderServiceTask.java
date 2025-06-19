package com.btireland.talos.ethernet.engine.workflow.pbtdc.validation;

import com.btireland.talos.ethernet.engine.exception.ValidationException;
import com.btireland.talos.ethernet.engine.service.OrderValidationService;
import com.btireland.talos.ethernet.engine.workflow.ServiceTask;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessConstants;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessVariables;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CheckDuplicateOrderServiceTask extends ServiceTask<PbtdcOrderProcessVariables> implements JavaDelegate {

    OrderValidationService orderValidationService;

    public CheckDuplicateOrderServiceTask(OrderValidationService orderValidationService) {
        this.orderValidationService = orderValidationService;
    }

    @Override
    public void executeTask(long orderId, PbtdcOrderProcessVariables processVariables){
        try {
            orderValidationService.checkDuplicateOrder(orderId);
            processVariables.setWsiptProduct();

        } catch (ValidationException e) {
            throw new BpmnError(PbtdcOrderProcessConstants.Error.DUPLICATE_ORDER_FOUND.name(), e);
        }
    }
}
