package com.btireland.talos.quote.facade.workflow.delegate;

import com.btireland.talos.core.common.rest.exception.checked.TalosInternalErrorException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.ethernet.engine.workflow.ServiceTask;
import com.btireland.talos.quote.facade.process.processor.QuoteWorkflowProcessor;
import com.btireland.talos.quote.facade.workflow.QuoteProcessVariables;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class SendQuoteCompleteNotificationServiceTask extends ServiceTask<QuoteProcessVariables> implements JavaDelegate {

    private final QuoteWorkflowProcessor quoteWorkflowProcessor;

    public SendQuoteCompleteNotificationServiceTask(QuoteWorkflowProcessor quoteWorkflowProcessor) {
        this.quoteWorkflowProcessor = quoteWorkflowProcessor;
    }

    /**
     * Override Method for executing service task for sending complete notification
     *
     * @param groupId the quote group id
     * @param processVariables Process Variables for the camunda process instance
     * @throws TalosNotFoundException      the talos not found exception
     * @throws JsonProcessingException     the json processing exception
     * @throws TalosInternalErrorException the talos internal error exception
     */
    @Override
    public void executeTask(long groupId, QuoteProcessVariables processVariables)
        throws TalosNotFoundException, TalosInternalErrorException, JsonProcessingException {

        quoteWorkflowProcessor.sendCompleteNotification(groupId);
    }
}
