package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.domain.InterventionDetails;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.dto.InterventionDetailsDTO;
import com.btireland.talos.ethernet.engine.facade.OrderMapper;
import com.btireland.talos.ethernet.engine.mq.CerberusDataSyncMessageProducer;
import com.btireland.talos.ethernet.engine.util.OrdersDTOFactory;
import com.btireland.talos.ethernet.engine.util.PbtdcFactory;
import com.btireland.talos.ethernet.engine.util.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class InterventionServiceTest {

    @Mock
    CerberusDataSyncMessageProducer dataSyncMessageProducer;
    @Mock
    OrderMapper orderMapper;
    @InjectMocks
    InterventionService interventionService;

    @BeforeEach
    public void setUp() {
        interventionService = new InterventionService(dataSyncMessageProducer, orderMapper);
    }
    @Test
    void testCreateIntervention() {
        Pbtdc order= PbtdcFactory.defaultPbtdc();
        InterventionDetails interventionDetails= InterventionDetails.builder().id(1l).createdAt(LocalDateTime.parse("2020-10-12T12:29:40.214777")).updatedAt(LocalDateTime.parse("2020-10-12T12:29:40.214777")).status(Status.OPEN).build();
        List<InterventionDetails> interventionDetailsList=new ArrayList<>();
        interventionDetailsList.add(interventionDetails);

        Mockito.when(orderMapper.toOrderDTO(Mockito.any(Pbtdc.class))).thenReturn(OrdersDTOFactory.defaultOrdersDTO());
        Mockito.when(orderMapper.toInterventionDetailsDTO(Mockito.any(InterventionDetails.class))).thenReturn(InterventionDetailsDTO.builder().id(1l).status(Status.OPEN).build());

        interventionService.createOrUpdateIntervention(order, interventionDetails);
        Assertions.assertEquals(order.getInterventionDetails(), interventionDetailsList);
    }

    @Test
    void testUpdateIntervention() {
        Pbtdc order= PbtdcFactory.defaultPbtdc();
        InterventionDetails interventionDetails1= InterventionDetails.builder().id(1l).createdAt(LocalDateTime.parse("2020-10-12T12:29:40.214777")).updatedAt(LocalDateTime.parse("2020-10-12T12:29:40.214777")).workflow("Activity_SupplierAcceptProcess").status(Status.CLOSED).build();

        List<InterventionDetails> interventionDetailsList=new ArrayList<>();
        interventionDetailsList.add(interventionDetails1);
        order.setInterventionDetails(interventionDetailsList);

        InterventionDetails interventionDetails = InterventionDetails.builder().workflow("Activity_SupplierAcceptProcess")
                .status(Status.CLOSED).build();

        Mockito.when(orderMapper.toOrderDTO(Mockito.any(Pbtdc.class))).thenReturn(OrdersDTOFactory.defaultOrdersDTO());
        Mockito.when(orderMapper.toInterventionDetailsDTO(Mockito.any(InterventionDetails.class))).thenReturn(InterventionDetailsDTO.builder().workflow("Activity_SupplierAcceptProcess")
                .status(Status.CLOSED).build());

        interventionService.createOrUpdateIntervention(order, interventionDetails);
        Assertions.assertEquals(order.getInterventionDetails().get(0).getWorkflow(), interventionDetails.getWorkflow());
        Assertions.assertEquals(order.getInterventionDetails().get(0).getStatus(), interventionDetails.getStatus());
    }

}
