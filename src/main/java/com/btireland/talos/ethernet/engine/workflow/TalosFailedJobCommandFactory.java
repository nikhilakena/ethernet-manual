package com.btireland.talos.ethernet.engine.workflow;

import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.jobexecutor.FailedJobCommandFactory;
import org.springframework.stereotype.Component;

@Component
public class TalosFailedJobCommandFactory implements FailedJobCommandFactory {

    private ApplicationConfiguration config;

    public TalosFailedJobCommandFactory(ApplicationConfiguration config) {
        this.config = config;
    }

    @Override
    public Command<Object> getCommand(String jobId, Throwable exception) {
        return new TalosJobRetryCmd(jobId, exception, config);
    }
}
