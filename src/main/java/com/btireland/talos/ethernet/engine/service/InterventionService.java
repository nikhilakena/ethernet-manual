package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.rest.exception.ResourceNotFoundException;
import com.btireland.talos.ethernet.engine.domain.InterventionDetails;
import com.btireland.talos.ethernet.engine.domain.Orders;
import com.btireland.talos.ethernet.engine.dto.OrdersDTO;
import com.btireland.talos.ethernet.engine.facade.OrderMapper;
import com.btireland.talos.ethernet.engine.mq.CerberusDataSyncMessageProducer;
import com.btireland.talos.ethernet.engine.util.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class InterventionService {

    private CerberusDataSyncMessageProducer dataSyncMessageProducer;
    private OrderMapper orderMapper;
    public InterventionService(CerberusDataSyncMessageProducer dataSyncMessageProducer, OrderMapper orderMapper) {
        this.dataSyncMessageProducer = dataSyncMessageProducer;
        this.orderMapper = orderMapper;
    }

    public Orders createOrUpdateIntervention(Orders order, InterventionDetails interventionDetails) {
        if (interventionDetails.isOpen()) {
            log.debug("saving intervention details : intervention already open");
            order= saveInterventionDetails(order, interventionDetails);
        } else {
            log.debug("saving intervention details : closing intervention");
            order= closeInterventionStatus(order, interventionDetails);
            if(Boolean.FALSE.equals(order.getInterventionFlag())){
                order.setPausedOnErrorFlag(Boolean.FALSE);
            }
        }
        OrdersDTO ordersDTO = orderMapper.toOrderDTO(order);
        ordersDTO.setInterventionDetails(orderMapper.toInterventionDetailsDTO(interventionDetails));

        //Push process Order data for Order Manager for cerberus data sync
        log.debug("saving intervention details : send order DTO : "  + ordersDTO);
        dataSyncMessageProducer.sendOrderData(ordersDTO);

        return order;
    }

    public Orders saveInterventionDetails(Orders order, InterventionDetails interventionDetails)
    {
        if (order == null) {
            throw new ResourceNotFoundException("No order found with id " + order.getWagOrderId());
        }

        if(order.getInterventionDetails() == null){
            order.setInterventionDetails(new ArrayList<InterventionDetails>());
        }
        order.getInterventionDetails().add(interventionDetails);
        interventionDetails.setOrders(order);
        return order;
    }


    public Orders closeInterventionStatus(Orders order, InterventionDetails interventionDetails) {
        log.info("closing intervention");
        if (order == null) {
            throw new ResourceNotFoundException("No order found with id " + order.getWagOrderId());
        }

        order.getInterventionDetails().stream().filter(interventionDetailsObj -> interventionDetails.getWorkflow().equals(interventionDetailsObj.getWorkflow()))
                .forEach(interventionDetailsObj -> {
                    interventionDetailsObj.setStatus(Status.CLOSED);
                    interventionDetailsObj.setClosingNotes(interventionDetails.getClosingNotes());
                    interventionDetailsObj.setClosingAgent(interventionDetails.getClosingAgent());
                });

        return order;
    }
}
