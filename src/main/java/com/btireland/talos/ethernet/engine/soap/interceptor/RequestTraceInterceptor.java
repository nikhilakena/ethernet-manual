package com.btireland.talos.ethernet.engine.soap.interceptor;

import com.btireland.talos.ethernet.engine.soap.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.SoapEndpointInterceptor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

@Slf4j
public class RequestTraceInterceptor implements SoapEndpointInterceptor {

    /* The Pact broker imports the public Xerces parser during test invocation, which overrides the internal
       JDK parser. Xerces does not support the ACCESS_EXTERNAL_DTD/SCHEMA attributes, so tests break.

       To avoid this problem, when initializing the DocumentBuilderFactory we can force it to use
       the internal parser.
     */
    private static final String xercesInternalDocumentBuilderFactoryImpl =
            "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl";
    @Autowired
    private RequestContext requestContext;

    private DocumentBuilderFactory dbf;

    public RequestTraceInterceptor(){
        this.dbf = DocumentBuilderFactory.newInstance(xercesInternalDocumentBuilderFactoryImpl, null);
        dbf.setNamespaceAware(true);
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    }
    @Override
    public boolean understands(SoapHeaderElement soapHeaderElement) {
        /*This returns true to the call from org.springframework.ws.soap.server.SoapMessageDispatcher
         that checks to see if an interceptor is configured to understand mustUnderstand in SOAPHeader*/
        return true;
    }

    @Override
    public boolean handleRequest(MessageContext messageContext, Object o) throws Exception {
        String payload;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            messageContext.getRequest().writeTo(buffer);
            payload = buffer.toString(java.nio.charset.StandardCharsets.UTF_8.name());

        } catch (IOException e) {
            throw new WebServiceClientException("Can not write the SOAP request into the out stream", e) {
                private static final long serialVersionUID = -7118480620416458069L;
            };
        }

        Document doc = obfuscateUsernamePassword(payload);

        String obfuscatedXMLString = transformXMLToString(doc);

        requestContext.setOriginalXMLRequest(obfuscatedXMLString);

        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object o) throws Exception {
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object o) throws Exception {
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object o, Exception e) throws Exception {
        /*Added empty method as this class isnt abstract*/
    }

    private Document obfuscateUsernamePassword(String payload) {
        Document doc;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();

            doc = db.parse(new InputSource(new StringReader(payload)));

            Node usernameToken = doc.getDocumentElement().getElementsByTagNameNS("*", "UsernameToken").item(0);
            NodeList children = usernameToken.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeName().contains("Username") || children.item(i).getNodeName().contains("Password")) {
                    children.item(i).setTextContent("****");
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new WebServiceClientException("Unable to parse XML request", e) {
                private static final long serialVersionUID = -7118480620416458069L;
            };
        }
        return doc;
    }

    private String transformXMLToString(Document doc) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            StringWriter sw = new StringWriter();
            trans.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (TransformerException e) {
            throw new WebServiceClientException("Unable to parse XML to String", e) {
                private static final long serialVersionUID = -7118480620416458069L;
            };
        }
    }
}
