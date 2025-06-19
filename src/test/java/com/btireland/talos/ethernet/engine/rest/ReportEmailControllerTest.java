package com.btireland.talos.ethernet.engine.rest;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.service.ReportEmailService;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;
import org.eclipse.jetty.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = ReportEmailController.class)
@ActiveProfiles("test")
@AutoConfigureJsonTesters
@IntegrationTest
public class ReportEmailControllerTest {
    private static final String BASE_URL = "/api/v1/dc/email/kci";

    @Captor
    ArgumentCaptor<Optional<LocalDate>> optionalLocalDateCaptor;
    @MockBean
    private ReportEmailService reportEmailService;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void initialiseRestAssuredMockMvcWebApplicationContext() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssuredMockMvc.config = RestAssuredMockMvc.config().encoderConfig(new EncoderConfig(StandardCharsets.UTF_8.displayName(), StandardCharsets.UTF_8.displayName()));
    }

    // Mock the report generator service.

    @Test
    @DisplayName("Test email generation returns a 202")
    void testEmailGenerationRequestReturns202Status() throws Exception {
        given()
                .contentType(ContentType.ANY)
                .when()
                .post(BASE_URL + "/generate?oao=sky&date=2022-01-01")
                .then()
                .assertThat()
                .statusCode(HttpStatus.ACCEPTED_202);


        verify(reportEmailService).generateAndSendEmail(Optional.of("sky"), Optional.of(LocalDate.parse("2022-01-01")));
    }
}
