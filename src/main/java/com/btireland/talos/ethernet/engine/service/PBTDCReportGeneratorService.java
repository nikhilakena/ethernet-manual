package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.rest.exception.checked.TalosForbiddenException;
import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderReportDTO;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderReportSetDTO;
import com.btireland.talos.ethernet.engine.dto.PbtdcReportDateView;
import com.btireland.talos.ethernet.engine.dto.ReportAvailabilityDTO;
import com.btireland.talos.ethernet.engine.exception.BadRequestException;
import com.btireland.talos.ethernet.engine.exception.ReportGenerationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PBTDCReportGeneratorService {

    public static final Logger LOGGER = LoggerFactory.getLogger(PBTDCReportGeneratorService.class);
    private static final String DELIVERY_TYPE_STR="New Delivery";
    private PbtdcOrdersPersistenceService pbtdcOrdersPersistenceService;
    private PBTDCReportPersistenceService pbtdcReportPersistenceService;
    private ApplicationConfiguration applicationConfiguration;

    @Value("${application.report.active-time-period}")
    private int activeTimePeriodInDays;

    private boolean overwriteSupported = false;
    public PBTDCReportGeneratorService(PbtdcOrdersPersistenceService pbtdcOrdersPersistenceService,
                                       PBTDCReportPersistenceService pbtdcReportPersistenceService,
                                       ApplicationConfiguration applicationConfiguration){
        this.pbtdcOrdersPersistenceService = pbtdcOrdersPersistenceService;
        this.pbtdcReportPersistenceService  = pbtdcReportPersistenceService;
        this.applicationConfiguration = applicationConfiguration;

        if(applicationConfiguration.isEnableReportOverwrite()){
            this.overwriteSupported = true;
        }
    }

    private String buildReferenceIdString(Pbtdc order, Boolean internal){
        /* Build up the ReferenceIDs string */

        // Reference IDs. This is a formatted string including the following data :
        // - NOG Order Number
        // - Siebel Account Number
        // - Siebel Order Ref
        // - Logical BT Circuit ID
        // - A End Etherway BT Circuit ID
        // - B End Etherway BT Circuit ID
        // - BT Logical VLAN
        // - Lead Delivery Manager firstname, lastname, contact number, email
        // - customer internal details 1-5
        // - LogicalLink Glan Id
        // - CustomerAccess Glan Id
        // - WholesalerAccess Glan Id

        StringBuilder referenceIdString = new StringBuilder();

        if(order.getOrderNumber() != null){
            referenceIdString.append("NOG Order Number : ").append(order.getOrderNumber()).append('\n');
        }

        if(order.getAccountNumber() != null){
            referenceIdString.append("Siebel Account Number : ").append(order.getAccountNumber()).append('\n');
        }

        // Siebel order ref only included on Internal reports
        if(internal == Boolean.TRUE){
            String siebelNumber = order.getSupplierOrderSiebelNumberAsString();
            if (!siebelNumber.isEmpty()) {
                referenceIdString.append("Siebel Order Ref : ").append(order.getSupplierOrderSiebelNumberAsString()).append('\n');
            }
        }

        String logicalLinkCircuitReference = order.getLogicalLinkCircuitReferenceAsString();
        String customerAccessCircuitReference = order.getCustomerAccessCircuitReferenceAsString();
        String wholesalerAccessCircuitReference = order.getWholesalerAccessCircuitReferenceAsString();
        String logicalLinkVlan = order.getLogicalLinkVlanAsString();
        String leadDeliveryManager = order.getLeadDeliveryManagerAsString();
        String customerInternalDetails = order.getCustomerInternalDetailsAsString();

        if((!logicalLinkCircuitReference.isEmpty()) && (order.getLogicalLink() != null)){
            referenceIdString.append("Logical BT Circuit ID : ")
                    .append(logicalLinkCircuitReference).append('\n');
        }

        if((!customerAccessCircuitReference.isEmpty()) && (order.getCustomerAccess() != null)){
            // a-end = customer
            referenceIdString.append("A End Etherway BT Circuit ID : ")
                    .append(customerAccessCircuitReference).append('\n');
        }

        if(!wholesalerAccessCircuitReference.isEmpty()&& (order.getWholesalerAccess() != null)){
            // b-end = wholesaler
            referenceIdString.append("B End Etherway BT Circuit ID : ")
                    .append(wholesalerAccessCircuitReference).append('\n');
        }
        if(!logicalLinkVlan.isEmpty()){
            referenceIdString.append("BT Logical VLAN : ").append(logicalLinkVlan).append('\n');
        }
        if(!leadDeliveryManager.isEmpty()){
            referenceIdString.append("Company Lead Delivery Manager : ").append(leadDeliveryManager).append('\n');
        }
        if (!customerInternalDetails.isEmpty()) {
            // Internal Details already have a newline so no need for another one
            referenceIdString.append(customerInternalDetails);
        }

        if(internal == Boolean.TRUE) {

            //Add Glan Ids for LogicalLink, CustomerAccess and WholesalerAccess
            if(!order.getLogicalLinkGlanIdAsString().isEmpty()) {
                referenceIdString.append("GLAN Etherflow Order Ref : ").append(order.getLogicalLinkGlanIdAsString()).append('\n');
            }

            if(!order.getCustomerAccessGlanIdAsString().isEmpty()) {
                referenceIdString.append("GLAN Etherway A end Order Ref : ").append(order.getCustomerAccessGlanIdAsString()).append('\n');
            }

            if(!order.getWholesalerAccessGlanIdAsString().isEmpty()) {
                referenceIdString.append("GLAN Etherway B end Order Ref : ").append(order.getWholesalerAccessGlanIdAsString()).append('\n');
            }
        }

        return referenceIdString.toString();
    }

    private PBTDCOrderReportDTO.PBTDCOrderReportDTOBuilder produceReportObject(Pbtdc order){
            var orderReportRecordBuilder = PBTDCOrderReportDTO.builder();
            orderReportRecordBuilder
                    .orderManager(order.getOrderManagerNameAsString())
                    .orderManagerEmail(order.getOrderManagerEmailAsString())
                    .customerName("")
                    .siteName(StringUtils.defaultString(order.getOrganisationName()))
                    .dateReceivedByDelivery(order.dateReceivedByDeliveryAsString())
                    .product(order.getProductAsString())
                    .connectionType(order.getConnectionTypeAsString())
                    .deliveryType(DELIVERY_TYPE_STR)
                    // A End Site Details (a=end = customer)
                    .aendSiteDetails(order.getCustomerSiteDetailsAsString())
                    .aendContactDetails(order.getCustomerContactDetailsAsString())
                    // B End Site Details (b-end = wholesaler)
                    .bendSiteDetails(order.getWholesaleSiteDetailsAsString(applicationConfiguration.getHandoverMap()))
                    .bendContactDetails(order.getWholesaleContactDetailsAsString())
                    .accessSpeed(order.getAccessSpeedAsString())
                    .portSpeed(order.getPortSpeedAsString())
                    // a-end = customer
                    .aendPresentation(order.getCustomerAccessTerminatingEquipmentPresentationAsString())
                    // b-end = wholesaler
                    .bendPresentation(order.getWholesaleAccessTerminatingEquipmentPresentationAsString())
                    .notes((order.getNotesReceivedDateAsString().isEmpty() ? "" : "(Dated ") + StringUtils.defaultString(order.getNotesReceivedDateAsString()) + (order.getNotesReceivedDateAsString().isEmpty() ? "" : ") ") + StringUtils.defaultString(order.getNotes()))
                    .deliveryDate(order.getDeliveryDateAsString())
                    .deliveryOnTrack(order.getBusinessStatusDeliveryOnTrackAsString())
                    .orderEntryAndValidationStatus(order.getBusinessStatusOrderEntryValidationStatusAsString())
                    .planningStatus(order.getBusinessStatusPlanningStatusAsString())
                    .accessInstallation(order.getBusinessStatusAccessInstallationAsString())
                    .testingCpeInstallation(order.getBusinessStatusTestingCpeInstallationAsString())
                    .serviceCompleteAndOperational(order.getBusinessStatusServiceCompleteAndOperationalAsString());

            // we populate the indicative completion date only if there is no completion date

            if(order.getDueCompletionDateAsString().equals("")){
                orderReportRecordBuilder.indicativeDeliveryDate(order.getEstimatedCompletionDateAsString());
            }else{
                orderReportRecordBuilder.indicativeDeliveryDate("");
            }

            return orderReportRecordBuilder;
    }

    public boolean generate(Optional<LocalDate> date) throws ReportGenerationException, TalosForbiddenException {
        LocalDate reportDate;

        if(date.isPresent()) {
            reportDate = date.get();
        }else{
            reportDate = LocalDate.now();
        }
        //  if the report already exists and overwrite is disabled, return false.

        if(pbtdcReportPersistenceService.checkReportExistsForDate(reportDate) == true){
            if(this.overwriteSupported == false){
                throw new TalosForbiddenException(LOGGER,"REPORT_GENERATION_NOT_ALLOWED", "Cannot overwrite existing " +
                        "report record(s)");
            }else{
                // Delete any reports that currently exist for the given date.
                pbtdcReportPersistenceService.deleteAllReportsByDate(reportDate);
            }
        }
        // get a list of OAOs with active orders
        LocalDateTime timeFrame = LocalDateTime.now().minusDays(activeTimePeriodInDays);
        List<String> oaos = pbtdcOrdersPersistenceService.findOaosWithActiveOrders(timeFrame);
        for(String oao : oaos){
            List<Pbtdc> orders = pbtdcOrdersPersistenceService.findActiveOrdersByOao(oao,timeFrame);
            List<PBTDCOrderReportDTO> internalOrderReportEntries = new ArrayList<>();
            List<PBTDCOrderReportDTO> externalOrderReportEntries = new ArrayList<>();

            for(Pbtdc order : orders){
                PBTDCOrderReportDTO.PBTDCOrderReportDTOBuilder orderReportBuilder = produceReportObject(order);
                // create the internal report for this order
                orderReportBuilder.referenceIds(buildReferenceIdString(order, true));
                internalOrderReportEntries.add(orderReportBuilder.build());
                // create the external report for this order
                orderReportBuilder.referenceIds(buildReferenceIdString(order, false));
                externalOrderReportEntries.add(orderReportBuilder.build());
            }

            PBTDCOrderReportSetDTO reportSetDTO = PBTDCOrderReportSetDTO.builder()
                            .internalReportEntries(internalOrderReportEntries)
                            .externalReportEntries(externalOrderReportEntries)
                            .build();
            pbtdcReportPersistenceService.save(oao.toLowerCase(), reportDate, reportSetDTO);
        }
        return true;
    }

    public ReportAvailabilityDTO retrieveReportAvailability(LocalDate startDate, LocalDate endDate, String oao) throws BadRequestException {
        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("Date range for report is invalid");
        }
        //validating range is within 31 days
        if (startDate.datesUntil(endDate.plusDays(1)).count() > applicationConfiguration.getReport().getAvailabilityDateRange()) {
            throw new BadRequestException("Date range should not exceed " + applicationConfiguration.getReport().getAvailabilityDateRange() + " days.");
        }

        List<PbtdcReportDateView> availableReportDatesForRange = pbtdcReportPersistenceService.getReportsAvailableDatesForDateRange(startDate, endDate, oao);
        List<Date> availableReportDates = availableReportDatesForRange.stream().map(PbtdcReportDateView::getReportDate).collect(Collectors.toList());
        //Marking all dates with existing reports as true and others as false in map
        Map<LocalDate, Boolean> reportAvailability = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toMap(date -> date, date -> availableReportDates.contains(java.sql.Date.valueOf(date)), (u, v) -> u, LinkedHashMap::new));
        ReportAvailabilityDTO reportAvailabilityDTO = ReportAvailabilityDTO.builder().reportAvailabilityDates(reportAvailability).build();
        return reportAvailabilityDTO;

    }
}
