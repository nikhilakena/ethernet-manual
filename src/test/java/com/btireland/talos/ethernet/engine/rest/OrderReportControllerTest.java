package com.btireland.talos.ethernet.engine.rest;

import com.btireland.talos.core.common.rest.exception.checked.TalosForbiddenException;
import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.ethernet.engine.exception.ReportGenerationException;
import com.btireland.talos.ethernet.engine.service.JasperReportGeneratorService;
import com.btireland.talos.ethernet.engine.service.PBTDCReportGeneratorService;
import com.btireland.talos.ethernet.engine.util.ReportAvailabilityDTOFactory;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = OrderReportController.class)
@ActiveProfiles("test")
@AutoConfigureJsonTesters
@IntegrationTest
public class OrderReportControllerTest {
    private static final String BASE_URL = "/api/v1/dc/report/kci";
    private static final String CONTENT_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String CONTENT_TYPE_XLS = "application/vnd.ms-excel";

    private static final String DUMMY_XLS_FILE_CONTENT = "this represents an XLS file";
    private static final String DUMMY_XLSX_FILE_CONTENT = "this represents an XLSX file";
    private static final String DUMMY_PDF_FILE_CONTENT = "this represents a PDF file";
    private static final String DUMMY_JSON_FILE_CONTENT = "this represents a JSON file";
    @Captor
    ArgumentCaptor<Optional<LocalDate>> optionalLocalDateCaptor;
    @MockBean
    private JasperReportGeneratorService jasperReportGeneratorService;
    @MockBean
    private PBTDCReportGeneratorService reportGeneratorService;
    @MockBean
    private ApplicationConfiguration applicationConfiguration;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private Logger logger;

