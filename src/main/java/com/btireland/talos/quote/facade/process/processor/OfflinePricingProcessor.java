package com.btireland.talos.quote.facade.process.processor;

import static com.btireland.talos.quote.facade.base.constant.OfflinePricingConstants.QUOTE_GROUP_ID;
import com.btireland.talos.core.common.rest.exception.checked.TalosBadRequestException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteOrderResponse;
import com.btireland.talos.ethernet.engine.enums.OfflinePricingRejectCode;
import com.btireland.talos.ethernet.engine.util.NotificationTypes;
import com.btireland.talos.quote.facade.base.enumeration.internal.ErrorCode;
import com.btireland.talos.quote.facade.base.enumeration.internal.NetworkType;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteOrderMapStatus;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteStateType;
import com.btireland.talos.quote.facade.connector.rest.quotemanager.QuoteManagerClient;
import com.btireland.talos.quote.facade.domain.dao.QuoteItemMapRepository;
import com.btireland.talos.quote.facade.domain.entity.QuoteItemMapEntity;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import com.btireland.talos.quote.facade.dto.businessconsole.OfflinePricingTaskResponse;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemAcceptTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemNoBidTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemRejectTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteNotificationRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.request.offlinepricing.SearchQuoteRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.request.offlinepricing.UpdateQuoteRequest;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.GetQuoteResponse;
import com.btireland.talos.quote.facade.dto.quotemanager.response.offlinepricing.SearchQuoteResponse;
import com.btireland.talos.quote.facade.process.helper.CamundaHelper;
import com.btireland.talos.quote.facade.process.helper.QuoteHelper;
import com.btireland.talos.quote.facade.process.mapper.internal.OfflinePricingTaskModelAssembler;
import com.btireland.talos.quote.facade.process.mapper.quotemanager.offlinepricing.GetQuoteResponseMapper;
import com.btireland.talos.quote.facade.process.mapper.quotemanager.offlinepricing.NotificationRequestMapper;
import com.btireland.talos.quote.facade.process.mapper.quotemanager.offlinepricing.SearchQuoteResponseMapper;
import com.btireland.talos.quote.facade.workflow.QuoteProcessConstants;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfflinePricingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfflinePricingProcessor.class);
    public static final String CREATED_AT = "createdAt";

    private final QuoteHelper quoteHelper;
    private final QuoteManagerClient quoteManagerClient;
    private final SearchQuoteResponseMapper searchQuoteResponseMapper;
    private final CamundaHelper camundaHelper;
    private TaskService taskService;
    private RuntimeService runtimeService;
    private final GetQuoteResponseMapper quoteResponseMapper;
    private final OfflinePricingTaskModelAssembler assembler;
    private final QuoteItemMapRepository quoteItemMapRepository;

    public OfflinePricingProcessor(QuoteHelper quoteHelper, QuoteManagerClient quoteManagerClient,
        SearchQuoteResponseMapper searchQuoteResponseMapper, CamundaHelper camundaHelper,
        TaskService taskService, RuntimeService runtimeService,
        GetQuoteResponseMapper quoteResponseMapper, OfflinePricingTaskModelAssembler assembler,
        QuoteItemMapRepository quoteItemMapRepository) {
        this.quoteHelper = quoteHelper;
        this.quoteManagerClient = quoteManagerClient;
        this.searchQuoteResponseMapper = searchQuoteResponseMapper;
        this.camundaHelper = camundaHelper;
        this.taskService = taskService;
        this.runtimeService = runtimeService;
        this.quoteResponseMapper = quoteResponseMapper;
        this.assembler = assembler;
        this.quoteItemMapRepository = quoteItemMapRepository;
    }

    @Transactional(readOnly = true)
    public List<QuoteOrderResponse> searchQuote(String orderNumber) throws TalosNotFoundException {
        List<QuoteOrderMapEntity> quoteOrderMapList = quoteHelper.fetchQuoteOrderMapByOrderNumber(orderNumber);
        //Filtering orders which are waiting at user task
        List<Long> quoteGroupIdList = quoteOrderMapList
                .stream()
                .map(QuoteOrderMapEntity::getQuoteGroupId)
                .filter(camundaHelper::isActiveOrder)
                .collect(Collectors.toList());
        if (!quoteGroupIdList.isEmpty()) {
            SearchQuoteRequest searchQuoteRequest = new SearchQuoteRequest(quoteGroupIdList);
            //Fetching quotes with matching quoteGroupId from Quote Manager
            SearchQuoteResponse searchQuoteResponse = quoteManagerClient.searchQuotes(searchQuoteRequest);

            return searchQuoteResponseMapper.mapSearchQuoteResponse(searchQuoteResponse, quoteOrderMapList);

        }
        LOGGER.info("No qbtdc orders found with order number {}",orderNumber);
        return Collections.emptyList();
    }

    /**
     * Fetch all quotes which requires offline quoting.
     *
     * @param pageable the {@link Pageable}
     * @return the {@link Page< QuoteOrderResponse >}
     */
    @Transactional(readOnly = true)
    public Page<QuoteOrderResponse> fetchAllQuotes(Pageable pageable) throws TalosNotFoundException {
        //Creating a new Pageable object with same configuration but sort property as quotegroupid
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                                  Sort.by(pageable.getSort().getOrderFor(CREATED_AT).getDirection(), QUOTE_GROUP_ID));
        //Retrieving order map for all orders waiting at user task
        Page<QuoteOrderMapEntity> quoteOrderMapPage = fetchAllActiveOrders(pageable);
        if(quoteOrderMapPage.hasContent()) {
            List<Long> groupIdList = quoteOrderMapPage
                    .stream()
                    .map(QuoteOrderMapEntity::getQuoteGroupId)
                    .collect(Collectors.toList());
            SearchQuoteRequest searchQuoteRequest = new SearchQuoteRequest(groupIdList);
            //Fetching quotes with matching quoteGroupId from Quote Manager
            SearchQuoteResponse searchQuoteResponse = quoteManagerClient.searchQuotes(searchQuoteRequest);
            if(!searchQuoteResponse.getQuoteGroups().isEmpty()){
                return quoteOrderMapPage.map(orderMap -> {
                    try {
                        return searchQuoteResponseMapper.createQbtdcOrderDTO(orderMap, searchQuoteResponse);
                    } catch (TalosNotFoundException e) {
                       LOGGER.warn("Error while fetching async quotes: {}",e.getErrorMessage(),e);
                       return null;
                    }
                });
            }
        }
        return Page.empty();
    }

    /**
     * Method for Changing the assignee for a user task for a quote
     * @param taskId for the camunda process instance task
     * @param assignee Id of the person who has claimed the task. It will beNUll if task is unclaimed
     */
    @Transactional
    public void changeAssignee(String taskId, String assignee) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        String quoteGroupId = processInstance.getBusinessKey();

        Optional<QuoteOrderMapEntity> quoteOrderMap = quoteHelper.fetchQuoteOrderMapByQuoteGroupIdAndStatusNotAsDelayed(Long.valueOf(quoteGroupId));

        if (quoteOrderMap.isPresent()) {
            QuoteOrderMapEntity orderMap = quoteOrderMap.get();
            orderMap.setStatus(assignee == null ? QuoteOrderMapStatus.WSA :
                QuoteOrderMapStatus.CLAIMED);
        }

        task.setAssignee(assignee);
        taskService.saveTask(task);
    }

    /**
     * Fetch order map page for all qbtdc orders that are awaiting user intervention in camunda workflow.
     *
     * @param pageable the pageable
     * @return the {@link Page<QuoteOrderMapEntity>}
     */
    private Page<QuoteOrderMapEntity> fetchAllActiveOrders(Pageable pageable) {
        //Retrieving all qbtdc orders wih active camunda process
        List<Long> quoteGroupIdList = camundaHelper.fetchAllActiveOrders();
        return quoteHelper.fetchQuoteOrderMapByGroupIds(quoteGroupIdList,pageable);
    }

    /**
     * Gets quote details for a given camunda task.
     *
     * @param taskId the task id
     * @return the quote details {@link OfflinePricingTaskResponse}
     */
    @Transactional(readOnly = true)
    public OfflinePricingTaskResponse getQuoteDetails(String taskId) throws TalosNotFoundException {
        Long quoteId = camundaHelper.fetchQuoteIdForTaskId(taskId);
        //Fetch quote details from Quote Manager
        GetQuoteResponse quoteResponse = quoteManagerClient.getQuoteDetails(quoteId);
        QuoteItemMapEntity quoteItemMap = quoteHelper.fetchQuoteItemMapByQuoteId(quoteId);
        OfflinePricingTaskResponse offlinePricingTask = quoteResponseMapper.mapQuoteResponse(quoteResponse,
                                                                                           quoteItemMap, taskId);
        return assembler.toModel(offlinePricingTask);
    }

    /**
     * Process incoming business console notification.
     *
     * @param notification the notification {@link QuoteNotificationRequest}
     * @param orderNumber      the wag order number
     * @param oao the supplier
     * @throws TalosNotFoundException
     */
    @Transactional
    public void processNotification(QuoteNotificationRequest notification, String orderNumber, String oao) throws TalosNotFoundException, TalosBadRequestException {
        if (NotificationTypes.DELAYED.getNotificationCode().equalsIgnoreCase(notification.getType())) {
            QuoteOrderMapEntity quoteOrderMap = quoteHelper.updateOrderStatus(orderNumber,
                                                                              oao, QuoteOrderMapStatus.DELAY);
            quoteManagerClient.updateQuoteNotification(NotificationRequestMapper.map(notification), quoteOrderMap.getQuoteGroupId());
            runtimeService
                    .createMessageCorrelation(QuoteProcessConstants.MSG_NAME_QUOTE_DELAYED_COMPLETION)
                    .processInstanceBusinessKey(QuoteProcessConstants.PROC_DEF_KEY)
                    .processInstanceBusinessKey(String.valueOf(quoteOrderMap.getQuoteGroupId()))
                    .correlate();
        }
    }
    /**
     * Method for Accepting the quote
     * @param quoteItemAcceptTask QuoteItemAcceptTask Object
     * @param taskId Task Id of the Camunda Process Task
     * @throws TalosNotFoundException While fetching Network Type and also when calling quotemanager
     */
    @Transactional
    public void acceptQuote(QuoteItemAcceptTask quoteItemAcceptTask, String taskId) throws TalosNotFoundException {
        String quoteId = getQuoteId(taskId);

        NetworkType networkType = QuoteHelper.fetchNetworkType(quoteItemAcceptTask.getDeliveryType());

        UpdateQuoteRequest updateQuoteRequest = new UpdateQuoteRequest(Long.valueOf(quoteId), QuoteStateType.QUOTED,
            networkType, quoteItemAcceptTask.getRecurringCharge(), quoteItemAcceptTask.getNonRecurringCharge(),
            quoteItemAcceptTask.getEtherflowRecurringCharge(), quoteItemAcceptTask.getEtherwayRecurringCharge(),
            quoteItemAcceptTask.getTaskQuoteAcceptNotes());

        quoteManagerClient.updateQuoteForUserTask(updateQuoteRequest);

        setOfflineQuoted(Long.valueOf(quoteId));

        taskService.complete(taskId);
    }

    /**
     * Method for Rejecting the quote
     * @param quoteItemRejectTask QuoteItemRejectTask Object
     * @param taskId Task Id of the Camunda Process Task
     * @throws TalosNotFoundException When calling quotemanager
     */
    @Transactional
    public void rejectQuote(QuoteItemRejectTask quoteItemRejectTask, String taskId) throws TalosNotFoundException {
        String quoteId = getQuoteId(taskId);

        UpdateQuoteRequest updateQuoteRequest = new UpdateQuoteRequest(Long.valueOf(quoteId), QuoteStateType.REJECTED,
            quoteItemRejectTask.getRejectCode(),
            OfflinePricingRejectCode.getRejectReasonByCode(quoteItemRejectTask.getRejectCode()),
            quoteItemRejectTask.getNotes());

        quoteManagerClient.updateQuoteForUserTask(updateQuoteRequest);

        setOfflineQuoted(Long.valueOf(quoteId));

        taskService.complete(taskId);
    }

    /**
     * Method for No Bid the quote
     * @param quoteItemNoBidTask QuoteItemNoBidTask Object
     * @param taskId Task Id of the Camunda Process Task
     * @throws TalosNotFoundException When calling quotemanager
     */
    @Transactional
    public void noBidQuote(QuoteItemNoBidTask quoteItemNoBidTask, String taskId) throws TalosNotFoundException {
        String quoteId = getQuoteId(taskId);

        UpdateQuoteRequest updateQuoteRequest = new UpdateQuoteRequest(Long.valueOf(quoteId), QuoteStateType.NO_BID,
            quoteItemNoBidTask.getNotes());

        quoteManagerClient.updateQuoteForUserTask(updateQuoteRequest);

        setOfflineQuoted(Long.valueOf(quoteId));

        taskService.complete(taskId);
    }

    /**
     * Method to get QuoteId from the Camunda Process Task instance variables
     * @param taskId TaskId of the Camunda Process Task
     * @return quoteId QuoteId of the quote
     */
    private String getQuoteId(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return runtimeService.getVariables(task.getExecutionId()).get(QuoteProcessConstants.VAR_NAME_QUOTE_ID).toString();
    }

    /**
     * Method for updating offline quoted in the QuoteItemMapEntity
     * @param quoteId
     */
    private void setOfflineQuoted(Long quoteId) throws TalosNotFoundException {
        QuoteItemMapEntity quoteItemMapEntity = quoteItemMapRepository.findByQuoteId(quoteId)
            .orElseThrow(() -> new TalosNotFoundException(LOGGER, ErrorCode.QUOTE_ITEM_MAP_NOT_FOUND.name(),
                String.format("No Quote Item Map found for the %d", quoteId)));
        quoteItemMapEntity.setOfflineQuoted(true);
    }

}
