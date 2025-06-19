package com.btireland.talos.quote.facade.service.api;

import static com.btireland.talos.quote.facade.base.constant.FeatureFlagConstants.QUOTE_FACADE_ENABLED;
import com.btireland.talos.core.aop.annotation.LoggedApiMethod;
import com.btireland.talos.core.common.rest.exception.checked.TalosInternalErrorException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.ethernet.engine.dto.QBTDCEmailRequest;
import com.btireland.talos.ethernet.engine.service.QBTDCManualMailService;
import com.btireland.talos.quote.facade.process.processor.QuoteEmailProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QuoteEmailAPI {

  private final QBTDCManualMailService qbtdcManualMailService;

  private final QuoteEmailProcessor quoteEmailProcessor;

  @Value("${"+QUOTE_FACADE_ENABLED+"}")
  private boolean quoteFacadeEnabled;

  public QuoteEmailAPI(QBTDCManualMailService qbtdcManualMailService,
      QuoteEmailProcessor quoteEmailProcessor) {
    this.qbtdcManualMailService = qbtdcManualMailService;
    this.quoteEmailProcessor = quoteEmailProcessor;
  }

  @LoggedApiMethod
  public void sendQBTDCEmail(QBTDCEmailRequest qbtdcEmailRequest) throws TalosNotFoundException, TalosInternalErrorException {
    if (quoteFacadeEnabled) {
      quoteEmailProcessor.sendQuoteMail(qbtdcEmailRequest);
    } else {
      qbtdcManualMailService.sendQBTDCEmail(qbtdcEmailRequest);
    }
  }
}
