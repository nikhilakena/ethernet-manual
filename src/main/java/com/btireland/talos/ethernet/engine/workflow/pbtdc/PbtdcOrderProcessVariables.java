package com.btireland.talos.ethernet.engine.workflow.pbtdc;

import com.btireland.talos.ethernet.engine.workflow.OrderProcessVariables;
import org.camunda.bpm.engine.delegate.DelegateExecution;


/**
 * Access (get and set) variables in Fibre provisioning order process
 */
public class PbtdcOrderProcessVariables extends OrderProcessVariables {

    public PbtdcOrderProcessVariables(DelegateExecution delegateExecution) {
        super(delegateExecution);
    }

    public void setWsiptProduct(){
        delegateExecution.setVariable("wsiptProduct", "no");
    }

    public Long getParkedNotificationId(){
        return (Long)delegateExecution.getVariable(PbtdcOrderProcessConstants.PARKED_NOTIFICATION_ID);
    }

    public Boolean isInternalNotificationType(){
        return (Boolean) delegateExecution.getVariable(PbtdcOrderProcessConstants.VAR_NAME_INTERNAL_NOTIFICATION_TYPE);
    }

    public void setInternalNotificationType(Boolean internalNotificationType){
        delegateExecution.setVariable(PbtdcOrderProcessConstants.VAR_NAME_INTERNAL_NOTIFICATION_TYPE, internalNotificationType);
    }

    public Boolean isDeliveryOnTrackChanged(){
        return (Boolean) delegateExecution.getVariable(PbtdcOrderProcessConstants.VAR_NAME_DELIVERY_ON_TRACK_CHANGED);
    }

    public void setDeliveryOnTrackChanged(Boolean deliveryOnTrackChanged){
        delegateExecution.setVariable(PbtdcOrderProcessConstants.VAR_NAME_DELIVERY_ON_TRACK_CHANGED, deliveryOnTrackChanged);
    }

    public Boolean isWsiptProduct(){
        return (Boolean) delegateExecution.getVariable(PbtdcOrderProcessConstants.VAR_NAME_WSIPT_PRODUCT);
    }

    public void setWsiptProduct(Boolean wsiptProduct){
        delegateExecution.setVariable(PbtdcOrderProcessConstants.VAR_NAME_WSIPT_PRODUCT, wsiptProduct);
    }

    public Boolean isNotesTextEmpty(){
        return (Boolean) delegateExecution.getVariable(PbtdcOrderProcessConstants.VAR_NAME_NOTES_TEXT_EMPTY);
    }

    public void setNotesTextEmpty(Boolean notesTextEmpty){
        delegateExecution.setVariable(PbtdcOrderProcessConstants.VAR_NAME_NOTES_TEXT_EMPTY, notesTextEmpty);
    }
}
