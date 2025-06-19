package com.btireland.talos.ethernet.engine.report;

import com.btireland.talos.ethernet.engine.exception.ReportGenerationException;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

import static com.btireland.talos.ethernet.engine.report.PBTDCReportExporterType.JASPER_EXPORTER_JSON;

@Component
public class PBTDCReportExporterFactory {
    public Exporter<?, ?, ?, ?> get(PBTDCReportExporterType type, ByteArrayOutputStream outputStream) throws ReportGenerationException {
        Exporter e;
        switch(type){
            case JASPER_EXPORTER_XLSX:
                e = getXlsxExporter();
                break;
            case JASPER_EXPORTER_XLS:
                e = getXlsExporter();
                break;
            case JASPER_EXPORTER_PDF:
                e = getPdfExporter();
                break;
            case JASPER_EXPORTER_JSON:
                e = getJsonExporter();
                break;
            default:
                throw new ReportGenerationException("Unknown exporter type : " + type);
        }

        // Jasper plain text output uses a different kind of exporter output than
        // richer formats such as PDF or XLS.

        if (type == JASPER_EXPORTER_JSON) {
            e.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
        } else {
            e.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        }
        return e;
    }

    private Exporter<?, ?, ?, ?> getXlsxExporter(){
        var xlsxExporter = new JRXlsxExporter();
        SimpleXlsxExporterConfiguration xlsxConfig = new SimpleXlsxExporterConfiguration();
        xlsxExporter.setConfiguration(xlsxConfig);

        return xlsxExporter;
    }
    private Exporter<?, ?, ?, ?> getXlsExporter(){
        var xlsExporter = new JRXlsExporter();
        SimpleXlsExporterConfiguration xlsConfig = new SimpleXlsExporterConfiguration();
        xlsExporter.setConfiguration(xlsConfig);

        return xlsExporter;
    }

    private Exporter<?, ?, ?, ?> getPdfExporter(){
        var pdfExporter = new JRPdfExporter();
        SimplePdfExporterConfiguration pdfConfig = new SimplePdfExporterConfiguration();
        pdfExporter.setConfiguration(pdfConfig);

        return pdfExporter;
    }

    private Exporter<?, ?, ?, ?> getJsonExporter(){
        var jsonExporter = new JsonMetadataExporter();
        var jsonExporterConfig = new SimpleJsonExporterConfiguration();
        jsonExporter.setConfiguration(jsonExporterConfig);

        return jsonExporter;
    }
}
