package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.exception.AuthenticationException;
import com.btireland.talos.ethernet.engine.exception.RequestValidationException;
import com.btireland.talos.ethernet.engine.soap.auth.AuthenticationHelper;
import com.btireland.talos.ethernet.engine.util.FaultString;
import com.btireland.talos.ethernet.engine.util.OrderNumberPatterns;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderValidationHelperService {

    private static final String SLASH_DAY_FIRST_DATE_FORMAT = "dd/MM/yyyy";
    private ApplicationConfiguration applicationConfiguration;
    private AuthenticationHelper authenticationHelper;
    private Pattern orderNumberPattern;
    private Pattern oboPattern;
    private Map<Pattern, String> orderNumberPrefixPatternMap;
    private Pattern projectKeyPattern;

    public OrderValidationHelperService(ApplicationConfiguration applicationConfiguration, AuthenticationHelper authenticationHelper) {
        this.applicationConfiguration = applicationConfiguration;
        this.authenticationHelper = authenticationHelper;
        initPatterns();
    }

    public void initPatterns() {
        orderNumberPattern = Pattern.compile(applicationConfiguration.getPatterns().getOrderNumberPattern());
        oboPattern = Pattern.compile(applicationConfiguration.getPatterns().getOboPattern());
        orderNumberPrefixPatternMap = Arrays.stream(OrderNumberPatterns.values()).collect(Collectors.toMap(prefixPattern -> Pattern.compile(prefixPattern.getPattern()), prefixPattern -> prefixPattern.getErrorMessage()));
        projectKeyPattern = Pattern.compile(applicationConfiguration.getPatterns().getProjectKeyPattern());
    }

    public void validateOperatorDetails(String operatorCode, String operatorName) throws AuthenticationException {
        if (!(authenticationHelper.getOperator().getOperatorCode()
                .equalsIgnoreCase(operatorCode)
                && authenticationHelper.getOperator().getOperatorName()
                .equalsIgnoreCase(operatorName))) {
            throw new AuthenticationException("WS05:Authentication Failed.");
        }
    }

    public void validateOrderNumberPrefix(String orderNumber) throws RequestValidationException {
        Matcher orderNumberMatcher = null;
        StringBuilder faultString = new StringBuilder();
        for (Pattern pattern : orderNumberPrefixPatternMap.keySet()) {
            orderNumberMatcher = pattern.matcher(orderNumber);
            if (orderNumberMatcher.find()) {
                faultString.append(FaultString.WS03.toString()).append(":").append(FaultString.WS03.getFaultString()).append(orderNumberPrefixPatternMap.get(pattern));
                throw new RequestValidationException(faultString.toString());
            }
        }

    }

    public void validateOBO(String obo) throws RequestValidationException {
        StringBuilder faultString = new StringBuilder();
        if (obo != null && !obo.isBlank()) {
            Matcher oboMatcher = oboPattern.matcher(obo);
            if (!oboMatcher.matches()) {
                faultString.append(FaultString.WS03.toString()).append(":").append(FaultString.WS03.getFaultString()).append(" Invalid OBO.");
                throw new RequestValidationException(faultString.toString());
            }
        }

    }

    public boolean validateApplicationDate(String applicationDate) {
        try {
            LocalDate.parse(applicationDate, DateTimeFormatter.ofPattern(SLASH_DAY_FIRST_DATE_FORMAT));
        } catch (DateTimeParseException exception) {
            return false;
        }
        return true;
    }

    public boolean validateOrderNumber(String orderNumber) {
        if (StringUtils.isBlank(orderNumber))
            return false;
        Matcher orderNumberMatcher = orderNumberPattern.matcher(orderNumber);
        return orderNumberMatcher.matches();

    }

    public boolean validateOboRequired(String oao,String obo){

        return !(StringUtils.isBlank(obo) && applicationConfiguration.getOaoWithObo().contains(oao));
    }

    public boolean validateOboNotRequired(String oao,String obo){
        return !(StringUtils.isNotBlank(obo) && !applicationConfiguration.getOaoWithObo().contains(oao));

    }

    public boolean validateProjectKey(String projectKey){
        if (StringUtils.isNotBlank(projectKey)) {
            Matcher projectKeyMatcher = projectKeyPattern.matcher(projectKey);
            return projectKeyMatcher.matches();
        }
        return true;
    }
}
