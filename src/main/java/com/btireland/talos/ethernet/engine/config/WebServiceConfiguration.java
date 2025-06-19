package com.btireland.talos.ethernet.engine.config;

import com.btireland.talos.core.common.soap.interceptor.SoapEnvelopeLoggingPasswordObfuscateInterceptor;
import com.btireland.talos.ethernet.engine.soap.DetailSoapFaultAnnotationExceptionResolver;
import com.btireland.talos.ethernet.engine.soap.interceptor.PayloadPrefixInterceptor;
import com.btireland.talos.ethernet.engine.soap.interceptor.PayloadValidatingInterceptor;
import com.btireland.talos.ethernet.engine.soap.interceptor.RequestTraceInterceptor;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointExceptionResolver;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.SmartEndpointInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.server.SoapEndpointInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadRootSmartSoapEndpointInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.List;

import static com.btireland.talos.ethernet.engine.soap.SoapConstants.NAMESPACE_PAYLOAD_URI;

@EnableWs
@Configuration
public class WebServiceConfiguration extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        var servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);

        // we add the mapping /xsd/* so that the xsd schema bean defined in exampleSchema is available under the path /xsd/exampleWS-xsd-v1.xsd
        // by default, it will be available under /ws/exampleWS-xsd-v1.xsd. But inside the wsdl, the schema is imported under xsd/exampleWS-xsd-v1.xsd
        // so if we want that clients can download the wsdl from /ws/exampleWS-v1.wsdl and download the xsd from xsd/exampleWS-xsd-v1.xsd
        // we have to map 2 paths /xsd and /ws to this servlet
        return new ServletRegistrationBean<>(servlet, "/xsd/*", "/ws/*");
    }

    @Bean
    public EndpointExceptionResolver endpointExceptionResolver(Marshaller marshaller) {
        var exceptionResolver = new DetailSoapFaultAnnotationExceptionResolver(marshaller);
        exceptionResolver.setOrder(-1);
        return exceptionResolver;
    }

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        // add here the global interceptors, meaning the one you want to run for every endpoints
        interceptors.add(new PayloadPrefixInterceptor());
        interceptors.add(loggingInterceptor());
        interceptors.add(securityInterceptor());
        interceptors.add(requestTraceInterceptor());
    }

    public EndpointInterceptor loggingInterceptor() {
        return new SoapEnvelopeLoggingPasswordObfuscateInterceptor();
    }

    public EndpointInterceptor securityInterceptor() {
        var interceptor = new Wss4jSecurityInterceptor();
        interceptor.setSkipValidationIfNoHeaderPresent(true);
        interceptor.setValidationActions("NoSecurity");
        return interceptor;
    }

    /**
     * Definition of a validating interceptor to check client request against the XSD.
     * You can define another similar method if you have a second Web service with a different XSD.
     */

    @Bean(name = "PBTDCOrder")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema talosPBTDCOrderSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("PBTDCOrdersPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://wag.btireland.ie/WAG_WS/");
        wsdl11Definition.setSchema(talosPBTDCOrderSchema);
        return wsdl11Definition;
    }

    @Bean(name = "QBTDCOrder")
    public DefaultWsdl11Definition defaultQbtdcWsdl11Definition(XsdSchema talosQBTDCOrderSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("QBTDCOrdersPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://wag.btireland.ie/WAG_WS/");
        wsdl11Definition.setSchema(talosQBTDCOrderSchema);
        return wsdl11Definition;
    }
    /**
     * Definition of a validating interceptor to check client request against the XSD.
     * You can define another similar method if you have a second Web service with a different XSD.
     */
    @Bean()
    public SmartEndpointInterceptor pbtdcOrderWSValidatingInterceptor(Marshaller marshaller) {

        var validator = new PayloadValidatingInterceptor(marshaller);
        validator.setValidateRequest(true);
        validator.setValidateResponse(true);
        validator.setXsdSchema(pbdtcorderSchemaV1());

        // we validate only the messages for exampleWS namespace
        return new PayloadRootSmartSoapEndpointInterceptor(validator, NAMESPACE_PAYLOAD_URI, "PBTDC");
    }

    @Bean()
    public SmartEndpointInterceptor qbtdcOrderWSValidatingInterceptor(Marshaller marshaller) {

        var validator = new PayloadValidatingInterceptor(marshaller);
        validator.setValidateRequest(true);
        validator.setValidateResponse(true);
        validator.setXsdSchema(qbdtcorderSchemaV1());

        return new PayloadRootSmartSoapEndpointInterceptor(validator, NAMESPACE_PAYLOAD_URI, "QBTDC");
    }


    @Bean(name = "talosPBTDCOrderSchema")
    public XsdSchema pbdtcorderSchemaV1() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/pbtdcorder-xsd-v1.xsd"));
    }

    //Add Bean name so that xsd resource is available at this url http://localhost:8080/xsd/WSA_NotificationSchema.xsd
    //(name = "WSA_NotificationSchema")
    @Bean(name = "WSA_NotificationSchema")
    public XsdSchema talosWsaNotificationSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/PBTDCSyncResponseSchema-xsd-v1.xsd"));
    }

    @Bean(name = "talosQBTDCOrderSchema")
    public XsdSchema qbdtcorderSchemaV1() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/qbtdcorder-xsd-v1.xsd"));
    }

    @Bean(name = "Completion_NotificationSchema")
    public XsdSchema talosCompletionNotificationSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/QBTDCResponseSchema-xsd-v1.xsd"));
    }

    @Bean
    public SoapEndpointInterceptor requestTraceInterceptor() {
        return new RequestTraceInterceptor();
    }


}