    @BeforeEach
    public void initialiseRestAssuredMockMvcWebApplicationContext() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssuredMockMvc.config = RestAssuredMockMvc.config().encoderConfig(new EncoderConfig(StandardCharsets.UTF_8.displayName(), StandardCharsets.UTF_8.displayName()));
    }

    // Mock the report generator service.

    @Test
    @DisplayName("Test report generation returns a 202")
    void testReportGeneration() throws Exception {
        when(reportGeneratorService.generate(any())).thenReturn(true);
        given()
                .contentType(ContentType.ANY)
                .when()
                .post(BASE_URL + "/generate")
                .then()
                .assertThat()
                .statusCode(HttpStatus.ACCEPTED_202);


        verify(reportGeneratorService).generate(optionalLocalDateCaptor.capture());

        assertEquals(Optional.empty(), optionalLocalDateCaptor.getValue());
    }

    @Test
    @DisplayName("Test report generation with a date captures the correct date and returns a 202")
    void testReportGenerationWithDate() throws Exception {
        when(reportGeneratorService.generate(any())).thenReturn(true);
        given()
                .contentType(ContentType.ANY)
                .when()
                .post(BASE_URL + "/generate?date=2022-01-01")
                .then()
                .assertThat()
                .statusCode(HttpStatus.ACCEPTED_202);

        verify(reportGeneratorService).generate(optionalLocalDateCaptor.capture());

        assertEquals(Optional.of(LocalDate.of(2022, 01, 01)), optionalLocalDateCaptor.getValue());
    }

    @Test
    @DisplayName("Test report generation that would overwrite an existing report by default returns a 403")
    void testReportGenerationOverwriteWithOverwriteDisabled() throws Exception {
        TalosForbiddenException talosForbiddenException = new TalosForbiddenException(logger);
        when(reportGeneratorService.generate(any())).thenThrow(talosForbiddenException);
        given()
                .contentType(ContentType.ANY)
                .when()
                .post(BASE_URL + "/generate")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN_403);
    }

    // test the XLS retrieval.

    @Test
    @DisplayName("Test retrieving an XLS report")
    void testRetrieveXLSReport() throws ReportGenerationException {

        when(jasperReportGeneratorService.generateExcel9703(any(LocalDate.class), anyString(), anyBoolean()))
                .thenReturn(Optional.of(DUMMY_XLS_FILE_CONTENT.getBytes()));

        byte[] returnedReport =
                given()
                        .accept(CONTENT_TYPE_XLS)
                        .contentType(CONTENT_TYPE_XLS)
                        .log().all()
                        .when()
                        .get(BASE_URL + "/retrieve/acme?date=2022-01-01&internal=true")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK_200)
                        .and()
                        .contentType(CONTENT_TYPE_XLS)
                        .extract().body().asByteArray();

        Assertions.assertArrayEquals(DUMMY_XLS_FILE_CONTENT.getBytes(), returnedReport);
    }

    // test the XLSX retrieval.

    @Test
    @DisplayName("Test retrieving an XLSX report")
    void testRetrieveXLSXReport() throws ReportGenerationException {
        when(jasperReportGeneratorService.generateExcel2003(any(LocalDate.class), anyString(), anyBoolean()))
                .thenReturn(Optional.of(DUMMY_XLSX_FILE_CONTENT.getBytes()));

        byte[] returnedReport =
                given()
                        .accept(CONTENT_TYPE_XLSX)
                        .contentType(CONTENT_TYPE_XLSX)
                        .when()
                        .get(BASE_URL + "/retrieve/acme?date=2022-01-01&internal=true")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK_200)
                        .and()
                        .contentType(CONTENT_TYPE_XLSX)
                        .extract().body().asByteArray();

        Assertions.assertArrayEquals(DUMMY_XLSX_FILE_CONTENT.getBytes(), returnedReport);
    }

    // test the PDF retrieval.

    @Test
    @DisplayName("Test retrieving a PDF report")
    void testRetrievePDFReport() throws ReportGenerationException {
        when(jasperReportGeneratorService.generatePDF(any(LocalDate.class), anyString(), anyBoolean()))
                .thenReturn(Optional.of(DUMMY_PDF_FILE_CONTENT.getBytes()));

        byte[] returnedReport =
                given()
                        .accept(MediaType.APPLICATION_PDF)
                        .contentType(MediaType.APPLICATION_PDF_VALUE)
                        .when()
                        .get(BASE_URL + "/retrieve/acme?date=2022-01-01&internal=true")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK_200)
                        .and()
                        .contentType(MediaType.APPLICATION_PDF_VALUE)
                        .extract().body().asByteArray();

        Assertions.assertArrayEquals(DUMMY_PDF_FILE_CONTENT.getBytes(), returnedReport);
    }

    // test CSV retrieval

    @Test
    @DisplayName("Test retrieving a JSON report")
    void testRetrieveJSONReport() throws ReportGenerationException {
        when(jasperReportGeneratorService.generateJSON(any(LocalDate.class), anyString(), anyBoolean()))
                .thenReturn(Optional.of(DUMMY_JSON_FILE_CONTENT.getBytes()));

        byte[] returnedReport =
                given()
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get(BASE_URL + "/retrieve/acme?date=2022-01-01&internal=true")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK_200)
                        .and()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .extract().body().asByteArray();

        Assertions.assertArrayEquals(DUMMY_JSON_FILE_CONTENT.getBytes(), returnedReport);
    }

    // test retrieving an XLS report when there are no reports for the calendar day.

    @Test
    @DisplayName("Test retrieving an JSON report when no report is available - should return a 404")
    void testRetrieveJsonReportWhenNoReportAvailable() throws ReportGenerationException {
        when(jasperReportGeneratorService.generateJSON(any(LocalDate.class), anyString(), anyBoolean()))
                .thenReturn(Optional.empty());

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(BASE_URL + "/retrieve/acme?date=2022-01-01&internal=true")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404);
    }

    @Nested
    @DisplayName("Test retrieving report availability")
    class retrieveReportAvailability {
        @Test
        @DisplayName("Test retrieving report availability - success")
        void testRetrieveReportAvailability() throws Exception {
            when(reportGeneratorService.retrieveReportAvailability(any(LocalDate.class), any(LocalDate.class), anyString()))
                    .thenReturn(ReportAvailabilityDTOFactory.reportAvailability());

            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get(BASE_URL + "/availability/sky?from=2022-02-01&to=2022-02-04")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.OK_200)
                    .extract().body().asString().equals(ReportAvailabilityDTOFactory.asReportAvailabilityJson());

        }

        @Test
        @DisplayName("Test retrieving report availability with missing date range - 400 bad request")
        void testRetrieveReportAvailability_invalidDateRange() throws Exception {

            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get(BASE_URL + "/availability/sky?")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST_400);

        }

    }
}
