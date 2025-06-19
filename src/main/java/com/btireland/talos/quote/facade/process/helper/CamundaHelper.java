package com.btireland.talos.quote.facade.process.helper;

import com.btireland.talos.quote.facade.workflow.QuoteProcessConstants;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CamundaHelper {

    private final RuntimeService runtimeService;
    private final TaskService taskService;


    public CamundaHelper(RuntimeService runtimeService, TaskService taskService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
    }

    /**
     * Method to validate if there is an active camunda instance waiting at user task.
     *
     * @param quoteGroupId the quote group id
     * @return the boolean
     */
    public boolean isActiveOrder(Long quoteGroupId) {
        ProcessInstance processInstance =
                runtimeService.createProcessInstanceQuery().processDefinitionKey(QuoteProcessConstants.PROC_DEF_KEY)
                        .processInstanceBusinessKey(String.valueOf(quoteGroupId)).active().singleResult();
        return isAtUserTask(processInstance);
    }

    /**
     * Method to validate if camunda process is waiting at user task.
     *
     * @param processInstance the {@link ProcessInstance}
     * @return the boolean
     */
    private boolean isAtUserTask(ProcessInstance processInstance) {
        boolean isAtUserTask = false;
        if (processInstance != null) {
            String processInstanceId = processInstance.getProcessInstanceId();
            ActivityInstance activityInstance = processInstanceId != null ? runtimeService.getActivityInstance(processInstanceId) : null;
            ActivityInstance[] childActivityInstances = activityInstance != null ? activityInstance.getChildActivityInstances() : null;
            isAtUserTask = childActivityInstances != null && childActivityInstances.length >= 1 && childActivityInstances[0].getActivityId().equalsIgnoreCase(QuoteProcessConstants.ACT_ID_GENERATE_QUOTE_PROCESS);
        }
        return isAtUserTask;
    }


    /**
     * Fetch list of camunda user tasks along with corresponding quoteId.
     *
     * @param quoteGroupId the quote group id
     * @return the list of map of quoteid and corresponding camunda task
     */
    public List<Task> fetchUserTasks(Long quoteGroupId) {
        //Fetching active tasks for QBTDC Order
        return taskService.createTaskQuery().processDefinitionKey(QuoteProcessConstants.PROC_DEF_KEY)
                .processInstanceBusinessKey(quoteGroupId.toString()).active().list();

    }

    /**
     * Fetch quote id for camunda user task.
     *
     * @param task the camunda user task
     * @return the long quote id
     */
    public Long fetchQuoteIdForTask(Task task) {
        //Fetching the quote Item id associated to user task
        return (Long) runtimeService.getVariables(task.getExecutionId()).get(QuoteProcessConstants.VAR_NAME_QUOTE_ID);

    }

    /**
     * Fetch all quote group id list that are waiting at camunda user task.
     *
     * @return the list of quote group id
     */
    public List<Long> fetchAllActiveOrders() {
        //Fetch all active camunda process
        List<ProcessInstance> activeProcesses = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(QuoteProcessConstants.PROC_DEF_KEY).active().list();

        // Retrieve list of group ids which are awaiting user intervention
        return activeProcesses
                .parallelStream()
                .filter(this::isAtUserTask)
                .map(process -> Long.valueOf(process.getBusinessKey()))
                .collect(Collectors.toList());
    }

    /**
     * Fetch variable quote id for camunda task id.
     *
     * @param taskId the task id
     * @return quote id
     */
    public Long fetchQuoteIdForTaskId(String taskId) {
        Task task = getCamundaTask(taskId);
        return fetchQuoteIdForTask(task);
    }

    /**
     * Retrieves camunda task using task id.
     *
     * @param taskId the task id
     * @return the camunda task {@link Task}
     */
    public Task getCamundaTask(String taskId) {
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

}
