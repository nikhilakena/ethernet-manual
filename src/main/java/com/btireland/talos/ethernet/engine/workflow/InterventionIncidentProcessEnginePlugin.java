package com.btireland.talos.ethernet.engine.workflow;

import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;

import java.util.Arrays;

public class InterventionIncidentProcessEnginePlugin extends AbstractProcessEnginePlugin {

    private InterventionIncidentHandler interventionIncidentHandler;

    public InterventionIncidentProcessEnginePlugin(InterventionIncidentHandler interventionIncidentHandler) {
        this.interventionIncidentHandler = interventionIncidentHandler;
    }

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        processEngineConfiguration.setCustomIncidentHandlers(Arrays.asList(interventionIncidentHandler));
    }
}
