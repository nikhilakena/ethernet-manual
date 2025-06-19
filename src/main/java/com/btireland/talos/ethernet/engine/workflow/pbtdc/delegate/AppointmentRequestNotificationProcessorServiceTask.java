package com.btireland.talos.ethernet.engine.workflow.pbtdc.delegate;

import com.btireland.talos.ethernet.engine.service.PbtdcNotificationProcessorService;
import com.btireland.talos.ethernet.engine.workflow.ServiceTask;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessVariables;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component(value = "processAppointmentRequestNotificationServiceTask")
public class AppointmentRequestNotificationProcessorServiceTask extends ServiceTask<PbtdcOrderProcessVariables> implements JavaDelegate {

    private PbtdcNotificationProcessorService pbtdcNotificationProcessorService;

    public AppointmentRequestNotificationProcessorServiceTask(PbtdcNotificationProcessorService pbtdcNotificationProcessorService) {
        this.pbtdcNotificationProcessorService = pbtdcNotificationProcessorService;
    }

    @Override
    public void executeTask(long orderId, PbtdcOrderProcessVariables processVariables) throws Exception {

        pbtdcNotificationProcessorService.processAppointmentRequest(orderId, processVariables.getParkedNotificationId());
    }

}
