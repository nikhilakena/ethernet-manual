package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.domain.InterventionDetails;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.repository.InterventionDetailsRepository;
import com.btireland.talos.ethernet.engine.repository.OrderRepository;
import com.btireland.talos.ethernet.engine.util.Color;
import com.btireland.talos.ethernet.engine.util.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;

@UnitTest
@ExtendWith(MockitoExtension.class)
class PbtdcOrdersPersistenceServiceTest {

    @Mock
    private OrderRepository<Pbtdc> ordersRepository;
    @Mock
    private InterventionDetailsRepository interventionDetailsRepository;

    @Mock
    private ApplicationConfiguration applicationConfiguration;

    @InjectMocks
    private PbtdcOrdersPersistenceService ordersPersistenceService;

    @BeforeEach
    void setup(){
        ordersPersistenceService=new  PbtdcOrdersPersistenceService(ordersRepository, applicationConfiguration, interventionDetailsRepository);
    }

    @Test
    void testUpdateInterventionDetails(){
        InterventionDetails interventionDetails=InterventionDetails.builder().color(Color.RED).agent("SKY").status(Status.CLOSED).build();
        ordersPersistenceService.updateInterventionDetails(interventionDetails);
        Mockito.verify(interventionDetailsRepository, times(1)).save(interventionDetails);

    }

    @Test
    void testFindByInterventionId(){
        InterventionDetails expected=InterventionDetails.builder().color(Color.RED).agent("SKY").status(Status.OPEN).build();
        Mockito.lenient().when(interventionDetailsRepository.findByInterventionId(1l)).thenReturn(expected);
        InterventionDetails actual=ordersPersistenceService.findByInterventionId(1l);
        Assertions.assertEquals(expected,actual);
    }
}
