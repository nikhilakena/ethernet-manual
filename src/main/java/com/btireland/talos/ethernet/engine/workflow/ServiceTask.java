package com.btireland.talos.ethernet.engine.workflow;

import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceBadRequestException;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.lang.reflect.ParameterizedType;

@Slf4j
public abstract class ServiceTask<T extends OrderProcessVariables> implements JavaDelegate {

    private final String taskName;

    public ServiceTask() {
        taskName = this.getClass().getSimpleName();
    }

    public void log(DelegateExecution delegateExecution) {
        log.debug(taskName + " invoked for order : " + delegateExecution.getProcessBusinessKey() + " by "
                + "processDefinitionId=" + delegateExecution.getProcessDefinitionId()
                + ", activityId=" + delegateExecution.getCurrentActivityId()
                + ", activityName='" + delegateExecution.getCurrentActivityName() + "'"
                + ", processInstanceId=" + delegateExecution.getProcessInstanceId()
                + ", executionId=" + delegateExecution.getId());
    }

    public abstract void executeTask(long orderId, T processVariables) throws Exception, OrderManagerServiceBadRequestException, OrderManagerServiceNotFoundException;

    @SneakyThrows
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        log(delegateExecution);

        //Retrieve process instance Order data from DB
        Class<T> taskClass = (Class<T>)
                ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
        T processVariables = taskClass.getConstructor(DelegateExecution.class).newInstance(delegateExecution);

        Long orderId = processVariables.getOrderId();
        this.executeTask(orderId, processVariables);

        log.debug("Exited from " + taskName + " for order : " + orderId + " successfully ");
    }
}
