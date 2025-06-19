package com.btireland.talos.quote.facade.process.mapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteOrderMapStatus;
import com.btireland.talos.quote.facade.domain.entity.QuoteItemMapEntity;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteOrderResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.SearchQuoteResponse;
import com.btireland.talos.quote.facade.process.helper.CamundaHelper;
import com.btireland.talos.quote.facade.process.mapper.quotemanager.offlinepricing.SearchQuoteResponseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.task.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

@UnitTest
@ExtendWith(MockitoExtension.class)
class SearchQuoteResponseMapperTest {

    private static final String TEST_DATA_DIR = "/data/QuoteFacade/SearchQuoteResponseMapperTest/";

    @Mock
    private CamundaHelper camundaHelper;

    @InjectMocks
    private SearchQuoteResponseMapper searchQuoteResponseMapper;

    @Test
    @DisplayName("Validate business console quotes are correctly mapped using Quote Manager response")
    void mapSearchQuoteResponse_withQuoteManagerResponse_returnsQbtdcOrderDTOList() throws IOException, TalosNotFoundException {
        //GIVEN
        SearchQuoteResponse searchQuoteResponse = fetchQuoteManagerSearchQuoteResponse();
        List<QuoteOrderMapEntity> quoteOrderMapList = getQuoteOrderMapList();
        List<QuoteOrderResponse> expected = fetchSearchResponse();
        Mockito.when(camundaHelper.fetchUserTasks(anyLong())).thenReturn(List.of(new TaskEntity()));
        Mockito.when(camundaHelper.fetchQuoteIdForTask(any(Task.class))).thenReturn(1L).thenReturn(2L);

        //WHEN
        List<QuoteOrderResponse> quoteOrderResponseList = searchQuoteResponseMapper.mapSearchQuoteResponse(searchQuoteResponse,
                                                                                                 quoteOrderMapList);

        //THEN
        Assertions.assertThat(quoteOrderResponseList).usingRecursiveComparison().isEqualTo(expected);
    }

    private SearchQuoteResponse fetchQuoteManagerSearchQuoteResponse() throws IOException {
        return new ObjectMapper().readValue(IOUtils.toString(new ClassPathResource(TEST_DATA_DIR +
                                                                                           "SearchQuoteResponse" +
                                                                                           ".json", this.getClass()).getInputStream(), StandardCharsets.UTF_8), SearchQuoteResponse.class);
    }

    private List<QuoteOrderMapEntity> getQuoteOrderMapList() {
        QuoteOrderMapEntity quoteOrderMap1 = new QuoteOrderMapEntity("83758", "SKY",
                                                                     "BT-QBTDC-11", Boolean.FALSE, "sky");
        quoteOrderMap1.setQuoteGroupId(1L);
        quoteOrderMap1.setStatus(QuoteOrderMapStatus.WSA);
        QuoteOrderMapEntity quoteOrderMap2 = new QuoteOrderMapEntity("83758", "VODAFONE",
                                                                     "BT-QBTDC-12", Boolean.FALSE, "vodafone");
        quoteOrderMap2.setQuoteGroupId(2L);
        quoteOrderMap2.setStatus(QuoteOrderMapStatus.WSA);
        quoteOrderMap1.setQuoteItemMapList(List.of(new QuoteItemMapEntity(1L, 111L, quoteOrderMap1)));
        quoteOrderMap2.setQuoteItemMapList(List.of(new QuoteItemMapEntity(2L, 112L, quoteOrderMap2)));
        return List.of(quoteOrderMap1, quoteOrderMap2);
    }


    private List<QuoteOrderResponse> fetchSearchResponse() {
        return List.of(new QuoteOrderResponse(1L, "Created", "BT-QBTDC-11", LocalDateTime.parse("2023-04-07T11:50:54"),
                "sky", "MONTHLY", List.of(QuoteItemTask.QuoteItemTaskBuilder.newQuoteItemTaskBuilder()
                .withEircode("A00F027")
                .withProduct("WIC")
                .withConnectionType(ConnectionType.ETHERWAY_STANDARD.getPrompt())
                .withQuoteItemId(111L)
                .build())),
            new QuoteOrderResponse(2L, "Created", "BT-QBTDC-12", LocalDateTime.parse("2023-04-07T11:50:54"),
                "vodafone", "MONTHLY", List.of(QuoteItemTask.QuoteItemTaskBuilder.newQuoteItemTaskBuilder()
                .withEircode("A00F027")
                .withProduct("WIC")
                .withConnectionType(ConnectionType.ETHERWAY_STANDARD.getPrompt())
                .withQuoteItemId(112L)
                .build())));
    }
}
