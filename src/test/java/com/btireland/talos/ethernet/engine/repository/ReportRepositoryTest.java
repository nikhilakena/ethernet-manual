package com.btireland.talos.ethernet.engine.repository;

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.config.DatabaseConfiguration;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.ethernet.engine.domain.PBTDCReportRecord;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@DataJpaTest
@DisplayName("OrderRepositoryTest")
@IntegrationTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatabaseConfiguration.class, DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
@Transactional
public class ReportRepositoryTest {
    private static final String TEST_DATA_DIR = "/data/ReportRepositoryTest/";

    @Autowired
    private ReportRepository repository;
    @Test
    @DisplayName("Test retrieving a report by date and OAO when a report exists")
    @DataSet(value = TEST_DATA_DIR + "reportTest-ds.yml")
    void testGetReportByDateAndOaoWhenExists(){
        PBTDCReportRecord record = repository.findByReportDateAndOao(Date.valueOf(LocalDate.of( 2022, 1, 8)), "sky");
        Assertions.assertThat(new String(record.getSerializedReportInput())).isEqualTo("report dummy content 1");
    }

    @Test
    @DisplayName("Test retrieving a report by date and OAO when no report exists for the OAO")
    @DataSet(value = TEST_DATA_DIR + "reportTest-ds.yml")
    void testGetReportByDateAndOaoWhenNotExists(){
        PBTDCReportRecord record = repository.findByReportDateAndOao(Date.valueOf(LocalDate.of( 2022, 2, 8)), "sky");
        Assertions.assertThat(record).isNull();
    }

    @Test
    @DisplayName("Test retrieving a report by date when a report exists")
    @DataSet(value = TEST_DATA_DIR + "reportTest-ds.yml")
    void testGetReportByDateWhenExists(){
        boolean exists = repository.existsByReportDate(Date.valueOf(LocalDate.of( 2022, 1, 9)));
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Test retrieving a report by date when no report exists for the date")
    @DataSet(value = TEST_DATA_DIR + "reportTest-ds.yml")
    void testGetReportByDateWhenNotExists(){
        boolean exists = repository.existsByReportDate(Date.valueOf(LocalDate.of( 2022, 2, 8)));
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Test deleting reports by date when a report exists")
    @DataSet(value = TEST_DATA_DIR + "reportTest-ds.yml", transactional = true, cleanBefore = true)
    @ExpectedDataSet(value = TEST_DATA_DIR + "reportTestDeleteByDateExists-result-ds.yml", ignoreCols = "id")
    @Disabled("DBRider tests do not seem to work when records are deleted. Investigation could not determine a cause.")
    void testDeleteReportsByDateWhenExists(){
        long count = repository.deleteByReportDateEquals(Date.valueOf(LocalDate.of(2022, 1, 8)));
    }

    @Test
    @DisplayName("Test deleting reports by date when no reports exists for the date")
    @DataSet(value = TEST_DATA_DIR + "reportTest-ds.yml")
    @ExpectedDataSet(value = TEST_DATA_DIR + "reportTest-ds.yml",
            ignoreCols = {"LAST_UPDATED", "RECEIVED_TIMESTAMP"})
    void testDeleteReportsByDateWhenNotExists(){
        repository.deleteByReportDateEquals(Date.valueOf(LocalDate.of(2022, 2, 8)));
    }
}
