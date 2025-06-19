package com.btireland.talos.ethernet.engine.exception;

import com.btireland.talos.core.common.rest.exception.ApiError;
import com.btireland.talos.core.common.test.tag.UnitTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Arrays;

@UnitTest
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    public void setUp(){
        var errorProperties = buildErrorProperties();
        exceptionHandler = buildGlobalExceptionHandler(errorProperties);
    }

    ErrorProperties buildErrorProperties(){
        var errorProperties = new ErrorProperties();
        errorProperties.setIncludeStacktrace(ErrorProperties.IncludeAttribute.NEVER);
        errorProperties.setIncludeBindingErrors(ErrorProperties.IncludeAttribute.ALWAYS);
        errorProperties.setIncludeException(true);
        errorProperties.setIncludeMessage(ErrorProperties.IncludeAttribute.ALWAYS);
        return errorProperties;
    }

    GlobalExceptionHandler buildGlobalExceptionHandler(ErrorProperties errorProperties){
        var serverProperties = new ServerProperties();
        ReflectionTestUtils.setField(serverProperties, "error", errorProperties);
        return new GlobalExceptionHandler(serverProperties);
    }

    // we need a custom assert as sub errors can be in any orders and a regular equals can’t deal with that
    void assertThatApiErrorEquals(ResponseEntity<?> actual, ResponseEntity<?> expected){
        Assertions.assertThat(actual).isEqualToIgnoringGivenFields(expected, "body");
        var actualError = (ApiError) actual.getBody();
        var expectedError = (ApiError) expected.getBody();
        Assertions.assertThat(actualError).isEqualToIgnoringGivenFields(expectedError, "timestamp", "errors");
        if (actualError != null && expectedError != null){
            if (actualError.getErrors() == null){
                Assertions.assertThat(expectedError.getErrors()).isNull();
            } else{
                Assertions.assertThat(actualError.getErrors()).containsExactlyInAnyOrderElementsOf(expectedError.getErrors());
            }
        }
    }

    @Test
    @DisplayName("check that DataIntegrityViolationException is translated into 400 HTTP code")
    void handleDataIntegrityViolationException() {
        var expected = ResponseEntity.status(400).headers(new HttpHeaders())
                .body(ApiError.builder()
                        .status(400).httpStatus(HttpStatus.BAD_REQUEST).error("Bad Request").message("primary key violated")
                        .exception(DataIntegrityViolationException.class.getName()).path("/api/v1/exampleorders")
                        .build());
        var webRequest = new ServletWebRequest(new MockHttpServletRequest("POST", "/api/v1/exampleorders"));
        var exception = new DataIntegrityViolationException("primary key violated");

        var actual = exceptionHandler.handleDataIntegrityViolationException(webRequest, exception);
        assertThatApiErrorEquals(actual, expected);
    }

    // build a complete Api error object with every fields populated
    ApiError buildApiError(){
        return ApiError.builder()
                .status(400).httpStatus(HttpStatus.BAD_REQUEST).error("Bad Request").message("Validation failed")
                .errors(Arrays.asList(
                        ApiError.ApiSubError.builder().message("externalReference: must not be empty").build(),
                        ApiError.ApiSubError.builder().message("id: must be an integer").build()
                ))
                .exception(MethodArgumentNotValidException.class.getName()).path("/api/v1/exampleorders")
                .build();
    }
}