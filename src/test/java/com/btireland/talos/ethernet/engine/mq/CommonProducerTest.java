package com.btireland.talos.ethernet.engine.mq;

import com.btireland.talos.core.common.message.test.MessageUtils;
import com.btireland.talos.core.common.test.tag.IntegrationTest;
import com.btireland.talos.ethernet.engine.config.DatabaseRiderConfiguration;
import com.btireland.talos.ethernet.engine.dto.OrdersDTO;
import com.btireland.talos.ethernet.engine.util.PBTDCOrderDTOFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.apache.activemq.artemis.junit.EmbeddedActiveMQResource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

@IntegrationTest
@SpringBootTest
@ActiveProfiles("test")
@Import({DatabaseRiderConfiguration.class})
@DBRider(dataSourceBeanName = "databaseRiderDatasource")
public class CommonProducerTest {

    @Autowired
    public EmbeddedActiveMQResource mqServer;
    @Autowired
    private CommonProducer commonProducer;

    @Value("${application.queue.cerberus.data-sync}")
    private String cerberusDataSyncQueue;

    @Test
    @DataSet(cleanBefore = true, skipCleaningFor = {"ACT_GE_BYTEARRAY", "ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG", "ACT_ID_GROUP", "ACT_ID_INFO", "ACT_ID_MEMBERSHIP", "ACT_ID_TENANT",
            "ACT_ID_TENANT_MEMBER", "ACT_ID_USER", "ACT_RE_CASE_DEF", "ACT_RE_DECISION_DEF", "ACT_RE_DECISION_REQ_DEF", "ACT_RE_DEPLOYMENT", "ACT_RE_PROCDEF",
            "ACT_RU_AUTHORIZATION", "ACT_RU_EVENT_SUBSCR", "ACT_RU_FILTER", "ACT_RU_JOBDEF",
            "flyway_schema_history"
            })
    public void testSendMessage() throws JsonProcessingException {
        MessageUtils messageUtils = new MessageUtils(mqServer,
                List.of(), List.of(cerberusDataSyncQueue),
                Map.of(OrdersDTO.class, OrdersDTO.class.getSimpleName()));

        OrdersDTO ordersDTO = PBTDCOrderDTOFactory.defaultOrdersDTO();

        commonProducer.sendMessage(ordersDTO,cerberusDataSyncQueue);

        Assertions.assertThat(messageUtils.getFirstMessageFromQueue(cerberusDataSyncQueue, OrdersDTO.class)).isEqualTo(ordersDTO);
    }
}
