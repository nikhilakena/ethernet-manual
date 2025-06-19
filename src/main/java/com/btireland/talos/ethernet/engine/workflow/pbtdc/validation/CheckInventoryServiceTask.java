package com.btireland.talos.ethernet.engine.workflow.pbtdc.validation;

import com.btireland.talos.ethernet.engine.workflow.ServiceTask;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessVariables;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CheckInventoryServiceTask extends ServiceTask<PbtdcOrderProcessVariables> implements JavaDelegate {

    @Override
    public void executeTask(long orderId, PbtdcOrderProcessVariables processVariables) throws Exception {
        log.info("TODO: code check inventory");
    }
}
