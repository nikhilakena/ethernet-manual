package com.btireland.talos.ethernet.engine.workflow.pbtdc.provi;

import com.btireland.talos.ethernet.engine.service.PbtdcNotificationProcessorService;
import com.btireland.talos.ethernet.engine.workflow.ServiceTask;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessConstants;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessVariables;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component(value = "processNotesNotificationServiceTask")
public class NotesNotificationProcessorServiceTask extends ServiceTask<PbtdcOrderProcessVariables> implements JavaDelegate {

    private PbtdcNotificationProcessorService pbtdcNotificationProcessorService;

    public NotesNotificationProcessorServiceTask(PbtdcNotificationProcessorService pbtdcNotificationProcessorService) {
        this.pbtdcNotificationProcessorService = pbtdcNotificationProcessorService;
    }

    @Override
    public void executeTask(long orderId, PbtdcOrderProcessVariables processVariables) throws Exception {

        Map<String, Boolean> resultMap = pbtdcNotificationProcessorService.processNotesNotification(orderId, processVariables.getParkedNotificationId());
        processVariables.setDeliveryOnTrackChanged(resultMap.get(PbtdcOrderProcessConstants.VAR_NAME_DELIVERY_ON_TRACK_CHANGED));
        processVariables.setNotesTextEmpty(resultMap.get(PbtdcOrderProcessConstants.VAR_NAME_NOTES_TEXT_EMPTY));
    }

}
