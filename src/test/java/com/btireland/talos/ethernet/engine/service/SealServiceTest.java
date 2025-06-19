package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.client.asset.seal.SealClient;
import com.btireland.talos.ethernet.engine.dto.EmailDTO;
import com.btireland.talos.ethernet.engine.facade.PbtdcMapper;
import com.btireland.talos.ethernet.engine.util.EmailDTOFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

@UnitTest
@ExtendWith(MockitoExtension.class)
class SealServiceTest {

    private static final String TALOS_COMPLETE = "talos complete";
    @Mock
    SealClient sealClient;

    @Mock
    PbtdcMapper pbtdcMapper;

    @InjectMocks
    SealService sealService;

    @Test
    @DisplayName("Pbtdc Order is sent to seal")
    void createPbtdcOrderForSeal() throws Exception {
        EmailDTO emailDTO = EmailDTOFactory.defaultEmailDTO();
        Mockito.when(sealClient.createOrder(emailDTO)).thenReturn("success");

        String expected = "success";
        String actual = sealService.createPbtdcOrderForSeal(emailDTO);
        Assertions.assertEquals(expected, actual);
        Mockito.verify(sealClient, Mockito.times(1)).createOrder(any(EmailDTO.class));
    }

}
