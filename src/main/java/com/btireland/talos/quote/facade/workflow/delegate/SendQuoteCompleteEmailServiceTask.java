package com.btireland.talos.quote.facade.workflow.delegate;

import com.btireland.talos.core.common.rest.exception.checked.TalosInternalErrorException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.ethernet.engine.workflow.ServiceTask;
import com.btireland.talos.quote.facade.process.processor.QuoteWorkflowProcessor;
import com.btireland.talos.quote.facade.workflow.QuoteProcessVariables;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class SendQuoteCompleteEmailServiceTask extends ServiceTask<QuoteProcessVariables> implements JavaDelegate {

    private final QuoteWorkflowProcessor quoteWorkflowProcessor;

    public SendQuoteCompleteEmailServiceTask(QuoteWorkflowProcessor quoteWorkflowProcessor) {
        this.quoteWorkflowProcessor = quoteWorkflowProcessor;
    }

    @Override
    public void executeTask(long groupId, QuoteProcessVariables processVariables) throws TalosNotFoundException,
            TalosInternalErrorException {
        quoteWorkflowProcessor.sendQuoteEmail(groupId);
    }
}
