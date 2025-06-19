package com.btireland.talos.ethernet.engine.workflow;

import com.btireland.talos.ethernet.engine.domain.InterventionDetails;
import com.btireland.talos.ethernet.engine.domain.Orders;
import com.btireland.talos.ethernet.engine.dto.InterventionDetailsDTO;
import com.btireland.talos.ethernet.engine.service.InterventionService;
import com.btireland.talos.ethernet.engine.service.OrdersPersistenceService;
import com.btireland.talos.ethernet.engine.util.Color;
import com.btireland.talos.ethernet.engine.util.Status;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessVariables;
import com.btireland.talos.quote.facade.workflow.QuoteProcessConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.incident.DefaultIncidentHandler;
import org.camunda.bpm.engine.impl.incident.IncidentContext;
import org.camunda.bpm.engine.runtime.Incident;
import java.util.List;

/**
 * Extends {@link DefaultIncidentHandler} to create an incident in Camunda and set the order status
 * to "INTERVENTION" in cerberus
 */
@Slf4j
public class InterventionIncidentHandler extends DefaultIncidentHandler {

    private InterventionService interventionService;

    private OrdersPersistenceService ordersPersistenceService;



    public InterventionIncidentHandler(String type, InterventionService interventionService, OrdersPersistenceService ordersPersistenceService) {
        super(type);
        this.interventionService = interventionService;
        this.ordersPersistenceService = ordersPersistenceService;
    }

    DelegateExecution findExecution(String executionId) {
        return Context
                .getCommandContext()
                .getExecutionManager()
                .findExecutionById(executionId);
    }

    Long getOrderId(DelegateExecution execution) {
        PbtdcOrderProcessVariables orderProcessVariables = new PbtdcOrderProcessVariables(execution);
        return orderProcessVariables.getOrderId();
    }

    @Override
    public Incident handleIncident(IncidentContext context, String message) {
        Incident incident = super.handleIncident(context, message);

        DelegateExecution execution = findExecution(context.getExecutionId());
        String processDefinitionKey = execution.getProcessEngineServices().getRepositoryService()
                .getProcessDefinition(execution.getProcessDefinitionId()).getKey();

        if(!QuoteProcessConstants.PROC_DEF_KEY.equals(processDefinitionKey)) {
            if (execution == null || StringUtils.isEmpty(context.getActivityId())) {
                log.info("cannot retrieve execution {} or activityId is null, do not apply customisation on incident resolution", context.getExecutionId());
            } else {
                Long orderId = getOrderId(execution);
                openIntervention(orderId, context, message);
            }
        }
        log.debug("returning incident");

        return incident;
    }

    private void openIntervention(Long orderId, IncidentContext context, String message) {
        InterventionDetails interventionDetails = InterventionDetails.builder()
                .color(Color.RED)
                .agent(InterventionDetailsDTO.DEFAULT_AGENT)
                .status(Status.OPEN)
                .workflow(context.getActivityId())
                .notes(message)
                .build();
        log.warn("order {}, creating intervention {}", orderId, interventionDetails);
        Orders order = ordersPersistenceService.findByOrderId(orderId);
        interventionService.createOrUpdateIntervention(order, interventionDetails);
        ordersPersistenceService.update(order);

    }

    @Override
    public void resolveIncident(IncidentContext context) {
        log.info("resolveIncident invoked for incident with Job ID :" + context.getConfiguration());
        DelegateExecution execution = findExecution(context.getExecutionId());

        if (execution == null && StringUtils.isEmpty(context.getActivityId())) {
            log.info("cannot retrieve execution {} and activityId are null, do not apply customisation on incident resolution", context.getExecutionId());
        } else if (execution == null) {
            log.info("cannot retrieve execution {} is null, do not apply customisation on incident resolution", context.getExecutionId());
        } else {
            Long orderId = getOrderId(execution);
            if(orderId!=null) {
                closeInterventions(orderId, context);
            }
        }

        super.resolveIncident(context);
    }

    private void closeInterventions(Long orderId, IncidentContext context) {
        List<Incident> incidents = Context
                .getCommandContext()
                .getIncidentManager()
                .findIncidentByConfiguration(context.getConfiguration());

        for (Incident incident : incidents) {
            closeIntervention(orderId, incident);
        }
    }

    private void closeIntervention(Long orderId, Incident incident) {
        InterventionDetails interventionDetails = InterventionDetails.builder()
                .workflow(incident.getActivityId())
                .closingAgent(InterventionDetailsDTO.DEFAULT_AGENT)
                .color(Color.CLEAR)
                .status(Status.CLOSED)
                .closingNotes(InterventionDetailsDTO.DEFAULT_CLOSING_NOTES)
                .build();
        log.warn("order {}, closing intervention {}", orderId, interventionDetails);
        Orders order = ordersPersistenceService.findByOrderId(orderId);
        interventionService.createOrUpdateIntervention(order, interventionDetails);
        ordersPersistenceService.update(order);
    }
}
