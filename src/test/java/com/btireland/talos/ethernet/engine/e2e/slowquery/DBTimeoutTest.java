package com.btireland.talos.ethernet.engine.e2e.slowquery;

//    This class allows us to check that the JPA timeout is being enforced.
//    because H2 does not allow slow queries to be simulated, MariaDB
//    is needed to test. In addition, only MariaDB 10.1 or later supports
//    aborting a slow query. So this test is disabled by default.
//
//    We can re-enable this test later when we have an automated test
//    framework that can test against MariaDB automatically.

import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.config.DatabaseConfiguration;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.QueryTimeoutException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@DisplayName("DB Timeout Test")
@IntegrationTest
@ActiveProfiles("test-mariadb")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatabaseConfiguration.class, DatabaseRiderConfiguration.class})
@Slf4j
@Disabled
public class DBTimeoutTest {
    @Autowired
    private EntityManager entityManager;
    @Test
    @DisplayName("DB Timeout Test")
    public void proveDatabaseTimeout(){
        assertThrows(QueryTimeoutException.class,
                () -> entityManager
                        .createNativeQuery("SELECT SLEEP(10)")
                        .getResultList(),
                "Expected a timeout exception to be thrown");
    }
}
