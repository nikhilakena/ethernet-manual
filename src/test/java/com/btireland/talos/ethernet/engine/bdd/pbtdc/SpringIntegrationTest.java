package com.btireland.talos.ethernet.engine.bdd.pbtdc;

import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import({DatabaseRiderConfiguration.class})
public class SpringIntegrationTest {

}