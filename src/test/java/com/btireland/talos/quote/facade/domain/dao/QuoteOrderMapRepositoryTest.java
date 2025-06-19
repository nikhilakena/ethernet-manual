package com.btireland.talos.quote.facade.domain.dao;


import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.config.DatabaseConfiguration;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteOrderMapStatus;
import com.btireland.talos.quote.facade.domain.entity.QuoteEmailEntity;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@DataJpaTest
@DisplayName("QuoteMapRepositoryTest")
@IntegrationTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatabaseConfiguration.class, DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
public class QuoteOrderMapRepositoryTest {
    private static final String TEST_DATA_DIR = "/data/QuoteMapRepositoryTest/";

    @Autowired
    private QuoteOrderMapRepository quoteOrderMapRepository;

    @Test
    @DataSet(cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
        "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
        "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
        "flyway_schema_history"})
    void saveQbtdcMapEntity_givenQbtdcMapAttribute_entitySaved(){
        //GIVEN
        QuoteOrderMapEntity quoteMap = new QuoteOrderMapEntity("VODA", "VODAFF", "AUTOGENERATE", true, "SKY");
        quoteMap.setQuoteEmails(List.of(new QuoteEmailEntity(quoteMap,"test1@bt.com")));

        //WHEN
        QuoteOrderMapEntity quoteMapEntity = quoteOrderMapRepository.save(quoteMap);

        //THEN
        assertThat(quoteMapEntity.getQuoteGroupId()).isNotNull();
        assertEquals("VODA", quoteMapEntity.getOperatorCode());
        assertEquals("VODAFF", quoteMapEntity.getOperatorName());
        assertTrue(quoteMapEntity.getSyncFlag());
        assertEquals("AUTOGENERATE", quoteMapEntity.getOrderNumber());
        assertEquals("SKY", quoteMapEntity.getSupplier());
        assertFalse(quoteMapEntity.getQuoteEmails().isEmpty());
        assertEquals("test1@bt.com",quoteMapEntity.getQuoteEmails().get(0).getEmail());
    }

    @Test
    @DataSet(cleanBefore = true,value = TEST_DATA_DIR + "findQbtdcQuote.yml", skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
        "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
        "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
        "flyway_schema_history"})
    void getQbtdcMapEntity_givenOrderNumberAndSupplier_qbtdcMapReturned(){
        //GIVEN

        //WHEN
        QuoteOrderMapEntity quoteMapEntity =
            quoteOrderMapRepository.findByOrderNumberAndSupplierAndStatusIn("BT-QBTDC-101", "sky", List.of(
                QuoteOrderMapStatus.COMPLETE, QuoteOrderMapStatus.DELAY)).get();

        //THEN
        assertEquals(11L, quoteMapEntity.getQuoteGroupId().longValue());
        assertEquals("VODA", quoteMapEntity.getOperatorCode());
        assertEquals("VODAFF", quoteMapEntity.getOperatorName());
        assertEquals("BT-QBTDC-101", quoteMapEntity.getOrderNumber());
        assertEquals("sky", quoteMapEntity.getSupplier());
        assertEquals(true, quoteMapEntity.getSyncFlag());

        assertEquals(1, quoteMapEntity.getQuoteItemMapList().size());
        assertEquals(200L, quoteMapEntity.getQuoteItemMapList().get(0).getWagQuoteId().longValue());
        assertEquals(100L, quoteMapEntity.getQuoteItemMapList().get(0).getQuoteId().longValue());

    }

    @Test
    @DataSet(cleanBefore = true, value = TEST_DATA_DIR + "findByQuoteGroupIdList.yml", skipCleaningFor = {
            "ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO",
            "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF"
            , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"})
    void getQuoteOrderMapEntities_givenQuoteGroupIdList_returnsQuoteOrderMapPage() {
        //GIVEN

        //WHEN
        Page<QuoteOrderMapEntity> quoteOrderMapPage = quoteOrderMapRepository.findByQuoteGroupIdIn(List.of(11L, 12L),
                                                                                                   Pageable.ofSize(3));
        //THEN
        assertEquals(2, quoteOrderMapPage.getTotalElements());
        assertEquals(11L, quoteOrderMapPage.getContent().get(0).getQuoteGroupId());
        assertEquals(12L, quoteOrderMapPage.getContent().get(1).getQuoteGroupId());

    }

    @Test
    @DataSet(cleanBefore = true,value = TEST_DATA_DIR + "findQbtdcQuote.yml", skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
        "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
        "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
        "flyway_schema_history"})
    void getQbtdcMapEntity_givenQuoteGroupId_qbtdcMapReturned(){
        //GIVEN

        //WHEN
        QuoteOrderMapEntity quoteMapEntity =
            quoteOrderMapRepository
                .findByQuoteGroupIdAndStatusNotIn(11L, Collections.singletonList(QuoteOrderMapStatus.DELAY)).get();

        //THEN
        assertEquals(11L, quoteMapEntity.getQuoteGroupId().longValue());
        assertEquals("VODA", quoteMapEntity.getOperatorCode());
        assertEquals("VODAFF", quoteMapEntity.getOperatorName());
        assertEquals("BT-QBTDC-101", quoteMapEntity.getOrderNumber());
        assertEquals("sky", quoteMapEntity.getSupplier());
        assertEquals(true, quoteMapEntity.getSyncFlag());

        assertEquals(1, quoteMapEntity.getQuoteItemMapList().size());
        assertEquals(200L, quoteMapEntity.getQuoteItemMapList().get(0).getWagQuoteId().longValue());
        assertEquals(100L, quoteMapEntity.getQuoteItemMapList().get(0).getQuoteId().longValue());

    }

    @Test
    @DataSet(cleanBefore = true, value = TEST_DATA_DIR + "findQbtdcQuote.yml", skipCleaningFor = {"ACT_GE_BYTEARRAY",
            "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF"
            , "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"})
    void getQuoteOrderMapEntity_givenOrderNumberAndSupplier_orderMapReturned() {
        //GIVEN

        //WHEN
        QuoteOrderMapEntity quoteMapEntity = quoteOrderMapRepository.findByOrderNumberAndSupplier("BT-QBTDC-101",
                                                                                                  "sky").get();

        //THEN
        assertEquals("BT-QBTDC-101", quoteMapEntity.getOrderNumber());
        assertEquals("sky", quoteMapEntity.getSupplier());
        assertEquals(2, quoteMapEntity.getQuoteEmails().size());
        assertEquals(List.of("test1@bt.com","test2@bt.com"),
                     quoteMapEntity.getQuoteEmails()
                             .stream()
                             .map(QuoteEmailEntity::getEmail)
                             .collect(toList()));

    }

}
