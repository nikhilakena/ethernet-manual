package com.btireland.talos.quote.facade.workflow;

import com.btireland.talos.ethernet.engine.workflow.OrderProcessVariables;
import java.util.List;
import org.camunda.bpm.engine.delegate.DelegateExecution;


/**
 * Access (get and set) variables in Fibre provisioning order process
 */
public class QuoteProcessVariables extends OrderProcessVariables {

    public QuoteProcessVariables(DelegateExecution delegateExecution) {
        super(delegateExecution);
    }

    public void setQuoteItemIdList(List<String> quotes){
        delegateExecution.setVariable(QuoteProcessConstants.VAR_NAME_QUOTE_ID_LIST,quotes );
    }

    public List<String> getQuoteItemIdList(){
        return (List) delegateExecution.getVariable(QuoteProcessConstants.VAR_NAME_QUOTE_ID_LIST);
    }

    public String getQuoteItemId(){
        return delegateExecution.getVariable(QuoteProcessConstants.VAR_NAME_QUOTE_ID).toString();
    }

}
