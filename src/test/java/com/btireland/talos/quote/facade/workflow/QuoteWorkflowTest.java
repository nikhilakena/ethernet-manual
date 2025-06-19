package com.btireland.talos.quote.facade.workflow;

import static com.btireland.talos.quote.facade.workflow.QuoteProcessConstants.ACT_ID_GENERATE_QUOTE_PROCESS;
import static com.btireland.talos.quote.facade.workflow.QuoteProcessConstants.ACT_ID_QUOTE_OFFLINE_PRICING_USER_TASK;
import static com.btireland.talos.quote.facade.workflow.QuoteProcessConstants.ACT_ID_SEND_QUOTE_DELAYED_COMPLETION;
import static com.btireland.talos.quote.facade.workflow.QuoteProcessConstants.END_EVENT_QUOTE_DELAYED_COMPLETION;
import static com.btireland.talos.quote.facade.workflow.QuoteProcessConstants.MSG_NAME_QUOTE_DELAYED_COMPLETION;
import static com.btireland.talos.quote.facade.workflow.QuoteProcessConstants.PROC_DEF_KEY;
import static com.btireland.talos.quote.facade.workflow.QuoteProcessConstants.START_EVENT_QUOTE_DELAYED_COMPLETION;
import static com.btireland.talos.quote.facade.workflow.QuoteProcessConstants.VAR_NAME_QUOTE_ID_LIST;
import static java.util.Map.entry;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.execute;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.job;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.quote.facade.process.processor.QuoteWorkflowProcessor;
import com.btireland.talos.quote.facade.workflow.delegate.SendQuoteDelayedEmailServiceTask;
import com.btireland.talos.quote.facade.workflow.delegate.GetRejectedQuotesServiceTask;
import com.btireland.talos.quote.facade.workflow.delegate.SendQuoteCompleteEmailServiceTask;
import com.btireland.talos.quote.facade.workflow.delegate.SendQuoteCompleteNotificationServiceTask;
import com.btireland.talos.quote.facade.workflow.delegate.SendQuoteDelayedCompletionNotificationServiceTask;
import java.util.Arrays;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ProcessEngineExtension.class)
@UnitTest
class QuoteWorkflowTest {

    private ProcessEngine processEngine;

    @Mock
    private QuoteWorkflowProcessor quoteWorkflowProcessor;

    @BeforeEach
    public void setUp() {

        BpmnAwareTests.init(processEngine);

        // set up java delegates to use in the process. We mock every spring dependencies.
        Mocks.register("getRejectedQuotesServiceTask", new GetRejectedQuotesServiceTask());
        Mocks.register("sendQuoteCompleteNotificationServiceTask", new SendQuoteCompleteNotificationServiceTask(
            quoteWorkflowProcessor));
        Mocks.register("sendQuoteDelayedCompletionNotificationServiceTask", new SendQuoteDelayedCompletionNotificationServiceTask(quoteWorkflowProcessor));
        Mocks.register("sendQuoteCompleteEmailServiceTask", new SendQuoteCompleteEmailServiceTask(quoteWorkflowProcessor));
        Mocks.register("sendQuoteDelayedEmailServiceTask", new SendQuoteDelayedEmailServiceTask(quoteWorkflowProcessor));
    }


    @AfterEach
    public void teardown() {
        Mocks.reset();
        BpmnAwareTests.reset();
        processEngine.close();
    }

    ProcessInstance startProcessInstance(String orderId) {
        ProcessInstance processInstance = runtimeService()
                .createProcessInstanceByKey(PROC_DEF_KEY)
            .setVariable(VAR_NAME_QUOTE_ID_LIST, Arrays.asList("1", "2"))
                .businessKey(orderId)
                .execute();

        assertThat(processInstance).isStarted().isWaitingAt(ACT_ID_GENERATE_QUOTE_PROCESS).isNotEnded();
        return processInstance;
    }

    @Test
    @Deployment(resources = "bpmn/Quote.bpmn")
    public void instantiateProcessInstance_WhenAsyncRequestReceived_waitsAtUserTask() {
        //GIVEN

        //WHEN
        ProcessInstance processInstance = startProcessInstance("123");

        //THEN
        assertThat(processInstance)
            .isWaitingAt(ACT_ID_QUOTE_OFFLINE_PRICING_USER_TASK)
            .variables().contains(entry("quoteIdList", Arrays.asList("1", "2")));

    }

    @Test
    @Deployment(resources = "bpmn/Quote.bpmn")
    void processDelayedCompletion_whenDelayedNotificationReceived_waitsAtUserTask() {
        //GIVEN
        ProcessInstance processInstance = startProcessInstance("123");
        // receives Delayed completion notification
        runtimeService().createMessageCorrelation(MSG_NAME_QUOTE_DELAYED_COMPLETION).processInstanceBusinessKey("123").correlate();
        assertThat(processInstance)
                .hasPassed(START_EVENT_QUOTE_DELAYED_COMPLETION)
                .isWaitingAt(ACT_ID_SEND_QUOTE_DELAYED_COMPLETION);

        //WHEN
        // run send DC notification
        execute(job(ACT_ID_SEND_QUOTE_DELAYED_COMPLETION));

        //THEN
        assertThat(processInstance)
                .hasPassed(ACT_ID_SEND_QUOTE_DELAYED_COMPLETION, END_EVENT_QUOTE_DELAYED_COMPLETION)
                .isWaitingAt(ACT_ID_QUOTE_OFFLINE_PRICING_USER_TASK);
    }

}
