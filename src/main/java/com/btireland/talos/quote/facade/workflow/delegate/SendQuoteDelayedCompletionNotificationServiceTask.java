package com.btireland.talos.quote.facade.workflow.delegate;

import com.btireland.talos.core.common.rest.exception.checked.TalosInternalErrorException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.ethernet.engine.exception.NotificationClientException;
import com.btireland.talos.ethernet.engine.workflow.ServiceTask;
import com.btireland.talos.quote.facade.process.processor.QuoteWorkflowProcessor;
import com.btireland.talos.quote.facade.workflow.QuoteProcessVariables;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class SendQuoteDelayedCompletionNotificationServiceTask extends ServiceTask<QuoteProcessVariables> implements JavaDelegate {

    private final QuoteWorkflowProcessor quoteWorkflowProcessor;

    public SendQuoteDelayedCompletionNotificationServiceTask(QuoteWorkflowProcessor quoteWorkflowProcessor) {
        this.quoteWorkflowProcessor = quoteWorkflowProcessor;
    }

    /**
     * Override Method for executing service task for sending delay completion
     * @param groupId
     * @param processVariables
     * @throws TalosNotFoundException
     * @throws NotificationClientException
     * @throws JsonProcessingException
     * @throws TalosInternalErrorException
     */
    @Override
    public void executeTask(long groupId, QuoteProcessVariables processVariables) throws TalosNotFoundException,
        NotificationClientException, JsonProcessingException, TalosInternalErrorException {
        quoteWorkflowProcessor.sendDelayedNotification(groupId);
    }
}
