package com.btireland.talos.ethernet.engine.config;

import com.btireland.talos.ethernet.engine.service.InterventionService;
import com.btireland.talos.ethernet.engine.workflow.InterventionIncidentHandler;
import com.btireland.talos.ethernet.engine.workflow.InterventionIncidentProcessEnginePlugin;
import com.btireland.talos.ethernet.engine.service.OrdersPersistenceService;
import com.btireland.talos.ethernet.engine.workflow.TalosFailedJobCommandFactory;
import com.btireland.talos.ethernet.engine.workflow.TalosFailedJobConfiguration;
import org.camunda.bpm.engine.runtime.Incident;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaFailedJobConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BpmnConfiguration {

    @Bean
    public CamundaFailedJobConfiguration failedJobConfiguration(TalosFailedJobCommandFactory talosFailedJobCommandFactory) {
        return new TalosFailedJobConfiguration(talosFailedJobCommandFactory);
    }

    @Bean
    public InterventionIncidentHandler interventionIncidentHandler(InterventionService interventionService, OrdersPersistenceService ordersPersistenceService){
        return new InterventionIncidentHandler(Incident.FAILED_JOB_HANDLER_TYPE, interventionService, ordersPersistenceService);
    }

    @Bean
    public InterventionIncidentProcessEnginePlugin interventionIncidentProcessEnginePlugin(InterventionIncidentHandler handler){
        return new InterventionIncidentProcessEnginePlugin(handler);
    }

}
