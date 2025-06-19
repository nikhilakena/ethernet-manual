package com.btireland.talos.ethernet.engine.facade;


import com.btireland.talos.core.common.rest.exception.ResourceNotFoundException;
import com.btireland.talos.ethernet.engine.domain.InterventionDetails;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.dto.*;
import com.btireland.talos.ethernet.engine.exception.AuthenticationException;
import com.btireland.talos.ethernet.engine.exception.BadRequestException;
import com.btireland.talos.ethernet.engine.exception.PersistanceException;
import com.btireland.talos.ethernet.engine.exception.RequestValidationException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceBadRequestException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceNotFoundException;
import com.btireland.talos.ethernet.engine.mq.CerberusDataSyncMessageProducer;
import com.btireland.talos.ethernet.engine.service.PBTDCOrderService;
import com.btireland.talos.ethernet.engine.service.PBTDCOrderWorkflowService;
import com.btireland.talos.ethernet.engine.service.PbtdcOrdersPersistenceService;
import com.btireland.talos.ethernet.engine.soap.orders.PBTDCOrder;
import com.btireland.talos.ethernet.engine.soap.orders.PBTDCOrderresponse;
import com.btireland.talos.ethernet.engine.util.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Component that interacts with the service layer and the REST layer.
 * Its role :
 * <ol>
 * <li>translate the API data model (DTO objects) into the persistence data model (entities)</li>
 * <li>Aggregate the calls to the service layer. So that the REST controller can call only one method from the facade.
 * That way, we are sure the REST controller deals with HTTP stuff only. The Facade orchestrates the business. The
 * business is done by the service</li>
 * </ol>
 */
@Slf4j
@Component
@Transactional
public class PBTDCOrderFacade {
    private PBTDCOrderMapper objectMapper;
    private PbtdcMapper pbtdcMapper;
    private CerberusDataSyncMessageProducer dataSyncMessageProducer;
    private PBTDCOrderWorkflowService pBTDCOrderWorkflowService;
    private PBTDCOrderService pbtdcOrderService;
    private PbtdcOrdersPersistenceService pbtdcOrdersPersistenceService;

    public PBTDCOrderFacade(PBTDCOrderService pbtdcOrderService, PBTDCOrderMapper objectMapper,
                            PbtdcOrdersPersistenceService pbtdcOrdersPersistenceService,
                            PBTDCOrderWorkflowService pBTDCOrderWorkflowService,
                            PbtdcMapper pbtdcMapper, CerberusDataSyncMessageProducer dataSyncMessageProducer) {
        this.pbtdcOrderService = pbtdcOrderService;
        this.objectMapper = objectMapper;
        this.pbtdcOrdersPersistenceService = pbtdcOrdersPersistenceService;
        this.pBTDCOrderWorkflowService = pBTDCOrderWorkflowService;
        this.pbtdcMapper = pbtdcMapper;
        this.dataSyncMessageProducer = dataSyncMessageProducer;
    }


    public PBTDCOrderresponse createPBTDCOrder(PBTDCOrder pbtdcOrderRequest, String oao) throws BadRequestException,
            RequestValidationException, AuthenticationException, PersistanceException,
            OrderManagerServiceBadRequestException, OrderManagerServiceNotFoundException {
        //Create corresponding initial entries in the db
        PBTDCOrderresponse pBTDCOrderresponse =
                pbtdcOrderService.createPBTDCOrder(objectMapper.buildOrderManagerRequest(pbtdcOrderRequest, oao),
                        pbtdcOrderRequest.getVERSION());

        //Create Orders
        pBTDCOrderWorkflowService.processPbtdcOrders(pBTDCOrderresponse.getORDERID());
        pbtdcOrdersPersistenceService.createOrder(objectMapper.createPBTDCEntries(pbtdcOrderRequest,
                pBTDCOrderresponse, oao));
        return pBTDCOrderresponse;
    }

    public PBTDCOrderJsonResponse createPBTDCOrder(PBTDCOrderRequestDTO pbtdcOrderRequestDTO) throws PersistanceException, OrderManagerServiceNotFoundException, AuthenticationException, BadRequestException, OrderManagerServiceBadRequestException, RequestValidationException {
        PBTDCOrderJsonResponse pbtdcOrderJsonResponse = pbtdcOrderService.createPBTDCOrder2(pbtdcOrderRequestDTO.getPbtdc(), pbtdcOrderRequestDTO.getPbtdcOtherDetails());
        pBTDCOrderWorkflowService.processPbtdcOrders(pbtdcOrderJsonResponse.getPbtdcOrderData().getOrders().getOrderId());
        pbtdcOrdersPersistenceService.createOrder(pbtdcOrderRequestDTO.getPbtdc());
        return pbtdcOrderJsonResponse;
    }

    public List<InterventionDetailsDTO> getInterventions(Long wagOrderId, String status) {

        List<InterventionDetails> interventionDetailsList =
                pbtdcOrdersPersistenceService.getInterventionDetails(wagOrderId, Status.valueOf(status));

        if (interventionDetailsList == null || interventionDetailsList.isEmpty()) {
            throw new ResourceNotFoundException("no intervention details found for id ::" + wagOrderId + " and status" +
                    " ::" + status);
        }

        List<InterventionDetailsDTO> interventionDetailsDTOS = new ArrayList<InterventionDetailsDTO>();

        for (InterventionDetails interventionDetail : interventionDetailsList) {
            InterventionDetailsDTO interventionDetailsDTO = pbtdcMapper.toInterventionDetailsDTO(interventionDetail);

            interventionDetailsDTOS.add(interventionDetailsDTO);

        }
        return interventionDetailsDTOS;
    }

    public void closeIntervention(Long id, CloseInterventionDTO closeInterventionDTO) {

        InterventionDetails interventionDetails = pbtdcOrdersPersistenceService.findByInterventionId(id);
        if (interventionDetails == null) {
            throw new ResourceNotFoundException("No order found for intervention  id::" + id);
        }
        long wagOrderId = interventionDetails.getOrders().getWagOrderId();
        interventionDetails.setStatus(Status.CLOSED);
        interventionDetails.setAgent(closeInterventionDTO.getAgent());
        interventionDetails.setClosingNotes(closeInterventionDTO.getClosingNotes());
        pbtdcOrdersPersistenceService.updateInterventionDetails(interventionDetails);

        Pbtdc order = pbtdcOrdersPersistenceService.findByOrderId(wagOrderId);
        OrdersDTO ordersDTO = pbtdcMapper.toOrderDTO(order);
        if (order.getInterventionDetails() != null && !order.getInterventionDetails().isEmpty()) {
            InterventionDetails newInterventionDetails =
                    order.getInterventionDetails().get(order.getInterventionDetails().size() - 1);

            ordersDTO.setInterventionDetails(pbtdcMapper.toInterventionDetailsDTO(newInterventionDetails));
        }
        dataSyncMessageProducer.sendOrderData(ordersDTO);
    }


}
