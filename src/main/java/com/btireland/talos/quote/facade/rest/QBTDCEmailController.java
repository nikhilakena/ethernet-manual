package com.btireland.talos.quote.facade.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.btireland.talos.core.common.rest.exception.checked.TalosInternalErrorException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.ethernet.engine.dto.QBTDCEmailRequest;
import com.btireland.talos.ethernet.engine.exception.BadRequestException;
import com.btireland.talos.quote.facade.base.constant.RestMapping;
import com.btireland.talos.quote.facade.service.api.QuoteEmailAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.ws.rs.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "QBTDC Emailing Controller", description = "Controller to generate email for quotes")
@RequestMapping(value = RestMapping.EMAIL_BASE_PATH)
public class QBTDCEmailController {

  private final QuoteEmailAPI quoteEmailAPI;

  public QBTDCEmailController(QuoteEmailAPI quoteEmailAPI) {
    this.quoteEmailAPI = quoteEmailAPI;
  }

  @Operation(summary = "trigger email generation for QBTDC", description = "trigger email generation for QBTDC " +
      "quotes",
      responses = {
          @ApiResponse(responseCode = "500", description = "Technical error while processing the request"),
          @ApiResponse(responseCode = "404", description = "Quote Not Found"),
          @ApiResponse(responseCode = "202", description = "Successfully processed the quote request")})
  @PostMapping(path = "/send", consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void sendQBTDCEmail(@RequestBody @Valid QBTDCEmailRequest emailRequest) throws TalosNotFoundException, TalosInternalErrorException {
    quoteEmailAPI.sendQBTDCEmail(emailRequest);
  }
}
