package com.btireland.talos.quote.facade.workflow.delegate;

import com.btireland.talos.ethernet.engine.workflow.ServiceTask;
import com.btireland.talos.quote.facade.workflow.QuoteProcessVariables;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class GetRejectedQuotesServiceTask extends ServiceTask<QuoteProcessVariables> implements JavaDelegate {

    public GetRejectedQuotesServiceTask() {
    }

    @Override
    public void executeTask(long orderId, QuoteProcessVariables processVariables) throws Exception {
    }
}
