package com.btireland.talos.ethernet.engine.exception;

/**
 * Marker interface to let the workflow know that no retry is allowed if an exception of this type is raised.
 * The workflow engine will create an incident right away without applying any retry policies.
 */
public interface NoRetryInBpmn {
}
