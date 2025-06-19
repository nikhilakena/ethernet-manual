package com.btireland.talos.quote.facade.process.mapper.quotemanager.offlinepricing;

import static com.btireland.talos.quote.facade.base.enumeration.internal.ErrorCode.QUOTE_NOT_FOUND;

import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.base.enumeration.internal.ServiceClassType;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteOrderResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.QuoteGroupOfflinePricingResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.QuoteOfflinePricingResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.SearchQuoteResponse;
import com.btireland.talos.quote.facade.process.helper.CamundaHelper;
import com.btireland.talos.quote.facade.process.helper.QuoteHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SearchQuoteResponseMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchQuoteResponseMapper.class);

    private final CamundaHelper camundaHelper;

    public SearchQuoteResponseMapper(CamundaHelper camundaHelper) {
        this.camundaHelper = camundaHelper;
    }

    /**
     * Map search quote response from Quote manager to List of dto for business console.
     *
     * @param searchQuoteResponse     the search quote response for list of groupids
     * @param quoteOrderMapEntityList the quote order map entity list for list of groupids
     * @return the list dto for business console
     */
    public List<QuoteOrderResponse> mapSearchQuoteResponse(SearchQuoteResponse searchQuoteResponse,
                                                      List<QuoteOrderMapEntity> quoteOrderMapEntityList) throws TalosNotFoundException {
        if (!searchQuoteResponse.getQuoteGroups().isEmpty()) {
            List<QuoteOrderResponse> quoteOrderResponseList = new ArrayList<>();
            for (QuoteGroupOfflinePricingResponse quoteGroup : searchQuoteResponse.getQuoteGroups()) {
                QuoteOrderResponse quoteOrderResponse = mapQuoteGroupOfflinePricingResponse(quoteGroup,
                                                                                  QuoteHelper.fetchQuoteOrderMap(quoteOrderMapEntityList, quoteGroup.getQuoteGroupId()));
                quoteOrderResponseList.add(quoteOrderResponse);
            }
            return quoteOrderResponseList;
        }
        LOGGER.info("No quotes found in Quote Manager for quote groups {}",quoteOrderMapEntityList);
        return Collections.emptyList();
    }

    /**
     * Map quote group offline pricing response qbtdc order dto.
     *
     * @param quoteGroupOfflinePricingResponse the quote group offline pricing response
     * @param quoteOrderMap the quote order map
     * @return the qbtdc order dto
     */
    private QuoteOrderResponse mapQuoteGroupOfflinePricingResponse(
        QuoteGroupOfflinePricingResponse quoteGroupOfflinePricingResponse, QuoteOrderMapEntity quoteOrderMap) throws TalosNotFoundException {

        return new QuoteOrderResponse(quoteOrderMap.getQuoteGroupId(),
            QuoteHelper.getBusinessConsoleStatusfromOrderMapStatus(quoteOrderMap), quoteOrderMap.getOrderNumber(),
            quoteGroupOfflinePricingResponse.getQuoteDate(), quoteOrderMap.getSupplier(),
            quoteGroupOfflinePricingResponse.getQuotes().get(0).getRecurringChargePeriod().name(),
            mapQuoteOfflinePricingResponse(quoteGroupOfflinePricingResponse.getQuotes(),
                quoteGroupOfflinePricingResponse.getQuoteGroupId(),
                quoteOrderMap,
                quoteGroupOfflinePricingResponse.getServiceClass(),
                quoteGroupOfflinePricingResponse.getConnectionType()));
    }


    /**
     * Map quote offline pricing response list.
     *
     * @param quotes              the quotes from quote manager
     * @param quoteGroupId        quote group id
     * @param quoteOrderMapEntity the quote order map entity
     * @param serviceClass        the service class
     * @return the list
     */
    private List<QuoteItemTask> mapQuoteOfflinePricingResponse(List<QuoteOfflinePricingResponse> quotes,
                                                               Long quoteGroupId,
                                                               QuoteOrderMapEntity quoteOrderMapEntity,
                                                               ServiceClassType serviceClass,
                                                               ConnectionType connectionType) throws TalosNotFoundException {
        List<Task> userTaskList = camundaHelper.fetchUserTasks(quoteGroupId);
        List<QuoteItemTask> quoteItemTaskList = new ArrayList<>();
        for (Task task : userTaskList) {
            QuoteItemTask quoteItemTask = createQuoteItemTask(task, quotes, quoteOrderMapEntity, serviceClass, connectionType);
            quoteItemTaskList.add(quoteItemTask);
        }
        return quoteItemTaskList;
    }

    /**
     * Create quote item task quote item task dto.
     *
     * @param task                the task quote id map
     * @param quoteList           the quote list from quote manger
     * @param quoteOrderMapEntity the quote order map entity
     * @param serviceClass        the service class
     * @return the quote item task dto
     */
    private QuoteItemTask createQuoteItemTask(Task task,
                                                 List<QuoteOfflinePricingResponse> quoteList,
                                                 QuoteOrderMapEntity quoteOrderMapEntity,
                                                 ServiceClassType serviceClass,
                                                 ConnectionType connectionType) throws TalosNotFoundException {
        //Retrieving quote id for camunda user task
        Long camundaQuoteId = camundaHelper.fetchQuoteIdForTask(task);

        //Retrieving quote manager quote details corresponding to camunda task
        QuoteOfflinePricingResponse quoteOfflinePricingResponse =
                quoteList.stream()
                        .filter(quote -> quote.getQuoteId().equals(camundaQuoteId))
                        .findFirst()
                        .orElseThrow(() -> new TalosNotFoundException(LOGGER, QUOTE_NOT_FOUND.name(), String.format(
                                                                              "No Quote found for camunda quote id %d",
                                                                                       camundaQuoteId)));

        return QuoteItemTask.QuoteItemTaskBuilder.newQuoteItemTaskBuilder()
                .withTaskId(task.getId())
                .withAssignee(task.getAssignee())
                .withQuoteItemId(QuoteHelper.fetchWagQuoteItemId(quoteOrderMapEntity,
                                                             quoteOfflinePricingResponse.getQuoteId()))
                .withEircode(quoteOfflinePricingResponse.getEircode())
                .withProduct(serviceClass.name())
                .withConnectionType(connectionType.getPrompt())
                .withGroup(quoteOfflinePricingResponse.getGroup())
                .build();
    }

    /**
     * Create qbtdc order dto for business console response.
     *
     * @param orderMap            the {@link QuoteOrderMapEntity}
     * @param searchQuoteResponse the {@link SearchQuoteResponse}
     * @return the {@link QuoteOrderResponse}
     */
    public QuoteOrderResponse createQbtdcOrderDTO(QuoteOrderMapEntity orderMap, SearchQuoteResponse searchQuoteResponse) throws TalosNotFoundException {
        //Retrieving quote group from quote manager response
        QuoteGroupOfflinePricingResponse quoteGroupOfflinePricingResponse = searchQuoteResponse
                .getQuoteGroups()
                .stream()
                .filter(quoteGroupResponse -> quoteGroupResponse
                        .getQuoteGroupId()
                        .equals(orderMap.getQuoteGroupId()))
                .findFirst()
                .orElseThrow(() -> new TalosNotFoundException(LOGGER, QUOTE_NOT_FOUND.name(),
                                                              String.format("No Quote found in quote manager for " +
                                                                                       "quote group id" +
                                                                                       " %d",
                                                                               orderMap.getQuoteGroupId())));
        return mapQuoteGroupOfflinePricingResponse(quoteGroupOfflinePricingResponse, orderMap);
    }


}























































