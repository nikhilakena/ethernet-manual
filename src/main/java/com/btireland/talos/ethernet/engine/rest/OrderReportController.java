package com.btireland.talos.ethernet.engine.rest;

import com.btireland.talos.ethernet.engine.dto.ReportAvailabilityDTO;
import com.btireland.talos.ethernet.engine.service.JasperReportGeneratorService;
import com.btireland.talos.ethernet.engine.service.PBTDCReportGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@RestController
@Tag(name= "Report Processing Controller", description = "Controller to generate and retrieve reports")
@RequestMapping(value = {/* deprecated */"/api/v1/report", "/api/v1/dc/report/kci"})
public class OrderReportController {
    private static final String CONTENT_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String CONTENT_TYPE_XLS = "application/vnd.ms-excel";
    private final JasperReportGeneratorService reportGeneratorService;
    private final PBTDCReportGeneratorService pbtdcReportGeneratorService;

    public OrderReportController(
            JasperReportGeneratorService reportGeneratorService,
            PBTDCReportGeneratorService pbtdcReportGeneratorService) {
        this.reportGeneratorService = reportGeneratorService;
        this.pbtdcReportGeneratorService = pbtdcReportGeneratorService;
    }

    @Operation(summary = "trigger report generation", description = "trigger generation of a report")
    @PostMapping(path = "/generate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void generate(@RequestParam(value = "date", required = false)
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws Exception {
        pbtdcReportGeneratorService.generate(Optional.ofNullable(date));
    }

    @Operation(summary = "retrieve Excel 2003 report", description = "retrieve report in Excel 2003 or later format")
    @GetMapping(path = "/retrieve/{oao}", produces = CONTENT_TYPE_XLSX)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> retrieveExcel2003(
            @PathVariable("oao") String oao,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("internal") Boolean internal) throws Exception {
        return ResponseEntity.of(reportGeneratorService.generateExcel2003(date, oao, internal));
    }

    @Operation(summary = "retrieve Excel 97-03 report", description = "retrieve report in Excel 97-03 or later format")
    @GetMapping(path = "/retrieve/{oao}", produces = CONTENT_TYPE_XLS)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> retrieveExcel9703(
            @PathVariable("oao") String oao,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("internal") Boolean internal) throws Exception {
        return ResponseEntity.of(reportGeneratorService.generateExcel9703(date, oao, internal));
    }

    @Operation(summary = "retrieve PDF", description = "retrieve report in PDF format")
    @GetMapping(path = "/retrieve/{oao}", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> retrievePDF(
            @PathVariable("oao") String oao,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("internal") Boolean internal) throws Exception {
        return ResponseEntity.of(reportGeneratorService.generatePDF(date, oao, internal));
    }

    @Operation(summary = "retrieve JSON", description = "retrieve report in JSON format")
    @GetMapping(path = "/retrieve/{oao}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> retrieveJSON(
            @PathVariable("oao") String oao,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("internal") Boolean internal) throws Exception {

        return ResponseEntity.of(reportGeneratorService.generateJSON(date, oao, internal));
    }

    @Operation(summary = "retrieve report availability", description = "retrieve availability of report for all dates in a given range")
    @GetMapping(path = "/availability/{oao}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<ReportAvailabilityDTO> retrieveReportAvailability(
            @PathVariable("oao") @NotNull String oao,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate startDate,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate endDate
    ) throws Exception {
        return EntityModel.of(pbtdcReportGeneratorService.retrieveReportAvailability(startDate, endDate, oao));
    }


}
