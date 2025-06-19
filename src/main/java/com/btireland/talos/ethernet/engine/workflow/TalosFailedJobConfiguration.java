package com.btireland.talos.ethernet.engine.workflow;

import org.camunda.bpm.engine.impl.bpmn.parser.DefaultFailedJobParseListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.jobexecutor.FailedJobCommandFactory;
import org.camunda.bpm.engine.spring.SpringProcessEnginePlugin;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaFailedJobConfiguration;

import java.util.ArrayList;

public class TalosFailedJobConfiguration extends SpringProcessEnginePlugin implements CamundaFailedJobConfiguration {

    private FailedJobCommandFactory failedJobCommandFactory;

    public TalosFailedJobConfiguration(FailedJobCommandFactory failedJobCommandFactory) {
        this.failedJobCommandFactory = failedJobCommandFactory;
    }

    @Override
    public void preInit(ProcessEngineConfigurationImpl configuration) {

        if (configuration.getCustomPostBPMNParseListeners() == null) {
            configuration.setCustomPostBPMNParseListeners(new ArrayList<>());
        }

        configuration.getCustomPostBPMNParseListeners().add(new DefaultFailedJobParseListener());
        configuration.setFailedJobCommandFactory(failedJobCommandFactory);
    }
}
