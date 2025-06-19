package com.btireland.talos.quote.facade.dto.ordermanager;

public class Qbtdcs {

    private Long id;

    private Long orderId;

    private String mode;

    private String applicationDate;

    private String recurringFrequency;

    private String delayReason;

    public Qbtdcs() {
    }

    public Qbtdcs(String mode, String applicationDate, String recurringFrequency) {
        this.mode = mode;
        this.applicationDate = applicationDate;
        this.recurringFrequency = recurringFrequency;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getMode() {
        return mode;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public String getRecurringFrequency() {
        return recurringFrequency;
    }

    public String getDelayReason() {
        return delayReason;
    }

    @Override
    public String toString() {
        return "Qbtdcs{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", mode='" + mode + '\'' +
                ", applicationDate='" + applicationDate + '\'' +
                ", recurringFrequency='" + recurringFrequency + '\'' +
                ", delayReason='" + delayReason + '\'' +
                '}';
    }
}
