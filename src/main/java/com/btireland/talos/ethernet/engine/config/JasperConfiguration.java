package com.btireland.talos.ethernet.engine.config;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JasperConfiguration {
    private String  jasperReport;

    private Map<String, Object> jasperFillParameters;

    public JasperConfiguration(@Value("classpath:jasper/BT_Logo_White.png") Resource btLogoResource,
                                     @Value("classpath:jasper/KCI_report.jrxml") Resource jasperReportResource) throws IOException {
        this.jasperReport = new String (jasperReportResource.getInputStream().readAllBytes());

        jasperFillParameters = new HashMap<>();

        jasperFillParameters.put("btlogo", ImageIO.read(btLogoResource.getInputStream()));
    }

    public Map<String, Object> getJasperFillParameters() {
        return this.jasperFillParameters;
    }

    public InputStream getJasperReport() throws IOException {
        return new ByteArrayInputStream(jasperReport.getBytes());
    }

    @Bean
    JasperFillManager getJasperFillManager(){
        return JasperFillManager.getInstance(DefaultJasperReportsContext.getInstance());
    }

    @Bean
    JasperCompileManager getJasperCompileManager(){
        return JasperCompileManager.getInstance(DefaultJasperReportsContext.getInstance());
    }
}
