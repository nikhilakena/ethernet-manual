package com.btireland.talos.quote.facade.rest;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.dto.QBTDCEmailRequest;
import com.btireland.talos.quote.facade.service.api.QuoteEmailAPI;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.WebApplicationContext;
@WebMvcTest(controllers = QBTDCEmailController.class)
@ActiveProfiles("test")
@AutoConfigureJsonTesters
@IntegrationTest
public class QBTDCEmailControllerTest {
    private static final String BASE_URL = "/api/v1/dc/email/qbtdc";
    private static final String BASE_FOLDER = "/data/QBTDCEmailControllerTest/";

    @Autowired
    private WebApplicationContext webApplicationContext;

   @MockBean
   QuoteEmailAPI quoteEmailAPI;

    @BeforeEach
    public void initialiseRestAssuredMockMvcWebApplicationContext() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssuredMockMvc.config = RestAssuredMockMvc.config()
                .encoderConfig(new EncoderConfig(StandardCharsets.UTF_8.displayName(),
                                                 StandardCharsets.UTF_8.displayName()));
    }


    @Test
    @DisplayName("Test qbtdc email generation returns a 202")
    void testEmailGeneration_whenRequestIsReceived_Returns202Status() throws Exception {
        ReflectionTestUtils.setField(quoteEmailAPI, "quoteFacadeEnabled", false);
        given()
                .contentType(ContentType.JSON)
                .body(IOUtils.toString(new ClassPathResource(BASE_FOLDER + "EmailRequest.json",
                                                             this.getClass()).getInputStream(),
                                       StandardCharsets.UTF_8))
                .when()
                .post(BASE_URL + "/send")
                .then()
                .assertThat()
                .statusCode(HttpStatus.ACCEPTED_202);

        verify(quoteEmailAPI).sendQBTDCEmail(any(QBTDCEmailRequest.class));
    }

    @Test
    @DisplayName("Invalid Email request generated 400")
    void testEmailGeneration_whenInvalidRequest_Returns400Status() throws Exception {
        given()
                .contentType(ContentType.JSON)
                .body(IOUtils.toString(new ClassPathResource(BASE_FOLDER + "InvalidEmailRequest.json",
                                                             this.getClass()).getInputStream(),
                                       StandardCharsets.UTF_8))
                .when()
                .post(BASE_URL + "/send")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST_400);

    }
}
