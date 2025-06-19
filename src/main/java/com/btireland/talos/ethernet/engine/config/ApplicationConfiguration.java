package com.btireland.talos.ethernet.engine.config;

import com.btireland.talos.ethernet.engine.dto.OaoDetailDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Properties specific to this application.
 * <p>
 * Properties are configured in the {@code application*.yml} file.
 */
@Component
@Data
@ConfigurationProperties(prefix = "application")
@Validated
public class ApplicationConfiguration {
    private List<NotificationToProcessItem> notificationToProcess;
    private Map<String, String> handoverMap;
    private ReportConfiguration report;
    private Patterns patterns;
    private List<String> oaoWithObo;
    private List<String> wicHandover;
    private List<String> oaoWithPrivileges;
    private Bpmn bpmn;
    private OfflinePricingMailConfiguration offlinePricingMail;

    @Getter
    @Setter
    public static class NotificationToProcessItem {
        private String source;
        private List<String> types;
    }

    @Getter
    @Setter
    public static class ReportConfiguration {
        private int activeTimePeriod;
        private int availabilityDateRange;
        private String mailSource;
        private String mailDestination;
    }

    @Getter
    @Setter
    @NotNull
    private Map<String, OaoDetailDTO> oaoDetails;

    private boolean enableReportOverwrite = false;

    private int reportLimit = 200;

    @Getter
    @Setter
    public static class Patterns {
        private String orderNumberPattern;
        private String eircodePattern;
        private String oboPattern;
        private String projectKeyPattern;
    }

    @Getter
    @Setter
    public static class Bpmn {
        private List<Class<?>> noRetryExceptions;
    }

    @Getter
    @Setter
    public static class OfflinePricingMailConfiguration {
        @NotNull
        private String mailSource;
    }
}
