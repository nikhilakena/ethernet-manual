package com.btireland.talos.ethernet.engine.workflow.pbtdc.provi;

import com.btireland.talos.ethernet.engine.service.PbtdcNotificationProcessorService;
import com.btireland.talos.ethernet.engine.workflow.ServiceTask;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessVariables;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component(value = "processCompleteNotificationServiceTask")
public class CompleteNotificationProcessorServiceTask extends ServiceTask<PbtdcOrderProcessVariables> implements JavaDelegate {

    private PbtdcNotificationProcessorService pbtdcNotificationProcessorService;

    public CompleteNotificationProcessorServiceTask(PbtdcNotificationProcessorService pbtdcNotificationProcessorService) {
        this.pbtdcNotificationProcessorService = pbtdcNotificationProcessorService;
    }

    @Override
    public void executeTask(long orderId, PbtdcOrderProcessVariables processVariables) throws Exception {

        pbtdcNotificationProcessorService.processCompleteNotification(orderId, processVariables.getParkedNotificationId());
        processVariables.setWsiptProduct(false);
    }

}
