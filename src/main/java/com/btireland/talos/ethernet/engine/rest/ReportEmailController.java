package com.btireland.talos.ethernet.engine.rest;

import com.btireland.talos.ethernet.engine.service.ReportEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@RestController
@Tag(name= "Report Emailing Controller", description = "Controller to generate and email reports to sharepoint")
@RequestMapping(value = "/api/v1/dc/email/kci")
public class ReportEmailController {
    private final ReportEmailService reportEmailService;

    public ReportEmailController(
            ReportEmailService reportEmailService) {
        this.reportEmailService = reportEmailService;
    }

    @Operation(summary = "trigger email generation", description = "trigger generation of a email")
    @PostMapping(path = "/generate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void generateAndSendEmail(@RequestParam(value = "oao", required = false) String oao, @RequestParam(value = "date", required = false)
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws Exception {
        reportEmailService.generateAndSendEmail(Optional.ofNullable(oao), Optional.ofNullable(date));
    }
}
