package com.btireland.talos.ethernet.engine.soap.interceptor;

import com.btireland.talos.ethernet.engine.soap.orders.ObjectFactory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.xml.transform.TransformerHelper;
import org.w3c.dom.*;

import javax.xml.bind.annotation.XmlSchema;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import java.util.stream.IntStream;

public class PayloadPrefixInterceptor extends TransformerHelper implements EndpointInterceptor {

    public static final String NAMESPACE = ObjectFactory.class.getPackage().getAnnotation(XmlSchema.class).namespace();
    public static final String XMLNS = "xmlns:";
    private static final String FAULT_LOCAL_NAME = "Fault";
    private static final String SOAP_ENV_NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";
    private static final String PREFERRED_PREFIX = "soap";
    private static final String DEFAULT_NS = "xmlns:SOAP-ENV";
    private static final String HEADER_LOCAL_NAME = "Header";
    private static final String BODY_LOCAL_NAME = "Body";


    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
        WebServiceMessage response = messageContext.getResponse();
        Source payloadSource = response.getPayloadSource();
        DOMResult result = new DOMResult();
        transform(payloadSource, result);
        removePrefix(result.getNode());
        transform(new DOMSource(result.getNode()), response.getPayloadResult());
        return true;
    }

    private void removePrefix(Node node) {
        if (node == null) {
            return;
        }

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            removeNamespaceDeclaration(node);
        }
        if (node.getPrefix() != null) {
            node.setPrefix(null);
        }
        NodeList childNodes = node.getChildNodes();
        if (childNodes != null) {
            IntStream.of(0, childNodes.getLength()).forEach(index -> removePrefix(childNodes.item(index)));
        }
        Node nextSibling = node.getNextSibling();
        if (nextSibling != null) {
            removePrefix(nextSibling);
        }
    }

    private void removeNamespaceDeclaration(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        IntStream.range(0, attributes.getLength()).forEach(index -> {
            Node attribute = attributes.item(index);
            if (StringUtils.startsWith(attribute.getNodeName(), XMLNS) &&
                    StringUtils.equals(attribute.getNodeValue(), NAMESPACE)) {
                attributes.removeNamedItemNS(attribute.getNamespaceURI(), attribute.getLocalName());
            }
        });
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
        SaajSoapMessage soapResponse = (SaajSoapMessage) messageContext.getResponse();
        alterSoapEnvelope(soapResponse);
        return false;
    }
    
    private void alterSoapEnvelope(SaajSoapMessage soapResponse) {
        Document doc = soapResponse.getDocument();
        Element rootElement = doc.getDocumentElement();
        rootElement.setPrefix(PREFERRED_PREFIX);
        // Remove default SOAP namespace
        rootElement.removeAttribute(DEFAULT_NS);
        NodeList headerNodes = doc.getElementsByTagNameNS(SOAP_ENV_NAMESPACE, HEADER_LOCAL_NAME);
        NodeList bodyNodes = doc.getElementsByTagNameNS(SOAP_ENV_NAMESPACE, BODY_LOCAL_NAME);
        NodeList faultNodes = doc.getElementsByTagNameNS(SOAP_ENV_NAMESPACE, FAULT_LOCAL_NAME);

        if (headerNodes.getLength() != 0) {
            Element headerElement = (Element) headerNodes.item(0);
            headerElement.setPrefix(PREFERRED_PREFIX);
        }
        // Change Body's SOAP namespace prefix.
        if (bodyNodes.getLength() != 0) {
            Element bodyElement = (Element) bodyNodes.item(0);
            bodyElement.setPrefix(PREFERRED_PREFIX);
        }
        // Change Fauts's SOAP namespace prefix.
        if (faultNodes.getLength() != 0) {
            Element faultElement = (Element) faultNodes.item(0);
            faultElement.setPrefix(PREFERRED_PREFIX);
        }
        String faultCode = faultNodes.item(0).getFirstChild().getTextContent();
        //Removing prefix of FaultCode
        faultNodes.item(0).getFirstChild().setTextContent(faultCode.substring(faultCode.indexOf(':') + 1));
    }


    @Override
    public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception {
        /*Added empty method as this class isnt abstract*/
    }
}