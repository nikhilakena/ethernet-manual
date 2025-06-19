package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.domain.PBTDCReportRecord;
import com.btireland.talos.ethernet.engine.dto.PBTDCOrderReportSetDTO;
import com.btireland.talos.ethernet.engine.dto.PbtdcReportDateView;
import com.btireland.talos.ethernet.engine.exception.ReportGenerationException;
import com.btireland.talos.ethernet.engine.repository.ReportRepository;
import com.btireland.talos.ethernet.engine.util.ByteArrayOutputStreamFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional

public class PBTDCReportPersistenceService {
    private ReportRepository reportRepository;

    private CompressorStreamFactory compressorStreamFactory;
    private ObjectMapper jacksonObjectMapper;

    private ByteArrayOutputStreamFactory byteArrayOutputStreamFactory;
    public PBTDCReportPersistenceService(ReportRepository reportRepository,
                                         CompressorStreamFactory compressorStreamFactory,
                                         ObjectMapper objectMapper,
                                         ByteArrayOutputStreamFactory byteArrayOutputStreamFactory){
        this.reportRepository = reportRepository;
        this.compressorStreamFactory = compressorStreamFactory;
        this.jacksonObjectMapper = objectMapper;
        this.byteArrayOutputStreamFactory = byteArrayOutputStreamFactory;
    }

    private byte[] serializeAndCompressReportSet(PBTDCOrderReportSetDTO reportSet) throws ReportGenerationException{
        // serialize as JSON
        String jsonReportSet = null;
        try {
            jsonReportSet = this.jacksonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reportSet);
        } catch (JsonProcessingException e) {
            throw new ReportGenerationException("Error while serializing report : " + e.getMessage());
        }

        // Compress the JSON

        ByteArrayOutputStream compressedOutput = this.byteArrayOutputStreamFactory.construct();

        try (CompressorOutputStream compressorOutputStream = compressorStreamFactory.createCompressorOutputStream(CompressorStreamFactory.GZIP, compressedOutput)) {
            compressorOutputStream.write(jsonReportSet.getBytes());
            compressorOutputStream.close();
        }catch(CompressorException | IOException e){
            throw new ReportGenerationException("Error while generating compressed report : " + e.getMessage());
        }

        return compressedOutput.toByteArray();
    }

    private PBTDCOrderReportSetDTO decompressAndDeserializeReportSet(byte[] compressedReportData) throws ReportGenerationException {
        // decompress
        try (CompressorInputStream inputStream = this.compressorStreamFactory.createCompressorInputStream(new ByteArrayInputStream(compressedReportData))) {
            // deserialize
            return this.jacksonObjectMapper.readValue(inputStream.readAllBytes(), PBTDCOrderReportSetDTO.class);
        } catch (CompressorException exception) {
            throw new ReportGenerationException("Exception while decompressing report : " + exception.getMessage());
        } catch (IOException exception) {
            throw new ReportGenerationException("Exception while parsing decompressed report : " + exception.getMessage());
        }
    }

    public void save(String oao, LocalDate date, PBTDCOrderReportSetDTO reportSet) throws ReportGenerationException{
        byte[] compressedReportSet = serializeAndCompressReportSet(reportSet);
        PBTDCReportRecord r = PBTDCReportRecord.builder()
                        .oao(oao)
                        .reportDate(java.sql.Date.valueOf(date))
                        .serializedReportInput(compressedReportSet)
                        .build();
        reportRepository.save(r);
    }

    public Optional<PBTDCOrderReportSetDTO> get(LocalDate date, String oao) throws ReportGenerationException {
        PBTDCReportRecord r  = reportRepository.findByReportDateAndOao(java.sql.Date.valueOf(date), oao);
        if(r != null){
            return Optional.of(decompressAndDeserializeReportSet(r.getSerializedReportInput()));
        }else{
            return Optional.empty();
        }
    }

    public boolean checkReportExistsForDate(LocalDate date){
        return reportRepository.existsByReportDate(java.sql.Date.valueOf(date));
    }

    public void deleteAllReportsByDate(LocalDate date){
        reportRepository.deleteByReportDateEquals(java.sql.Date.valueOf(date));
    }

    public List<PbtdcReportDateView> getReportsAvailableDatesForDateRange(LocalDate startDate, LocalDate endDate, String oao) {
        return reportRepository.findByOaoAndReportDateBetween(oao,java.sql.Date.valueOf(startDate),java.sql.Date.valueOf(endDate));
    }
}
