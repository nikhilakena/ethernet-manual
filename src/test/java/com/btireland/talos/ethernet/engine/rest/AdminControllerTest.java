package com.btireland.talos.ethernet.engine.rest;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.dto.CloseInterventionDTO;
import com.btireland.talos.ethernet.engine.dto.InterventionDetailsDTO;
import com.btireland.talos.ethernet.engine.facade.PBTDCOrderFacade;
import io.restassured.config.EncoderConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;


@WebMvcTest(controllers = AdminController.class)
@ActiveProfiles("test")
@AutoConfigureJsonTesters
@IntegrationTest

public class AdminControllerTest {
    private static final String BASE_URL = "/api/v1/admin";

    @MockBean
    private PBTDCOrderFacade ordersFacade;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void initialiseRestAssuredMockMvcWebApplicationContext() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssuredMockMvc.config = RestAssuredMockMvc.config().encoderConfig(new EncoderConfig(StandardCharsets.UTF_8.displayName(), StandardCharsets.UTF_8.displayName()));
    }

    @BeforeEach
    public void resetMock() {
        Mockito.reset(ordersFacade);
    }

    @Test
    void testGetInterventionDetails(){
        Long id=1l;
        String status="OPEN";
        List<InterventionDetailsDTO> interventionDetailsDTOS=List.of(InterventionDetailsDTO.builder().id(1l).build(),InterventionDetailsDTO.builder().id(2l).build());
        Mockito.lenient().when(ordersFacade.getInterventions(id,status)).thenReturn(interventionDetailsDTOS);

        List<InterventionDetailsDTO>extract = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(BASE_URL+"/"+id+"/interventions?status="+status)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().body().jsonPath().getList("",InterventionDetailsDTO.class);

        Assertions.assertEquals(extract.get(0).getId(),1l);
    }



    @Test
    void testCloseIntervention(){
        Long id=1l;
        Long intId=1l;

        CloseInterventionDTO closeInterventionDTO=CloseInterventionDTO.builder().agent("sky").closingNotes("matter solved").build();

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(closeInterventionDTO)
                .when()
                .post(BASE_URL+"/interventions/"+intId+":close")
                .then()
                .assertThat()
                .statusCode(200);
    }

}
