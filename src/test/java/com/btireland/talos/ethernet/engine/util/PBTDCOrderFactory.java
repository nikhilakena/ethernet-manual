package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.soap.orders.*;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;

public class PBTDCOrderFactory {

    public static PBTDCOrder pbtdcOrder() throws SOAPException, JAXBException, IOException {
        SOAPMessage message = MessageFactory.newInstance().createMessage(null, new ClassPathResource("PBTDCOrder.xml", PBTDCOrderFactory.class).getInputStream());
        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        PBTDCOrder pbtdcOrder = ((JAXBElement<PBTDCOrder>) jaxbContext.createUnmarshaller().unmarshal(message.getSOAPBody().extractContentAsDocument())).getValue();
        return pbtdcOrder;
    }

    public static PBTDCOrderresponse pbtdcOrderResponse () {
        var pbtdcOrderResponse = new PBTDCOrderresponse();
        pbtdcOrderResponse.setORDERID(1L);
        var notification = new Notification();
        var notificationData = new NotificationData();
        notificationData.setORDERNUMBER("BT-PBTDC-123");
        notification.setNOTIFICATIONDATA(notificationData);
        pbtdcOrderResponse.setNOTIFICATION(notification);
        return pbtdcOrderResponse;
    }
}
