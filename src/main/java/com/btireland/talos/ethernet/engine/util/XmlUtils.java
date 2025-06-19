package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.soap.notifications.Notification;
import com.btireland.talos.ethernet.engine.soap.notifications.ObjectFactory;
import org.apache.commons.io.IOUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.nio.charset.StandardCharsets;

public class XmlUtils {

    public static Notification unmarshalNotification(byte[] content) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Notification notification = ((JAXBElement<Notification>) unmarshaller.unmarshal(IOUtils.toInputStream(new String(content), StandardCharsets.UTF_8))).getValue();
        return notification;
    }
}
