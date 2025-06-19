package com.btireland.talos.quote.facade.base.constant;

import java.time.format.DateTimeFormatter;

public final class QuoteManagerConstants {

  public static final DateTimeFormatter BT_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
  public static final DateTimeFormatter BT_DATE_WITHOUT_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  public static final String QBTDC_DATA_CONTRACT_NAME = "WAG";
  public static final String QBTDC_ORIGINATOR_CODE = "EXT";
  public static final String QBTDC_CONTRACT_VERSION = "1.0";
  public static final String GENERIC_ERROR_CODE = "Error";
  public static final String GENERIC_ERROR_REASON = "Unable to generate quote - Please try again later";
  public static final String QBTDC_TYPE = "QBTDC";
  public static final String QUOTE_ORDER_NOT_FOUND = "QUOTE_ORDER_NOT_FOUND";
  public static final String YES = "Y";
  public static final String NO = "N";
  public static final String DECIMAL_PATTERN = "#.##";
}
