package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.soap.orders.ObjectFactory;
import com.btireland.talos.ethernet.engine.soap.orders.QBTDCOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;

public class QBTDCOrderFactory {
    public static String asJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(object);
    }

    public static QBTDCOrder qbtdcOrder() throws SOAPException, JAXBException, IOException {
        SOAPMessage message = MessageFactory.newInstance().createMessage(null, new ClassPathResource("/data/QBTDCOrderEndpointTest/QBTDC-ValidOrder.xml", QBTDCOrderFactory.class).getInputStream());
        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        QBTDCOrder qbtdcOrder = ((JAXBElement<QBTDCOrder>) jaxbContext.createUnmarshaller().unmarshal(message.getSOAPBody().extractContentAsDocument())).getValue();
        return qbtdcOrder;
    }


}
