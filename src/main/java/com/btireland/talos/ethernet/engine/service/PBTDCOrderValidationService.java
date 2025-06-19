package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.client.asset.ordermanager.PBTDCOrderDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.RejectDTO;
import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.exception.AuthenticationException;
import com.btireland.talos.ethernet.engine.exception.RequestValidationException;
import com.btireland.talos.ethernet.engine.soap.auth.AuthenticationHelper;
import com.btireland.talos.ethernet.engine.util.FaultString;
import com.btireland.talos.ethernet.engine.util.OrderNumberPatterns;
import com.btireland.talos.ethernet.engine.util.RejectCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@Transactional
public class PBTDCOrderValidationService {

    ApplicationConfiguration applicationConfiguration;

    AuthenticationHelper authenticationHelper;

    @Value("${application.patterns.order-number-pattern}")
    String orderNumberPatternStr;

    @Value("${application.patterns.eircode-pattern}")
    String eircodePatternStr;

    @Value("${application.patterns.obo-pattern}")
    String oboPatternStr;

    public PBTDCOrderValidationService(ApplicationConfiguration applicationConfiguration, AuthenticationHelper authenticationHelper) {
        this.applicationConfiguration = applicationConfiguration;
        this.authenticationHelper = authenticationHelper;
    }

    public RejectDTO validateOrder(PBTDCOrderDTO pfibOrderData) throws RequestValidationException, AuthenticationException {
        RejectDTO pfibRejectDTO = null;
        // validateOperatorDetails(pfibOrderData);
        pfibRejectDTO = validateOrderDetails(pfibOrderData);

        return pfibRejectDTO;

    }

    private void validateOperatorDetails(PBTDCOrderDTO pbtcOrderData) throws AuthenticationException {

        if (!(authenticationHelper.getOperator().getOperatorCode()
                .equalsIgnoreCase(pbtcOrderData.getOrders().getOperatorCode())
                && authenticationHelper.getOperator().getOperatorName()
                .equalsIgnoreCase(pbtcOrderData.getOrders().getOperatorName()))) {
            throw new AuthenticationException("WS05:Authentication Failed.");
        }


    }

    private RejectDTO validateOrderDetails(PBTDCOrderDTO pbtdcOrderData) throws RequestValidationException {
        validateOrderNumberPrefix(pbtdcOrderData);
        validateOBO(pbtdcOrderData);
        int rejectCode = 0;
        String rejectReason = null;
        if (!validateApplicationDate(pbtdcOrderData.getPbtdcs().getApplicationDate())) {
            rejectCode = RejectCode.CODE_3_APPL_DATE.getCode();
            rejectReason = RejectCode.CODE_3_APPL_DATE.getRejectReason();
        } else if (!validateOrderNumber(pbtdcOrderData.getOrders().getOrderNumber())) {
            rejectCode = RejectCode.CODE_3_ORDER_NUM.getCode();
            rejectReason = RejectCode.CODE_3_ORDER_NUM.getRejectReason();
        }
        return RejectDTO.builder().rejectCode(rejectCode).rejectReason(rejectReason).build();

    }

    private void validateOrderNumberPrefix(PBTDCOrderDTO pfibOrderData) throws RequestValidationException {
        Pattern orderNumberPattern = null;
        Matcher orderNumberMatcher = null;
        StringBuilder faultString = new StringBuilder();
        for (OrderNumberPatterns pattern : OrderNumberPatterns.values()) {
            orderNumberPattern = Pattern.compile(pattern.getPattern());
            orderNumberMatcher = orderNumberPattern.matcher(pfibOrderData.getOrders().getOrderNumber());
            if (orderNumberMatcher.find()) {
                faultString.append(FaultString.WS03.toString()).append(":").append(FaultString.WS03.getFaultString()).append(pattern.getErrorMessage());
                throw new RequestValidationException(faultString.toString());
            }
        }

    }

    private void validateOBO(PBTDCOrderDTO pfibOrderData) throws RequestValidationException {
        StringBuilder faultString = new StringBuilder();
        if(pfibOrderData.getOrders().getObo() != null && !pfibOrderData.getOrders().getObo().isBlank()) {
            Pattern oboPattern = Pattern.compile(oboPatternStr);
            Matcher oboMatcher = oboPattern.matcher(pfibOrderData.getOrders().getObo());
            if(!oboMatcher.matches()) {
                faultString.append(FaultString.WS03.toString()).append(":").append(FaultString.WS03.getFaultString()).append(" Invalid OBO.");
                throw new RequestValidationException(faultString.toString());
            }
        }

    }

    public static LocalDate parseDateWithMultipleFormats(String applicationDate, String[] formats) {
        for (String format : formats) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalDate.parse(applicationDate, formatter);
            } catch (DateTimeParseException e) {

            }
        }
        throw new IllegalArgumentException("Date string does not match any supported format.");

    }

    private boolean validateApplicationDate(String applicationDate) {
        String[] formats = {
                "yyyy-MM-dd",
                "dd/MM/yyyy",
        };

        try {
             parseDateWithMultipleFormats(applicationDate, formats);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }


    private boolean validateOrderNumber(String orderNumber) {
        if (StringUtils.isBlank(orderNumber))
            return false;
        Pattern orderNumberPattern = Pattern.compile(orderNumberPatternStr);
        Matcher orderNumberMatcher = orderNumberPattern.matcher(orderNumber);
        return orderNumberMatcher.matches();

    }


}
