package com.btireland.talos.quote.facade.domain.dao;


import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.config.DatabaseConfiguration;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.quote.facade.domain.entity.QuoteItemMapEntity;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DisplayName("QuoteMapRepositoryTest")
@IntegrationTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatabaseConfiguration.class, DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
public class QuoteItemMapRepositoryTest {
    private static final String TEST_DATA_DIR = "/data/QuoteMapRepositoryTest/";

    @Autowired
    private QuoteItemMapRepository quoteItemMapRepository;

    @Autowired
    private QuoteOrderMapRepository quoteOrderMapRepository;

    @Test
    @DataSet(cleanBefore = true,value = TEST_DATA_DIR + "findQbtdcQuote.yml", skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
        "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
        "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
        "flyway_schema_history"})
    void getQbtdcItemEntity_givenQbtdcItemId_qbtdcItemMapReturned(){
        //GIVEN

        //WHEN
        Optional<QuoteItemMapEntity> quoteItemMapEntity = quoteItemMapRepository.findById(1L);

        //THEN
        assertTrue(quoteItemMapEntity.isPresent());
        assertEquals(200L, quoteItemMapEntity.get().getWagQuoteId().longValue());
        assertEquals(100L, quoteItemMapEntity.get().getQuoteId().longValue());
        assertEquals("BT-QBTDC-101", quoteItemMapEntity.get().getQuoteOrderMap().getOrderNumber());
        assertEquals("VODAFF", quoteItemMapEntity.get().getQuoteOrderMap().getOperatorName());
    }

    @Test
    @DataSet(cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
        "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
        "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
        "flyway_schema_history"})
    void saveQbtdcItemMapEntity_givenQbtdcItemMapAttribute_entitySaved(){
        //GIVEN
        QuoteOrderMapEntity quoteMap = new QuoteOrderMapEntity("PURE","Pure telecom","AUTOGENERATE",true, "SKY");
        QuoteItemMapEntity quoteItemMap = new QuoteItemMapEntity(1000L, 2000L, quoteMap);
        quoteOrderMapRepository.save(quoteMap);

        //WHEN
        QuoteItemMapEntity savedQuoteItemMap = quoteItemMapRepository.save(quoteItemMap);

        //THEN
        assertThat(savedQuoteItemMap.getId()).isNotNull();
        assertEquals(1000L, savedQuoteItemMap.getQuoteId());
        assertEquals(2000L, savedQuoteItemMap.getWagQuoteId());
        assertEquals("Pure telecom", savedQuoteItemMap.getQuoteOrderMap().getOperatorName());
        assertEquals("AUTOGENERATE", savedQuoteItemMap.getQuoteOrderMap().getOrderNumber());
        assertEquals("SKY", savedQuoteItemMap.getQuoteOrderMap().getSupplier());
    }

    @Test
    @DataSet(cleanBefore = true,value = TEST_DATA_DIR + "findQbtdcQuote.yml", skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"})
    void getQuoteItemEntity_givenQuoteId_quoteItemMapReturned(){
        //GIVEN

        //WHEN
        Optional<QuoteItemMapEntity> quoteItemMapEntity = quoteItemMapRepository.findByQuoteId(100L);

        //THEN
        assertTrue(quoteItemMapEntity.isPresent());
        assertEquals(100L, quoteItemMapEntity.get().getQuoteId().longValue());
    }
}
