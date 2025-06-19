package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.dto.EmailDTO;
import com.btireland.talos.ethernet.engine.dto.OrdersDTO;
import com.btireland.talos.ethernet.engine.util.EmailDTOFactory;
import com.btireland.talos.ethernet.engine.util.OrdersDTOFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@UnitTest
@ExtendWith(MockitoExtension.class)
class MailContentBuilderServiceTest {

    @Mock
    TemplateEngine templateEngine;


    @InjectMocks
    MailContentBuilderService mailContentBuilderService;

    @Test
    @DisplayName("Build Email Text For WEC")
    void buildEmailTextForWEC() throws Exception {
        OrdersDTO orderDTO = OrdersDTOFactory.defaultOrdersDTO();
        EmailDTO emailDTO = EmailDTOFactory.defaultEmailDTO();
        Mockito.when(templateEngine.process(Mockito.any(String.class), Mockito.any(Context.class))).thenReturn(emailDTO.getEmailText());

        EmailDTO expected = emailDTO;
        EmailDTO actual = mailContentBuilderService.build(orderDTO);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Build Email Text For WIC")
    void buildEmailTextForWIC() throws Exception {
        OrdersDTO orderDTO = OrdersDTOFactory.defaultWICOrdersDTO();
        EmailDTO emailDTO = EmailDTOFactory.defaultEmailDTOForWIC();
        Mockito.when(templateEngine.process(Mockito.any(String.class), Mockito.any(Context.class))).thenReturn(emailDTO.getEmailText());

        EmailDTO expected = emailDTO;
        EmailDTO actual = mailContentBuilderService.build(orderDTO);
        Assertions.assertEquals(expected, actual);
    }

}
