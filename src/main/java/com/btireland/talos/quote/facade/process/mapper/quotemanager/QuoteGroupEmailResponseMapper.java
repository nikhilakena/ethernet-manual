package com.btireland.talos.quote.facade.process.mapper.quotemanager;

import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.DELAYED;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.EXISTING;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.GB;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.MB;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.ONE;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.SLA;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.SPACE;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.YEAR;
import static com.btireland.talos.quote.facade.base.constant.QuoteEmailConstants.YEARS;

import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.ethernet.engine.dto.QuoteItemEmailDTO;
import com.btireland.talos.ethernet.engine.enums.CosType;
import com.btireland.talos.ethernet.engine.enums.DeliveryType;
import com.btireland.talos.ethernet.engine.enums.FrequencyType;
import com.btireland.talos.ethernet.engine.enums.Sla;
import com.btireland.talos.quote.facade.base.enumeration.internal.PriceType;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteOrderMapStatus;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.quotemanager.response.QuotePriceResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.email.QuoteEmailResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.email.QuoteGroupEmailResponse;
import com.btireland.talos.quote.facade.process.helper.QuoteHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class QuoteGroupEmailResponseMapper {

  /**
   * Method for mapping QuoteGroupEmailResponse to List of QuoteItemEmailDTO
   *
   * @param quoteGroupEmailResponse QuoteGroupEmailResponse received from Quote Manager
   * @param quoteOrderMap           QuoteOrderMap object containing orderNumber, GroupId
   * @param handoverMap             handOverMap from Application Configuration Object
   * @return List of QuoteItemEmailDTO
   */
  public static List<QuoteItemEmailDTO> createQuoteItemEmailList(QuoteGroupEmailResponse quoteGroupEmailResponse,
      QuoteOrderMapEntity quoteOrderMap, Map<String, String> handoverMap) throws TalosNotFoundException {
    List<QuoteItemEmailDTO> quoteItemEmailDTOList = new ArrayList<>();
    for (QuoteEmailResponse quoteEmailResponse : quoteGroupEmailResponse.getQuoteEmails()) {
      QuoteItemEmailDTO quoteItemEmail = createQuoteItemEmail(quoteGroupEmailResponse, quoteEmailResponse,
                                                              quoteOrderMap,
                                                              handoverMap);
      quoteItemEmailDTOList.add(quoteItemEmail);
    }
    return quoteItemEmailDTOList;
  }

  /**
   * Method for mapping QuoteEmailResponse to QuoteItemEmailDTO
   *
   * @param quoteGroupEmailResponse QuoteGroupEmailResponse received from Quote Manager
   * @param quoteEmailResponse      QuoteEmailResppnse to be mapped
   * @param quoteOrderMap           QuoteOrderMap object containing orderNumber, GroupId
   * @param handoverMap             handOverMap from Application Configuration Object
   * @return QuoteItemEmailDTO
   */
  static QuoteItemEmailDTO createQuoteItemEmail(QuoteGroupEmailResponse quoteGroupEmailResponse,
      QuoteEmailResponse quoteEmailResponse, QuoteOrderMapEntity quoteOrderMap, Map<String, String> handoverMap) throws TalosNotFoundException {
    QuoteItemEmailDTO quoteItemEmail = QuoteItemEmailDTO.Builder.newQuoteItemEmailDTOBuilder()
        .withAEndAddress(quoteEmailResponse.getaEndResponse().getAddress())
        .withAEndEircode(quoteEmailResponse.getaEndResponse().getEircode())
        .withBandwidth(QuoteHelper.formatBandwidth(quoteEmailResponse.getaEndResponse().getBandwidth()))
        .withHandoff(quoteEmailResponse.getbEndHandOverNode())
        .withIpRange(Objects.toString(quoteEmailResponse.getLogicalResponse().getIpRange(), null))
        .withLogicalLinkBandwidth(QuoteHelper.formatBandwidth(quoteEmailResponse.getLogicalResponse().getBandwidth()))
        .withLogicalLinkProfile(quoteEmailResponse.getLogicalResponse().getProfile().name())
        .withNonRecurringPrice(getPriceByType(quoteEmailResponse.getQuotePriceResponses(), PriceType.NON_RECURRING))
        .withRecurringPrice(getPriceByType(quoteEmailResponse.getQuotePriceResponses(), PriceType.RECURRING))
        .withRecurringFrequency(getRecurringFrequency(quoteEmailResponse.getQuotePriceResponses()))
        .withServiceClass(quoteGroupEmailResponse.getServiceClass().name())
        .withConnectionType(quoteEmailResponse.getConnectionType() != null ? quoteEmailResponse.getConnectionType().getPrompt() : null)
        .withTerm(Objects.toString(quoteEmailResponse.getTerm(), null))
        .withQuoteId(QuoteHelper.fetchWagQuoteItemId(quoteOrderMap, quoteEmailResponse.getQuoteId()))
        .withGroup(quoteEmailResponse.getGroup())
        .withStatus(quoteEmailResponse.getQuoteState().getValue())
        .build();

    if (quoteEmailResponse.getaEndResponse().getTargetAccessSupplier() != null) {
      quoteItemEmail
          .setAEndTargetAccessSupplier(quoteEmailResponse.getaEndResponse().getTargetAccessSupplier().getValue());
    }
    if (quoteEmailResponse.getaEndResponse().getSla() != null) {
      quoteItemEmail.setSla(quoteEmailResponse.getaEndResponse().getSla().name());
    }

    if (quoteOrderMap.getStatus() == QuoteOrderMapStatus.DELAY) {
      quoteItemEmail.setStatus(DELAYED);
      quoteItemEmail.setReason(quoteEmailResponse.getDelayReason());
      quoteItemEmail.setRecurringPrice(null);
      quoteItemEmail.setNonRecurringPrice(null);
    } else {
      if (quoteOrderMap.getSyncFlag() && quoteEmailResponse.getRejectionDetails() != null) {
        quoteItemEmail.setReason(quoteEmailResponse.getRejectionDetails().getRejectReason());
      } else {
        quoteItemEmail.setReason(quoteEmailResponse.getNotes());
      }
    }

    formatQuoteItemEmailFields(quoteItemEmail, handoverMap);

    return quoteItemEmail;
  }

  /**
   * Method for formatting QuoteItemEmail object fields for user-friendly captions
   *
   * @param quoteItemEmail QuoteItemEmail Object to be formatted
   * @param handoverMap    handoverMap for user-friendly handover name
   */
  static void formatQuoteItemEmailFields(QuoteItemEmailDTO quoteItemEmail, Map<String, String> handoverMap) {
    quoteItemEmail.setTerm(quoteItemEmail.getTerm().equals(ONE) ? quoteItemEmail.getTerm() + SPACE + YEAR
        : quoteItemEmail.getTerm() + SPACE + YEARS);

    if (quoteItemEmail.getBandwidth() == null) {
      quoteItemEmail.setBandwidth(EXISTING);
      quoteItemEmail.setSla(null);
    } else {
      quoteItemEmail.setBandwidth(quoteItemEmail.getBandwidth() + SPACE + GB);
      if(quoteItemEmail.getSla() != null) {
        quoteItemEmail.setSla(Sla.getDisplayNameByCode(quoteItemEmail.getSla()) + SPACE + SLA);
      }
    }

    quoteItemEmail
        .setAEndTargetAccessSupplier(DeliveryType.getDisplayNameByCode(quoteItemEmail.getAEndTargetAccessSupplier()));

    quoteItemEmail.setHandoff(handoverMap.get(quoteItemEmail.getHandoff()));

    if (quoteItemEmail.getLogicalLinkBandwidth() != null) {
      quoteItemEmail.setLogicalLinkBandwidth(quoteItemEmail.getLogicalLinkBandwidth() + SPACE + MB);
    }

    if (quoteItemEmail.getLogicalLinkProfile() != null) {
      quoteItemEmail.setLogicalLinkProfile(CosType.getDisplayNameByCode(quoteItemEmail.getLogicalLinkProfile()));
    }

    if (quoteItemEmail.getRecurringFrequency() != null) {
      quoteItemEmail.setRecurringFrequency(FrequencyType.getDisplayNameByCode(quoteItemEmail.getRecurringFrequency()));
    }
  }

  /**
   * Method for getting the Price using the PriceType from the List of QuotePriceResponse
   *
   * @param quotePriceResponses QuotePriceResponses of the Quote
   * @param priceType           PriceType for which we are looking price
   * @return String price
   */
  static String getPriceByType(List<QuotePriceResponse> quotePriceResponses, PriceType priceType) {
    Optional<QuotePriceResponse> quotePrice =  quotePriceResponses
        .stream()
        .filter(quotePriceResponse -> quotePriceResponse.getPriceType() == priceType)
        .findFirst();

    return quotePrice.map(QuotePriceResponse::getQuotePrice).orElse(null);
  }

  /**
   * Method for getting the recurring frequency
   *
   * @param quotePriceResponses QuotePriceResponses of the quote
   * @return String recurringFrequency
   */
  static String getRecurringFrequency(List<QuotePriceResponse> quotePriceResponses) {
    Optional<QuotePriceResponse> quotePrice =  quotePriceResponses
        .stream()
        .filter(quotePriceResponse -> quotePriceResponse.getPriceType() == PriceType.RECURRING)
        .findFirst();

    return quotePrice.map(quotePriceResponse -> quotePriceResponse.getRecurringChargePeriod()
        .name())
        .orElse(null);
  }

}
