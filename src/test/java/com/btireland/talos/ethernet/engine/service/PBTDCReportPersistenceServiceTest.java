package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.domain.PBTDCReportRecord;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderReportDTO;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderReportSetDTO;
import com.btireland.talos.ethernet.engine.exception.ReportGenerationException;
import com.btireland.talos.ethernet.engine.repository.ReportRepository;
import com.btireland.talos.ethernet.engine.util.ByteArrayOutputStreamFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class PBTDCReportPersistenceServiceTest {
    @Mock
    private ReportRepository reportRepository;

    @Mock
    private CompressorStreamFactory compressorStreamFactory;

    @Mock
    private ObjectMapper mockJacksonObjectMapper;

    @Mock
    private ByteArrayOutputStreamFactory mockByteArrayOutputStreamFactory;

    @Captor
    ArgumentCaptor<Date> dateCaptor;
    @Captor
    ArgumentCaptor<String> oaoCaptor;

    @Captor
    ArgumentCaptor<ByteArrayInputStream> inputStreamArgumentCaptor;

    @Captor
    ArgumentCaptor<byte[]> byteArrayArgumentCaptor;

    @Captor
    ArgumentCaptor<PBTDCReportRecord> reportRecord;

    @Test
    @DisplayName("Test retrieving a report set")
    void testRetrieveReportSet() throws Exception {
        byte[] dummyCompressedReportArray = new String("test compressed data").getBytes();
        byte[] dummyUncompressedReportArray = new String("test uncompressed data").getBytes();

        PBTDCReportRecord reportRecord = PBTDCReportRecord.builder()
                .reportDate(java.sql.Date.valueOf(LocalDate.of(2022, 01, 01)))
                .serializedReportInput(dummyCompressedReportArray)
                .oao("acme")
                .id(1L)
                .build();

        CompressorInputStream mockCompressorInputStream = Mockito.mock(CompressorInputStream.class);

        Mockito.when(reportRepository.findByReportDateAndOao(Mockito.any(Date.class), Mockito.any(String.class)))
                .thenReturn(reportRecord);

        Mockito.when(mockCompressorInputStream.readAllBytes())
                        .thenReturn(dummyUncompressedReportArray);
        Mockito.when(compressorStreamFactory.createCompressorInputStream(Mockito.any(ByteArrayInputStream.class)))
                .thenReturn(mockCompressorInputStream);

        PBTDCOrderReportDTO dummyOrder = PBTDCOrderReportDTO.builder().orderManager("test order manager").build();

        PBTDCOrderReportSetDTO dummyOrderReportSetDTO = PBTDCOrderReportSetDTO.builder().internalReportEntries(List.of(dummyOrder)).build();

        Mockito.when(mockJacksonObjectMapper.readValue(Mockito.any(byte[].class), Mockito.any(Class.class)))
                .thenReturn(dummyOrderReportSetDTO);

        PBTDCReportPersistenceService pbtdcReportPersistenceService = new PBTDCReportPersistenceService(
                reportRepository,
                compressorStreamFactory,
                mockJacksonObjectMapper,
                mockByteArrayOutputStreamFactory
        );

        Optional<PBTDCOrderReportSetDTO> outputReportSetDTO = pbtdcReportPersistenceService.get(LocalDate.of(2022, 01, 01), "acme");

        // check that we fetched the correct date from the repository

        Mockito.verify(reportRepository).findByReportDateAndOao(
                dateCaptor.capture(),
                oaoCaptor.capture()
        );

        Assertions.assertEquals(dateCaptor.getValue(), java.sql.Date.valueOf(LocalDate.of(2022, 01, 01)));
        Assertions.assertEquals(oaoCaptor.getValue(), "acme");

        // check that we called the decompressor with the compressed data

        Mockito.verify(compressorStreamFactory).createCompressorInputStream(
                inputStreamArgumentCaptor.capture()
        );

        Assertions.assertArrayEquals(inputStreamArgumentCaptor.getValue().readAllBytes(), dummyCompressedReportArray);

        // check that we got the expected class back
        Assertions.assertEquals(outputReportSetDTO.get(), dummyOrderReportSetDTO);
    }

    @Test
    @DisplayName("Test saving a report set")
    void testSaveReportSet() throws Exception {
        PBTDCOrderReportDTO dummyOrder = PBTDCOrderReportDTO.builder().orderManager("test order manager").build();

        PBTDCOrderReportSetDTO dummyOrderReportSetDTO = PBTDCOrderReportSetDTO.builder().internalReportEntries(List.of(dummyOrder)).build();

        String dummyCompressedReportArray = "test save compressed data";
        String dummyUncompressedReportArray = "test save uncompressed data";

        CompressorOutputStream mockCompressorOutputStream = Mockito.mock(CompressorOutputStream.class);

        Mockito.when(compressorStreamFactory.createCompressorOutputStream(Mockito.anyString(), Mockito.any(ByteArrayOutputStream.class)))
                .thenReturn(mockCompressorOutputStream);

        ByteArrayOutputStream mockByteArrayOutputStream  = Mockito.mock(ByteArrayOutputStream.class);

        Mockito.when(mockByteArrayOutputStream.toByteArray())
                        .thenReturn(dummyCompressedReportArray.getBytes());
        Mockito.when(mockByteArrayOutputStreamFactory.construct())
                .thenReturn(mockByteArrayOutputStream);

        ObjectWriter mockObjectWriter = Mockito.mock(ObjectWriter.class);
        Mockito.when(mockObjectWriter.writeValueAsString(Mockito.any(PBTDCOrderReportSetDTO.class)))
                        .thenReturn(dummyUncompressedReportArray);
        Mockito.when(mockJacksonObjectMapper.writerWithDefaultPrettyPrinter())
                .thenReturn(mockObjectWriter);

        PBTDCReportPersistenceService pbtdcReportPersistenceService = new PBTDCReportPersistenceService(
                reportRepository,
                compressorStreamFactory,
                mockJacksonObjectMapper,
                mockByteArrayOutputStreamFactory
        );

        // run the function under test
        pbtdcReportPersistenceService.save("acme", LocalDate.of(2022, 01, 01), dummyOrderReportSetDTO);

        // check that we wrote the serialized data to the compressor

        Mockito.verify(mockCompressorOutputStream).write(byteArrayArgumentCaptor.capture());
        Assertions.assertEquals(dummyUncompressedReportArray, new String(byteArrayArgumentCaptor.getValue()));

        // check that we wrote the compressed data to the DB.

        Mockito.verify(reportRepository).save(reportRecord.capture());

        Assertions.assertEquals(reportRecord.getValue().getOao(), "acme");
        Assertions.assertEquals(new String(reportRecord.getValue().getSerializedReportInput()), dummyCompressedReportArray);
    }

    // Reading a report triggers an IO exception

    @Test
    @DisplayName("Test retrieving a report when there is an IO exception during the read")
    void testGetReportSetTriggersIOException() throws Exception {
        byte[] dummyCompressedReportArray = "test compressed data".getBytes();

        PBTDCReportRecord reportRecord = PBTDCReportRecord.builder()
                .reportDate(Date.from(Instant.now()))
                .serializedReportInput(dummyCompressedReportArray)
                .oao("acme")
                .id(1L)
                .build();

        CompressorInputStream mockCompressorInputStream = Mockito.mock(CompressorInputStream.class);

        Mockito.when(reportRepository.findByReportDateAndOao(Mockito.any(Date.class), Mockito.any(String.class)))
                .thenReturn(reportRecord);

        Mockito.when(mockCompressorInputStream.readAllBytes())
                .thenThrow(new IOException("Test Exception"));
        Mockito.when(compressorStreamFactory.createCompressorInputStream(Mockito.any(ByteArrayInputStream.class)))
                .thenReturn(mockCompressorInputStream);

        PBTDCReportPersistenceService pbtdcReportPersistenceService = new PBTDCReportPersistenceService(
                reportRepository,
                compressorStreamFactory,
                mockJacksonObjectMapper,
                mockByteArrayOutputStreamFactory
        );

        assertThrows(ReportGenerationException.class, () -> pbtdcReportPersistenceService.get(LocalDate.of(2022, 01, 01), "acme"));
    }

    // Reading a report triggers a compressor exception

    @Test
    @DisplayName("Test retrieving a report when there is an Compression exception during the read")
    void testGetReportSetTriggersCompressorException() throws Exception {
        byte[] dummyCompressedReportArray = "test compressed data".getBytes();
        PBTDCReportRecord reportRecord = PBTDCReportRecord.builder()
                .reportDate(Date.from(Instant.now()))
                .serializedReportInput(dummyCompressedReportArray)
                .oao("acme")
                .id(1L)
                .build();

        Mockito.when(reportRepository.findByReportDateAndOao(Mockito.any(Date.class), Mockito.any(String.class)))
                .thenReturn(reportRecord);

        Mockito.when(compressorStreamFactory.createCompressorInputStream(Mockito.any(ByteArrayInputStream.class)))
                .thenThrow(new CompressorException("Test Exception"));

        PBTDCReportPersistenceService pbtdcReportPersistenceService = new PBTDCReportPersistenceService(
                reportRepository,
                compressorStreamFactory,
                mockJacksonObjectMapper,
                mockByteArrayOutputStreamFactory
        );

        assertThrows(ReportGenerationException.class, () -> pbtdcReportPersistenceService.get(LocalDate.of(2022, 01, 01), "acme"));
    }

    // Writing a report triggers a JSON processing exception

    @Test
    @DisplayName("Test writing a report when there is an JSON processing exception")
    void testWriteReportSetTriggersJSONProcessingException() throws Exception {
        PBTDCOrderReportDTO dummyOrder = PBTDCOrderReportDTO.builder().orderManager("test order manager").build();

        PBTDCOrderReportSetDTO dummyOrderReportSetDTO = PBTDCOrderReportSetDTO.builder().internalReportEntries(List.of(dummyOrder)).build();

        ObjectWriter mockObjectWriter = Mockito.mock(ObjectWriter.class);
        Mockito.when(mockObjectWriter.writeValueAsString(Mockito.any(PBTDCOrderReportSetDTO.class)))
                .thenThrow(JsonProcessingException.class);
        Mockito.when(mockJacksonObjectMapper.writerWithDefaultPrettyPrinter())
                .thenReturn(mockObjectWriter);

        PBTDCReportPersistenceService pbtdcReportPersistenceService = new PBTDCReportPersistenceService(
                reportRepository,
                compressorStreamFactory,
                mockJacksonObjectMapper,
                mockByteArrayOutputStreamFactory
        );

        assertThrows(ReportGenerationException.class, () ->
                pbtdcReportPersistenceService.save("acme", LocalDate.of(2022, 01, 01), dummyOrderReportSetDTO));
    }

    @Test
    @DisplayName("Test writing a report when there is a compressor exception")
    void testWriteReportSetTriggersCompressorException() throws Exception {
        PBTDCOrderReportDTO dummyOrder = PBTDCOrderReportDTO.builder().orderManager("test order manager").build();

        PBTDCOrderReportSetDTO dummyOrderReportSetDTO = PBTDCOrderReportSetDTO.builder().internalReportEntries(List.of(dummyOrder)).build();

        String dummyUncompressedReportArray = "test save uncompressed data";

        CompressorOutputStream mockCompressorOutputStream = Mockito.mock(CompressorOutputStream.class);

        Mockito.when(compressorStreamFactory.createCompressorOutputStream(Mockito.anyString(), Mockito.any(ByteArrayOutputStream.class)))
                .thenThrow(new CompressorException("test"));

        ByteArrayOutputStream mockByteArrayOutputStream  = Mockito.mock(ByteArrayOutputStream.class);

        Mockito.when(mockByteArrayOutputStreamFactory.construct())
                .thenReturn(mockByteArrayOutputStream);

        ObjectWriter mockObjectWriter = Mockito.mock(ObjectWriter.class);
        Mockito.when(mockObjectWriter.writeValueAsString(Mockito.any(PBTDCOrderReportSetDTO.class)))
                .thenReturn(dummyUncompressedReportArray);
        Mockito.when(mockJacksonObjectMapper.writerWithDefaultPrettyPrinter())
                .thenReturn(mockObjectWriter);

        PBTDCReportPersistenceService pbtdcReportPersistenceService = new PBTDCReportPersistenceService(
                reportRepository,
                compressorStreamFactory,
                mockJacksonObjectMapper,
                mockByteArrayOutputStreamFactory
        );

        assertThrows(ReportGenerationException.class, () ->
                pbtdcReportPersistenceService.save("acme", LocalDate.of(2022, 01, 01), dummyOrderReportSetDTO));
    }

    @Test
    @DisplayName("Test writing a report when there is an IO exception")
    void testWriteReportSetTriggersIOException() throws Exception {
        PBTDCOrderReportDTO dummyOrder = PBTDCOrderReportDTO.builder().orderManager("test order manager").build();

        PBTDCOrderReportSetDTO dummyOrderReportSetDTO = PBTDCOrderReportSetDTO.builder().internalReportEntries(List.of(dummyOrder)).build();

        String dummyUncompressedReportArray = "test save uncompressed data";

        Mockito.when(compressorStreamFactory.createCompressorOutputStream(Mockito.anyString(), Mockito.any(ByteArrayOutputStream.class)))
                .thenThrow(new CompressorException("test"));

        ByteArrayOutputStream mockByteArrayOutputStream  = Mockito.mock(ByteArrayOutputStream.class);

        Mockito.when(mockByteArrayOutputStreamFactory.construct())
                .thenReturn(mockByteArrayOutputStream);

        ObjectWriter mockObjectWriter = Mockito.mock(ObjectWriter.class);
        Mockito.when(mockObjectWriter.writeValueAsString(Mockito.any(PBTDCOrderReportSetDTO.class)))
                .thenReturn(dummyUncompressedReportArray);
        Mockito.when(mockJacksonObjectMapper.writerWithDefaultPrettyPrinter())
                .thenReturn(mockObjectWriter);

        PBTDCReportPersistenceService pbtdcReportPersistenceService = new PBTDCReportPersistenceService(
                reportRepository,
                compressorStreamFactory,
                mockJacksonObjectMapper,
                mockByteArrayOutputStreamFactory
        );

        assertThrows(ReportGenerationException.class, () ->
                pbtdcReportPersistenceService.save("acme", LocalDate.of(2022, 01, 01), dummyOrderReportSetDTO));
    }

}
