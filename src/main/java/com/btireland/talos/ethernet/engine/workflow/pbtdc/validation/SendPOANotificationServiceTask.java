package com.btireland.talos.ethernet.engine.workflow.pbtdc.validation;

import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceBadRequestException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceNotFoundException;
import com.btireland.talos.ethernet.engine.service.PBTDCOrderWorkflowService;
import com.btireland.talos.ethernet.engine.workflow.ServiceTask;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessVariables;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SendPOANotificationServiceTask extends ServiceTask<PbtdcOrderProcessVariables> implements JavaDelegate {

    private PBTDCOrderWorkflowService pbtdcOrderWorkflowService;

    public SendPOANotificationServiceTask(PBTDCOrderWorkflowService pbtdcOrderWorkflowService) {
        this.pbtdcOrderWorkflowService = pbtdcOrderWorkflowService;
    }

    @Override
    public void executeTask(long orderId, PbtdcOrderProcessVariables processVariables) throws Exception, OrderManagerServiceBadRequestException, OrderManagerServiceNotFoundException {
        pbtdcOrderWorkflowService.processPOANotification(orderId);
    }

}
