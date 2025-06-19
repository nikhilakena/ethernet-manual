package com.btireland.talos.ethernet.engine.workflow.pbtdc.validation;

import com.btireland.talos.ethernet.engine.exception.UserUnauthorizedException;
import com.btireland.talos.ethernet.engine.exception.ValidationException;
import com.btireland.talos.ethernet.engine.service.OrderValidationService;
import com.btireland.talos.ethernet.engine.workflow.ServiceTask;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessConstants;
import com.btireland.talos.ethernet.engine.workflow.pbtdc.PbtdcOrderProcessVariables;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CheckQuoteServiceTask extends ServiceTask<PbtdcOrderProcessVariables> implements JavaDelegate {

    private OrderValidationService orderValidationService;

    @Override
    public void executeTask(long orderId, PbtdcOrderProcessVariables processVariables) throws Exception {
        try {
            orderValidationService.validateAndPersistQuote(orderId);
        } catch (ValidationException e) {
            throw new BpmnError(PbtdcOrderProcessConstants.Error.QUOTE_INVALID.name(), e);
        } catch (UserUnauthorizedException e) {
            throw new BpmnError(PbtdcOrderProcessConstants.Error.USER_UNAUTHORIZED.name(), e);
        }
    }
}
