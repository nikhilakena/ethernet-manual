package com.btireland.talos.ethernet.engine.workflow;

import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.bpmn.parser.FailedJobRetryConfiguration;
import org.camunda.bpm.engine.impl.cmd.DefaultJobRetryCmd;
import org.camunda.bpm.engine.impl.persistence.entity.JobEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;

import java.util.List;

/**
 * Extends the {@link DefaultJobRetryCmd} to be able to not apply any retry for certain types of exceptions.
 * This allows to create an incident as soon as something goes wrong.
 * By default, if the exception is not one of "application.bpmn.no-retry-exceptions", then the default retry policy is applied.
 */
@Slf4j
public class TalosJobRetryCmd extends DefaultJobRetryCmd {

    private ApplicationConfiguration config;

    public TalosJobRetryCmd(String jobId, Throwable exception, ApplicationConfiguration config) {
        super(jobId, exception);
        this.config = config;
    }

    @Override
    protected FailedJobRetryConfiguration getFailedJobRetryConfiguration(JobEntity job, ActivityImpl activity) {
        // if exception is marked as no retry, we return a configuration with 1 retry, otherwise default behaviour.
        return config.getBpmn().getNoRetryExceptions().stream()
                .filter(clazz -> clazz.isInstance(this.exception) || clazz.isInstance(this.exception.getCause()))
                .map(c -> {
                    log.debug("No retry configured for exception {}, job retries is set to 0", this.exception.getClass().getName());
                    return new FailedJobRetryConfiguration(1, List.of("PT0M"));
                })
                .findAny()
                .orElseGet(() -> {
                    log.debug("Default job retry policy for exception {}", this.exception.getClass().getName());
                    return super.getFailedJobRetryConfiguration(job, activity);
                });
    }
}
