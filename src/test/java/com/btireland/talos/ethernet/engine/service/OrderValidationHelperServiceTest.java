package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.exception.AuthenticationException;
import com.btireland.talos.ethernet.engine.exception.RequestValidationException;
import com.btireland.talos.ethernet.engine.soap.auth.AuthenticationHelper;
import com.btireland.talos.ethernet.engine.soap.auth.OperatorDetails;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@UnitTest
@ExtendWith(MockitoExtension.class)
class OrderValidationHelperServiceTest {

    @Mock
    ApplicationConfiguration applicationConfiguration;

    @Mock
    AuthenticationHelper authenticationHelper;

    OrderValidationHelperService orderValidationHelperService;

    @BeforeEach
    public void setUp() {
        ApplicationConfiguration.Patterns patterns = new ApplicationConfiguration.Patterns();
        patterns.setOboPattern("\\b[\\w-]{1,32}\\b");
        patterns.setOrderNumberPattern("^(?=.*[A-Za-z0-9])[\\w_-]+$");
        patterns.setProjectKeyPattern("^[\\w-]+$");
        Mockito.when(applicationConfiguration.getPatterns()).thenReturn(patterns);
        orderValidationHelperService = new OrderValidationHelperService(applicationConfiguration, authenticationHelper);
    }

    @Test
    @DisplayName("Validate Invalid Application Date")
    void testValidateInvalidApplicationDate() {
        boolean isValid = orderValidationHelperService.validateApplicationDate("20-03-2020");
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Validate invalid Order Number")
    void testValidateInvalidOrderNumber() {
        boolean isValid = orderValidationHelperService.validateOrderNumber("KL-1:23");
        Assertions.assertThat(isValid).isFalse();

    }

    @Test
    @DisplayName("Validate invalid Order Number Prefix")
    void testValidateInvalidOrderNumberPrefix() {

        Throwable actual = Assertions.catchThrowable(() -> {
            orderValidationHelperService.validateOrderNumberPrefix("BT-789");
        });
        Assertions.assertThat(actual).isInstanceOf(RequestValidationException.class).hasMessageContaining("Invalid Order Number.");

    }

    @Test
    @DisplayName("Valid invalid OBO")
    void testCreateQBTDCOrderInvalidOBO() {
        Throwable actual = Assertions.catchThrowable(() -> {
            orderValidationHelperService.validateOBO("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        });
        Assertions.assertThat(actual).isInstanceOf(RequestValidationException.class).hasMessageContaining("Invalid OBO.");
    }

    @Test
    @DisplayName("Validate invalid Operator")
    void testValidateOperatorDetails() throws AuthenticationException {
        Mockito.when(authenticationHelper.getOperator()).thenReturn(OperatorDetails.builder().operatorOAO("sky").operatorCode("83757").operatorName("SKY").build());
        Throwable actual = Assertions.catchThrowable(() -> {
            orderValidationHelperService.validateOperatorDetails("SKY", "83758");
        });
        Assertions.assertThat(actual).isInstanceOf(AuthenticationException.class).hasMessageContaining("WS05:Authentication Failed.");
    }

    @Test
    @DisplayName("Validate missing OBO")
    void testValidateMissingOBO(){
        Mockito.when(applicationConfiguration.getOaoWithObo()).thenReturn(List.of("BT Pricing","LMX"));
        boolean isValid = orderValidationHelperService.validateOboRequired("BT Pricing", null);

        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Validate if OBO is not required ")
    void testValidateOBONotRequired(){
        boolean isValid = orderValidationHelperService.validateOboNotRequired("sky", "xyz");
        Assertions.assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Validate incorrect project key")
    void testValidateProjectKey(){
        boolean isValid = orderValidationHelperService.validateProjectKey("XX#XX");
        Assertions.assertThat(isValid).isFalse();
    }

}
