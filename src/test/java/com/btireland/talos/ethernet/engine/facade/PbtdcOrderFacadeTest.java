package com.btireland.talos.ethernet.engine.facade;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.domain.InterventionDetails;
import com.btireland.talos.ethernet.engine.domain.Orders;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.dto.CloseInterventionDTO;
import com.btireland.talos.ethernet.engine.dto.InterventionDetailsDTO;
import com.btireland.talos.ethernet.engine.dto.OrdersDTO;
import com.btireland.talos.ethernet.engine.mq.CerberusDataSyncMessageProducer;
import com.btireland.talos.ethernet.engine.service.PbtdcOrdersPersistenceService;
import com.btireland.talos.ethernet.engine.util.Color;
import com.btireland.talos.ethernet.engine.util.OrdersDTOFactory;
import com.btireland.talos.ethernet.engine.util.PbtdcFactory;
import com.btireland.talos.ethernet.engine.util.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;

@UnitTest
@ExtendWith(MockitoExtension.class)
class PbtdcOrderFacadeTest {

    @Mock
    private PbtdcMapper pbtdcMapper;

    @Mock
    private PbtdcOrdersPersistenceService pbtdcOrdersPersistenceService;

    @Mock
    private PBTDCOrderMapper pbtdcOrderMapper;

    @Mock
    private CerberusDataSyncMessageProducer dataSyncMessageProducer;

    @InjectMocks
    private PBTDCOrderFacade pbtdcOrderFacade;

    @BeforeEach
    public void setUp() {
        Mockito.reset(pbtdcOrderMapper);
        Mockito.reset(pbtdcOrdersPersistenceService);
    }

    @Test
    @DisplayName("Test Get Interventions")
    void testGetInterventions(){
        InterventionDetails interventionDetails=InterventionDetails.builder().color(Color.RED).agent("SKY").status(Status.CLOSED).build();
        List<InterventionDetails> interventionDetailsList= new ArrayList<>();
        InterventionDetailsDTO interventionDetailsDTO = InterventionDetailsDTO.builder()
                .status(Status.CLOSED).build();
        interventionDetailsList.add(interventionDetails);
        Mockito.lenient().when(pbtdcMapper.toInterventionDetailsDTO(interventionDetails)).thenReturn(interventionDetailsDTO);
        Mockito.lenient().when(pbtdcOrdersPersistenceService.getInterventionDetails(1l,Status.CLOSED)).thenReturn(interventionDetailsList);
        List<InterventionDetailsDTO> actual=pbtdcOrderFacade.getInterventions(1l,"CLOSED");
        Assertions.assertThat(actual.get(0).getStatus().getLabel()).isEqualToIgnoringCase("CLOSED");

    }

    @Test
    @DisplayName("Test Update Interventions")
    void testUpdateInterventions(){
        Pbtdc order = PbtdcFactory.defaultPbtdc();
        OrdersDTO ordersDTO = OrdersDTOFactory.defaultOrdersDTO();
        InterventionDetails interventionDetails=InterventionDetails.builder().color(Color.RED).agent("SKY").status(Status.CLOSED).orders(Orders.builder().wagOrderId(1L).build()).build();
        CloseInterventionDTO interventionDetailsDTO = CloseInterventionDTO.builder().build();
        Mockito.when(pbtdcOrdersPersistenceService.findByInterventionId(1l)).thenReturn(interventionDetails);
        Mockito.when(pbtdcOrdersPersistenceService.findByOrderId(1L)).thenReturn(order);
        Mockito.when(pbtdcMapper.toOrderDTO(order)).thenReturn(ordersDTO);
        pbtdcOrderFacade.closeIntervention(1l,interventionDetailsDTO);
        Mockito.verify(pbtdcOrdersPersistenceService, times(1)).updateInterventionDetails(interventionDetails);
        Mockito.verify(dataSyncMessageProducer, times(1)).sendOrderData(ordersDTO);

    }



}
