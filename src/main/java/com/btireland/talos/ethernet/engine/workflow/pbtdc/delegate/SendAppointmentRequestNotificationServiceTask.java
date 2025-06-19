package com.btireland.talos.ethernet.engine.workflow.pbtdc.delegate;

import com.btireland.talos.ethernet.engine.service.PBTDCOrderWorkflowService;
import com.btireland.talos.ethernet.engine.workflow.ServiceTask;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessVariables;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SendAppointmentRequestNotificationServiceTask extends ServiceTask<PbtdcOrderProcessVariables> implements JavaDelegate {

    private PBTDCOrderWorkflowService pbtdcOrderWorkflowService;

    public SendAppointmentRequestNotificationServiceTask(PBTDCOrderWorkflowService pbtdcOrderWorkflowService){
            this.pbtdcOrderWorkflowService = pbtdcOrderWorkflowService;
        }

    @Override
    public void executeTask(long orderId, PbtdcOrderProcessVariables processVariables) throws Exception {
        pbtdcOrderWorkflowService.sendAppointmentRequestNotification(orderId);
    }

}
