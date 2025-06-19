package com.btireland.talos.ethernet.engine.facade;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.util.PBTDCOrderDTOFactory;
import com.btireland.talos.ethernet.engine.util.PBTDCOrderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import java.io.IOException;

@UnitTest
@Slf4j
class PBTDCOrderMapperTest {
    private static final PBTDCOrderMapper mapper = Mappers.getMapper(PBTDCOrderMapper.class);;

    @Test
    void buildOrderManagerRequest() throws SOAPException, JAXBException, IOException {

        var order = PBTDCOrderFactory.pbtdcOrder();

        var expected = PBTDCOrderDTOFactory.defaultPBTDCOrderDTO();

        var actual = mapper.buildOrderManagerRequest(order, "sky");

        ObjectMapper objectMapper= new ObjectMapper();
        Assertions.assertThat(actual).isEqualTo(expected);

        log.debug(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(actual));
    }

    @Test
    void buildOrderManagerRequestWithEmptyOBO() throws SOAPException, JAXBException, IOException {

        var order = PBTDCOrderFactory.pbtdcOrder();
        order.getORDER().getORDERDATA().getORDERDETAILS().setOBO(null);

        var expected = PBTDCOrderDTOFactory.defaultPBTDCOrderDTOWithEmptyOBO();

        var actual = mapper.buildOrderManagerRequest(order, "sky");

        ObjectMapper objectMapper= new ObjectMapper();
        Assertions.assertThat(actual).isEqualTo(expected);

        log.debug(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(actual));
    }

    @Test
    void populateOrdersWithNotesSaving() throws SOAPException, JAXBException, IOException {
        var order = PBTDCOrderFactory.pbtdcOrder();
        var pbtdcOrderResponse = PBTDCOrderFactory.pbtdcOrderResponse();
        var actual = mapper.createPBTDCEntries(order, pbtdcOrderResponse, "sky" );
        Assertions.assertThat(actual.getNotes()).isEqualTo(order.getORDER().getORDERDATA().getCIRCUIT().getNOTES());
    }
}