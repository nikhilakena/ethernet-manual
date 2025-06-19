package com.btireland.talos.quote.facade.service.api;

import com.btireland.talos.core.aop.annotation.LoggedApiMethod;
import com.btireland.talos.core.common.rest.exception.checked.TalosBadRequestException;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteOrderResponse;
import com.btireland.talos.quote.facade.dto.businessconsole.OfflinePricingTaskResponse;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemAcceptTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemNoBidTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemRejectTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteNotificationRequest;
import com.btireland.talos.quote.facade.process.processor.OfflinePricingProcessor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.validation.Valid;
import java.util.List;

@Service
public class OfflinePricingAPI {

    OfflinePricingProcessor offlinePricingProcessor;

    public OfflinePricingAPI(OfflinePricingProcessor offlinePricingProcessor) {
        this.offlinePricingProcessor = offlinePricingProcessor;
    }

    /**
     * Method to search offline pricing qbtdc orders using order number.
     *
     * @param orderNumber the order number
     * @return the {@link List< QuoteOrderResponse >}
     */
    @LoggedApiMethod
    public List<QuoteOrderResponse> searchQuote(String orderNumber) throws TalosNotFoundException {
        return offlinePricingProcessor.searchQuote(orderNumber);
    }

    /**
     * Method to fetch all offline pricing qbtdc orders
     *
     * @param pageable the pageable
     * @return the {@link Page< QuoteOrderResponse >}
     */
    @LoggedApiMethod
    public Page<QuoteOrderResponse> fetchAllQuotes(Pageable pageable) throws TalosNotFoundException {
        return offlinePricingProcessor.fetchAllQuotes(pageable);
    }

    /**
     * Gets quote details for a camunda user task id.
     *
     * @param taskId the task id
     * @return the quote details {@link OfflinePricingTaskResponse}
     */
    @LoggedApiMethod
    public OfflinePricingTaskResponse getQuoteDetails(String taskId) throws TalosNotFoundException {
        return offlinePricingProcessor.getQuoteDetails(taskId);
    }

    /**
     * Method for Changing the assignee for a user task for a quote
     * @param taskId for the camunda process instance task
     * @param assignee Id of the person who has claimed the task. It will beNUll if task is unclaimed
     */
    @LoggedApiMethod
    public void changeAssignee(String taskId, String assignee) {
        offlinePricingProcessor.changeAssignee(taskId, assignee);
    }

    /**
     * Method for Accepting the quote
     * @param quoteItemAcceptTask QuoteItemAcceptTask Object
     * @param taskId Task Id of the Camunda Process Task
     * @throws TalosNotFoundException When fetching Network Type and calling quotemanager
     */
    @LoggedApiMethod
    public void acceptQuote(QuoteItemAcceptTask quoteItemAcceptTask, String taskId) throws TalosNotFoundException {
        offlinePricingProcessor.acceptQuote(quoteItemAcceptTask, taskId);
    }

    /**
     * Method for Rejecting the quote
     * @param quoteItemRejectTask QuoteItemRejectTask Object
     * @param taskId Task Id of the Camunda Process Task
     * @throws TalosNotFoundException When calling quotemanager
     */
    @LoggedApiMethod
    public void rejectQuote(QuoteItemRejectTask quoteItemRejectTask, String taskId) throws TalosNotFoundException {
        offlinePricingProcessor.rejectQuote(quoteItemRejectTask, taskId);
    }

    /**
     * Method for No Bid the quote
     * @param quoteItemNoBidTask QuoteItemNoBidTask Object
     * @param taskId Task Id of the Camunda Process Task
     * @throws TalosNotFoundException When calling quotemanager
     */
    @LoggedApiMethod
    public void noBidQuote(QuoteItemNoBidTask quoteItemNoBidTask, String taskId) throws TalosNotFoundException {
        offlinePricingProcessor.noBidQuote(quoteItemNoBidTask, taskId);
    }

    /**
     * Process incoming business console notification.
     *
     * @param notification the notification {@link QuoteNotificationRequest}
     * @param orderNumber      the wag order number
     * @param oao supplier
     * @throws TalosNotFoundException the talos not found exception
     */
    @LoggedApiMethod
    public void processNotification(@Valid QuoteNotificationRequest notification, String orderNumber, String oao) throws TalosNotFoundException, TalosBadRequestException {
        offlinePricingProcessor.processNotification(notification, orderNumber,oao);
    }
}
