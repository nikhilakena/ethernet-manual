// package com.btireland.talos.ethernet.engine.service;

// import com.btireland.talos.core.common.test.tag.UnitTest;
// import com.btireland.talos.ethernet.engine.client.asset.ordermanager.PBTDCOrderDTO;
// import com.btireland.talos.ethernet.engine.client.asset.ordermanager.RejectDTO;
// import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
// import com.btireland.talos.ethernet.engine.exception.AuthenticationException;
// import com.btireland.talos.ethernet.engine.exception.RequestValidationException;
// import com.btireland.talos.ethernet.engine.soap.auth.AuthenticationHelper;
// import com.btireland.talos.ethernet.engine.soap.auth.OperatorDetails;
// import com.btireland.talos.ethernet.engine.util.PBTDCOrderDTOFactory;
// import com.btireland.talos.ethernet.engine.util.RejectCode;
// import com.btireland.talos.ethernet.engine.util.RejectDTOFactory;
// import org.assertj.core.api.Assertions;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.io.IOException;

// @UnitTest
// @ExtendWith(MockitoExtension.class)
// class PBTDCOrderValidationServiceTest {

//     @Mock
//     ApplicationConfiguration applicationConfiguration;

//     @Mock
//     AuthenticationHelper authenticationHelper;

//     PBTDCOrderValidationService pbtdcOrderValidationService;

//     @BeforeEach
//     public void setUp(){
//         pbtdcOrderValidationService = new PBTDCOrderValidationService(applicationConfiguration, authenticationHelper);
//         pbtdcOrderValidationService.orderNumberPatternStr = "^(?=.*[A-Za-z0-9])[\\w_-]+$";
//         pbtdcOrderValidationService.eircodePatternStr= "^([AC-FHKNPRTV-Y]{1}[0-9]{2}|D6W)[0-9AC-FHKNPRTV-Y]{4}$";
//         pbtdcOrderValidationService.oboPatternStr = "\\b[\\w-]{1,32}\\b";
//     }

//     @Test
//     @DisplayName("Create Order with invalid Application Date")
//     void testValidatePBTDCOrderWithInvalidApplicationDate() throws RequestValidationException, AuthenticationException, IOException {
//         PBTDCOrderDTO pbtdcOrderDTO = PBTDCOrderDTOFactory.defaultPBTDCOrderDTO();
//         pbtdcOrderDTO.getPbtdcs().setApplicationDate("20-03-2020");
//         RejectDTO expected = RejectDTOFactory.rejectDTO(RejectCode.CODE_3_APPL_DATE);
//         Mockito.when(authenticationHelper.getOperator()).thenReturn(OperatorDetails.builder().operatorOAO("sky").operatorCode("83758").operatorName("SKY").build());
//         RejectDTO rejectDTO = pbtdcOrderValidationService.validateOrder(pbtdcOrderDTO);
//         Assertions.assertThat(rejectDTO).isEqualTo(expected);

//     }

//     @Test
//     @DisplayName("Create Order with invalid Order Number")
//     void testValidatePBTDCOrderWithInvalidOrderNumber() throws RequestValidationException, AuthenticationException, IOException {
//         PBTDCOrderDTO pbtdcOrderDTO=PBTDCOrderDTOFactory.defaultPBTDCOrderDTO();
//         pbtdcOrderDTO.getOrders().setOrderNumber("KL-1:23");
//         RejectDTO expected = RejectDTOFactory.rejectDTO(RejectCode.CODE_3_ORDER_NUM);
//         Mockito.when(authenticationHelper.getOperator()).thenReturn(OperatorDetails.builder().operatorOAO("sky").operatorCode("83758").operatorName("SKY").build());
//         RejectDTO pbtdcRejectDTO = pbtdcOrderValidationService.validateOrder(pbtdcOrderDTO);
//         Assertions.assertThat(pbtdcRejectDTO).isEqualTo(expected);

//     }

//     @Test
//     @DisplayName("Create Order with invalid Order Number Prefix")
//     void testCreatePBTDCOrderInvalidOrderNumber() throws AuthenticationException, IOException {
//         PBTDCOrderDTO pbtdcOrderDTOResponse = PBTDCOrderDTOFactory.defaultPBTDCOrderDTO();
//         pbtdcOrderDTOResponse.getOrders().setOrderNumber("BT-789");
//         Mockito.when(authenticationHelper.getOperator()).thenReturn(OperatorDetails.builder().operatorOAO("sky").operatorCode("83758").operatorName("SKY").build());

//         Throwable actual = Assertions.catchThrowable(() -> {
//             pbtdcOrderValidationService.validateOrder(pbtdcOrderDTOResponse);
//         });
//         Assertions.assertThat(actual).isInstanceOf(RequestValidationException.class).hasMessageContaining("Invalid Order Number.");

//     }

//     @Test
//     @DisplayName("Create Order with invalid OBO")
//     void testCreatePBTDCOrderInvalidOBO() throws AuthenticationException, IOException {
//         PBTDCOrderDTO pbtdcOrderDTOResponse = PBTDCOrderDTOFactory.defaultPBTDCOrderDTO();
//         pbtdcOrderDTOResponse.getOrders().setObo("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//         Mockito.when(authenticationHelper.getOperator()).thenReturn(OperatorDetails.builder().operatorOAO("sky").operatorCode("83758").operatorName("SKY").build());

//         Throwable actual = Assertions.catchThrowable(() -> {
//             pbtdcOrderValidationService.validateOrder(pbtdcOrderDTOResponse);
//         });
//         Assertions.assertThat(actual).isInstanceOf(RequestValidationException.class).hasMessageContaining("Invalid OBO.");

//     }



// }
