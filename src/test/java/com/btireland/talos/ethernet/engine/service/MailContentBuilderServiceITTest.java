package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.dto.EmailDTO;
import com.btireland.talos.ethernet.engine.dto.OrdersDTO;
import com.btireland.talos.ethernet.engine.util.EmailDTOFactory;
import com.btireland.talos.ethernet.engine.util.OrdersDTOFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@IntegrationTest
@SpringBootTest
@ActiveProfiles("test")
class MailContentBuilderServiceITTest {

    @Autowired
    MailContentBuilderService mailContentBuilderService;

    @Test
    @DisplayName("Build Email Text for WEC")
    void buildEmailTextforWEC() throws Exception {
        OrdersDTO orderDTO = OrdersDTOFactory.defaultOrdersDTO();
        EmailDTO emailDTO = EmailDTOFactory.defaultEmailDTO();

        EmailDTO expected = emailDTO;
        EmailDTO actual = mailContentBuilderService.build(orderDTO);
        Assertions.assertEquals(replaceCRLF(expected), replaceCRLF(actual));
    }

    @Test
    @DisplayName("Build Email Text For WIC")
    void buildEmailTextForWIC() throws Exception {
        OrdersDTO orderDTO = OrdersDTOFactory.defaultWICOrdersDTO();
        EmailDTO emailDTO = EmailDTOFactory.defaultEmailDTOForWIC();

        EmailDTO expected = emailDTO;
        EmailDTO actual = mailContentBuilderService.build(orderDTO);
        Assertions.assertEquals(replaceCRLF(expected), replaceCRLF(actual));
    }

    private EmailDTO replaceCRLF(EmailDTO emailDTO) {
        emailDTO.setEmailText(emailDTO.getEmailText().replace("\r\n", "\n"));
        return emailDTO;
    }

}
