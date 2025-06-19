package com.btireland.talos.ethernet.engine.service;

import static org.mockito.Mockito.verify;

import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.domain.Qbtdc;
import com.btireland.talos.ethernet.engine.dto.QBTDCEmailRequest;
import com.btireland.talos.ethernet.engine.util.QbtdcFactory;
import com.btireland.talos.ethernet.engine.util.QbtdcMailAllFactory;
import javax.mail.MessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class QbtdcManualMailServiceTest {

    @Mock
    QbtdcMailService qbtdcMailService;

    @Mock
    QbtdcOrdersPersistenceService qbtdcOrdersPersistenceService;

    @InjectMocks
    QBTDCManualMailService qbtdcManualMailService;

    @Test
    @DisplayName("Invoke Mails service to trigger email all")
    void testSendQBTDCEmail() throws MessagingException, TalosNotFoundException {

        Qbtdc orders = QbtdcFactory.defaultQbtdcOrder();
        orders.setLastNotificationType("C");
        QBTDCEmailRequest qbtdcEmailRequest = QbtdcMailAllFactory.defaultEmailRequest();
        Mockito.when(qbtdcOrdersPersistenceService.findByOrderNumberAndOao(Mockito.anyString(),Mockito.anyString())).thenReturn(orders);

        qbtdcManualMailService.sendQBTDCEmail(qbtdcEmailRequest);

        verify(qbtdcOrdersPersistenceService).findByOrderNumberAndOao("BT-QBTCDC-12345","sky");
        verify(qbtdcMailService).generateAndSendEmail(orders, qbtdcEmailRequest.getEmailRecipients(),
                                                      qbtdcEmailRequest.getNarrative());

    }

}
