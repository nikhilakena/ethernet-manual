package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.domain.PBTDCBusinessStatus;
import com.btireland.talos.ethernet.engine.util.OrderStatus;
import com.btireland.talos.ethernet.engine.util.PhaseStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class PBTDCBusinessStatusService {

    private static final String PLANNING="planning";
    private static final String ACCESS_INSTALLATION="access_installation";
    private static final String TESTING_CPE_INSTALLATION="testing_cpe_installation";


    public PBTDCBusinessStatus updateBusinessStatusForDelayStart(PBTDCBusinessStatus pbtdcBusinessStatus) {
        if (isValidStatus(pbtdcBusinessStatus.getOrderEntryAndValidationStatus())) {
            pbtdcBusinessStatus.setOrderEntryAndValidationStatus(PhaseStatus.CUSTOMER_DELAY.getPhaseStatusValue());
        }
        else if (isValidStatus(pbtdcBusinessStatus.getPlanningStatus())) {
            pbtdcBusinessStatus.setPlanningStatus(PhaseStatus.CUSTOMER_DELAY.getPhaseStatusValue());
        }
        else if (isValidStatus(pbtdcBusinessStatus.getAccessInstallation())) {
            pbtdcBusinessStatus.setAccessInstallation(PhaseStatus.CUSTOMER_DELAY.getPhaseStatusValue());
        }
        else if (isValidStatus(pbtdcBusinessStatus.getTestingCpeInstallation())) {
            pbtdcBusinessStatus.setTestingCpeInstallation(PhaseStatus.CUSTOMER_DELAY.getPhaseStatusValue());
        }


        return pbtdcBusinessStatus;
    }

    public PBTDCBusinessStatus updateBusinessStatusForDelayEnd(PBTDCBusinessStatus pbtdcBusinessStatus) {
        if (isValidStatus(pbtdcBusinessStatus.getOrderEntryAndValidationStatus())) {
            pbtdcBusinessStatus.setOrderEntryAndValidationStatus(PhaseStatus.WIP.getPhaseStatusValue());
        }
        else if (isValidStatus(pbtdcBusinessStatus.getPlanningStatus())) {
            pbtdcBusinessStatus.setPlanningStatus(PhaseStatus.WIP.getPhaseStatusValue());
        }
        else if (isValidStatus(pbtdcBusinessStatus.getAccessInstallation())) {
            pbtdcBusinessStatus.setAccessInstallation(PhaseStatus.WIP.getPhaseStatusValue());
        }
        else if (isValidStatus(pbtdcBusinessStatus.getTestingCpeInstallation())) {
            pbtdcBusinessStatus.setTestingCpeInstallation(PhaseStatus.WIP.getPhaseStatusValue());
        }


        return pbtdcBusinessStatus;
    }

    public PBTDCBusinessStatus updateBusinessStatusForStatusNotification(PBTDCBusinessStatus pbtdcBusinessStatus, String orderStatus) {
        Set<String> planningOrderStatusSet = OrderStatus.getOrderStatusMapForBusinessStatusPhase(PLANNING);
        if(planningOrderStatusSet.contains(orderStatus)) {
            pbtdcBusinessStatus.setPlanningStatus(PhaseStatus.WIP.getPhaseStatusValue());
        } else if (isValidStatus(pbtdcBusinessStatus.getPlanningStatus())) {
            pbtdcBusinessStatus.setPlanningStatus(PhaseStatus.COMPLETE.getPhaseStatusValue());
        }

        Set<String> accessInstallationOrderStatusSet = OrderStatus.getOrderStatusMapForBusinessStatusPhase(ACCESS_INSTALLATION);
        if(accessInstallationOrderStatusSet.contains(orderStatus)) {
            pbtdcBusinessStatus.setAccessInstallation(PhaseStatus.WIP.getPhaseStatusValue());
        } else if (isValidStatus(pbtdcBusinessStatus.getAccessInstallation()) && !(OrderStatus.getBusinessStatusPhaseForTalosOrderStatus(orderStatus) == null) && !OrderStatus.getBusinessStatusPhaseForTalosOrderStatus(orderStatus).equalsIgnoreCase(PLANNING)) {
            pbtdcBusinessStatus.setAccessInstallation(PhaseStatus.COMPLETE.getPhaseStatusValue());
        }

        Set<String> testingCPEInstallationOrderStatusSet = OrderStatus.getOrderStatusMapForBusinessStatusPhase(TESTING_CPE_INSTALLATION);
        if(testingCPEInstallationOrderStatusSet.contains(orderStatus)) {
            pbtdcBusinessStatus.setTestingCpeInstallation(PhaseStatus.WIP.getPhaseStatusValue());
        }

        if (isValidStatus(pbtdcBusinessStatus.getOrderEntryAndValidationStatus())) {
            pbtdcBusinessStatus.setOrderEntryAndValidationStatus(PhaseStatus.COMPLETE.getPhaseStatusValue());
        }

        return pbtdcBusinessStatus;
    }

    public PBTDCBusinessStatus updateBusinessStatusForUndelivered(PBTDCBusinessStatus pbtdcBusinessStatus) {
        if (isValidStatus(pbtdcBusinessStatus.getOrderEntryAndValidationStatus())) {
            pbtdcBusinessStatus.setOrderEntryAndValidationStatus(PhaseStatus.ORDER_CANCELLED.getPhaseStatusValue());
        }
        else if (isValidStatus(pbtdcBusinessStatus.getPlanningStatus())) {
            pbtdcBusinessStatus.setPlanningStatus(PhaseStatus.ORDER_CANCELLED.getPhaseStatusValue());
        }
        else if (isValidStatus(pbtdcBusinessStatus.getAccessInstallation())) {
            pbtdcBusinessStatus.setAccessInstallation(PhaseStatus.ORDER_CANCELLED.getPhaseStatusValue());
        }
        else if (isValidStatus(pbtdcBusinessStatus.getTestingCpeInstallation())) {
            pbtdcBusinessStatus.setTestingCpeInstallation(PhaseStatus.ORDER_CANCELLED.getPhaseStatusValue());
        }

        return pbtdcBusinessStatus;
    }

    public PBTDCBusinessStatus updateBusinessStatusForAPTR(PBTDCBusinessStatus pbtdcBusinessStatus) {
        if (isValidStatus(pbtdcBusinessStatus.getOrderEntryAndValidationStatus())) {
            pbtdcBusinessStatus.setOrderEntryAndValidationStatus(PhaseStatus.COMPLETE.getPhaseStatusValue());
        }
        if (isValidStatus(pbtdcBusinessStatus.getPlanningStatus())) {
            pbtdcBusinessStatus.setPlanningStatus(PhaseStatus.COMPLETE.getPhaseStatusValue());
        }
        if (isValidStatus(pbtdcBusinessStatus.getAccessInstallation())) {
            pbtdcBusinessStatus.setAccessInstallation(PhaseStatus.COMPLETE.getPhaseStatusValue());
        }
        if (isValidStatus(pbtdcBusinessStatus.getTestingCpeInstallation())) {
            pbtdcBusinessStatus.setTestingCpeInstallation(PhaseStatus.BOOK_APPOINTMENT.getPhaseStatusValue());
        }
        return pbtdcBusinessStatus;
    }

    public PBTDCBusinessStatus updateBusinessStatusForAccept(PBTDCBusinessStatus pbtdcBusinessStatus, LocalDate deliveryDate) {
        pbtdcBusinessStatus.setOrderEntryAndValidationStatus(PhaseStatus.WIP.getPhaseStatusValue());

        pbtdcBusinessStatus.setDeliveryDate(deliveryDate);
        return pbtdcBusinessStatus;
    }

    public PBTDCBusinessStatus updateBusinessStatusForConfirmation(PBTDCBusinessStatus pbtdcBusinessStatus, LocalDate deliveryDate) {
        pbtdcBusinessStatus.setDeliveryDate(deliveryDate);
        return pbtdcBusinessStatus;
    }
    public PBTDCBusinessStatus updateBusinessStatusForComplete(PBTDCBusinessStatus pbtdcBusinessStatus) {
        if (isValidStatus(pbtdcBusinessStatus.getOrderEntryAndValidationStatus())) {
            pbtdcBusinessStatus.setOrderEntryAndValidationStatus(PhaseStatus.COMPLETE.getPhaseStatusValue());
        }
        if (isValidStatus(pbtdcBusinessStatus.getPlanningStatus())) {
            pbtdcBusinessStatus.setPlanningStatus(PhaseStatus.COMPLETE.getPhaseStatusValue());
        }
        if (isValidStatus(pbtdcBusinessStatus.getAccessInstallation())) {
            pbtdcBusinessStatus.setAccessInstallation(PhaseStatus.COMPLETE.getPhaseStatusValue());
        }
        if (isValidStatus(pbtdcBusinessStatus.getTestingCpeInstallation())) {
            pbtdcBusinessStatus.setTestingCpeInstallation(PhaseStatus.COMPLETE.getPhaseStatusValue());
        }
        if (isValidStatus(pbtdcBusinessStatus.getServiceCompleteAndOperational())) {
            pbtdcBusinessStatus.setServiceCompleteAndOperational(PhaseStatus.COMPLETE.getPhaseStatusValue());
        }
        if (isValidStatus(pbtdcBusinessStatus.getNetworkProvisioning())) {
            pbtdcBusinessStatus.setNetworkProvisioning(PhaseStatus.COMPLETE.getPhaseStatusValue());
        }
        return pbtdcBusinessStatus;
    }

    boolean isValidStatus(String status) {
        List<String> phaseValues = List.of(PhaseStatus.COMPLETE.getPhaseStatusValue(), PhaseStatus.UNDELIVERABLE.getPhaseStatusValue(), PhaseStatus.REJECTED.getPhaseStatusValue());
        if(status==null)
            return true;
        return !phaseValues.contains(status);
    }

}
