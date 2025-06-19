package com.btireland.talos.quote.facade.process.mapper.quotemanager.offlinepricing;

import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import com.btireland.talos.quote.facade.base.constant.OfflinePricingConstants;
import com.btireland.talos.quote.facade.domain.entity.QuoteItemMapEntity;
import com.btireland.talos.quote.facade.dto.businessconsole.OfflinePricingTaskResponse;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemTask;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.GetQuoteResponse;
import com.btireland.talos.quote.facade.process.helper.CamundaHelper;
import com.btireland.talos.quote.facade.process.helper.QuoteHelper;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

@Service
public class GetQuoteResponseMapper {

    private final CamundaHelper camundaHelper;
    private final ApplicationConfiguration applicationConfiguration;

    public GetQuoteResponseMapper(CamundaHelper camundaHelper, ApplicationConfiguration applicationConfiguration) {
        this.camundaHelper = camundaHelper;
        this.applicationConfiguration = applicationConfiguration;
    }

    /**
     * Map quote response into OfflinePricingTaskResponse.
     *
     * @param quoteResponse the quote response {@link GetQuoteResponse}
     * @param quoteItemMap  the quote item map {@link QuoteItemMapEntity}
     * @param taskId        the task id
     * @return the {@link OfflinePricingTaskResponse}
     */
    public OfflinePricingTaskResponse mapQuoteResponse(GetQuoteResponse quoteResponse, QuoteItemMapEntity quoteItemMap,
                                                       String taskId) {
        QuoteItemTask quoteItemTask = mapTaskDetails(quoteResponse, quoteItemMap, taskId);
        return new OfflinePricingTaskResponse(quoteItemTask,quoteItemMap.getQuoteOrderMap().getSupplier(),
                                       quoteItemMap.getQuoteOrderMap().getOrderNumber(),
                                       quoteResponse.getRecurringChargePeriod().getValue());

    }

    /**
     * Map task details to QuoteItemTask.
     *
     * @param quoteResponse the quote response {@link GetQuoteResponse}
     * @param quoteItemMap  the quote item map {@link QuoteItemMapEntity}
     * @param taskId        the task id
     * @return the {@link QuoteItemTask}
     */
    private QuoteItemTask mapTaskDetails(GetQuoteResponse quoteResponse, QuoteItemMapEntity quoteItemMap,
                                            String taskId) {
        //Fetching camunda user task
        Task task = camundaHelper.getCamundaTask(taskId);
        return QuoteItemTask.QuoteItemTaskBuilder.newQuoteItemTaskBuilder()
                .withQuoteItemId(quoteItemMap.getWagQuoteId())
                .withTerm(quoteResponse.getQuoteItemTerm() + OfflinePricingConstants.YEARS)
                .withEircode(quoteResponse.getaEndResponse().getEircode())
                .withIpRange(quoteResponse.getLogicalResponse().getIpRange() != null ?
                                 quoteResponse.getLogicalResponse().getIpRange().toString() : null)
                .withAendBandwidth(quoteResponse.getaEndResponse().getBandwidth() != null ?
                                           QuoteHelper.formatBandwidth(quoteResponse.getaEndResponse().getBandwidth()).concat(OfflinePricingConstants.GIGABYTES) : null)
                .withAendSla(quoteResponse.getaEndResponse().getSla()!=null?
                                 quoteResponse.getaEndResponse().getSla().getValue(): null)
                .withAendAction(quoteResponse.getaEndResponse().getAction().getDisplayText())
                .withDelivery(quoteResponse.getaEndResponse().getAccessSupplier()!=null?
                                  quoteResponse.getaEndResponse().getAccessSupplier().getPrompt(): null)
                .withHandoff(applicationConfiguration.getHandoverMap().get(quoteResponse.getbEndResponse().getHandOverNode()) == null ?
                                 quoteResponse.getbEndResponse().getHandOverNode() :
                                 applicationConfiguration.getHandoverMap().get(quoteResponse.getbEndResponse().getHandOverNode()))
                .withLogicalBandwidth(QuoteHelper.formatBandwidth(quoteResponse.getLogicalResponse().getBandwidth()).concat(OfflinePricingConstants.MEGABYTES))
                .withProfile(quoteResponse.getLogicalResponse().getProfile().getValue())
                .withRejectReason(quoteResponse.getRejectionDetailsResponse() != null
                    ? quoteResponse.getRejectionDetailsResponse().getRejectReason() : null)
                .withTaskId(taskId)
                .withProduct(quoteResponse.getServiceClass().name())
                .withConnectionType(quoteResponse.getConnectionType() != null ? quoteResponse.getConnectionType().getPrompt() : null)
                .withGroup(quoteResponse.getGroup())
                .withAssignee(task.getAssignee())
                .build();
    }
}
