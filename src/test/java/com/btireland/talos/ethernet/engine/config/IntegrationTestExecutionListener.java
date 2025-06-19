package com.btireland.talos.ethernet.engine.config;
import com.btireland.talos.core.common.test.tag.IntegrationTest;
import org.apache.activemq.artemis.junit.EmbeddedActiveMQResource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Component
@ActiveProfiles("test")
public class IntegrationTestExecutionListener implements TestExecutionListener, Ordered {
    private static final EmbeddedActiveMQResource embeddedActiveMQResource = new EmbeddedActiveMQResource("broker.xml");

    @Bean
    public EmbeddedActiveMQResource getEmbeddedActiveMQResource() {
        return embeddedActiveMQResource;
    }

    public void beforeTestClass(TestContext testContext) throws Exception {
        if(isIntegrationTest(testContext)) {
            if(!(embeddedActiveMQResource.getServer().getActiveMQServer() != null && embeddedActiveMQResource.getServer().getActiveMQServer().isStarted())) {
                embeddedActiveMQResource.start();
                embeddedActiveMQResource.getServer().waitClusterForming(10, TimeUnit.SECONDS, 5, 1);
                embeddedActiveMQResource.getServer().getActiveMQServer().waitForActivation(10, TimeUnit.SECONDS);
            }
            Thread.sleep(1000);
        }
    };

    public void afterTestClass(TestContext testContext) throws Exception {
        if(isIntegrationTest(testContext)) {
            if((embeddedActiveMQResource.getServer().getActiveMQServer() != null && embeddedActiveMQResource.getServer().getActiveMQServer().isStarted())) {
                embeddedActiveMQResource.getServer().waitClusterForming(10, TimeUnit.SECONDS, 5, 1);
                embeddedActiveMQResource.getServer().getActiveMQServer().waitForActivation(10, TimeUnit.SECONDS);
                embeddedActiveMQResource.stop();
            }
            Thread.sleep(1000);
        }
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    private Boolean isIntegrationTest(TestContext testContext) {
        if(testContext.getTestClass().getAnnotationsByType(IntegrationTest.class).length > 0){
            return true;
        }
        return false;
    }
}
