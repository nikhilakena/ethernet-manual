package com.btireland.talos.quote.facade.dto.businessconsole;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.InputType;

public class QuoteItemAcceptTask {

    @InputType("dropdown")
    private String deliveryType;

    @InputType("text")
    private String recurringCharge;

    @InputType("text")
    private String nonRecurringCharge;

    @InputType("text")
    private String etherflowRecurringCharge;

    @InputType("text")
    private String etherwayRecurringCharge;

    @InputType("textarea")
    private String taskQuoteAcceptNotes;

    QuoteItemAcceptTask() {
        //For Json Serialization / Deserialization
    }

    public QuoteItemAcceptTask(@NotNull String deliveryType,
                               @NotNull String connectionType,
        @NotNull @Length(min = 1, max = 32) String recurringCharge,
        @NotNull @Length(min = 1, max = 32) String nonRecurringCharge,
        @NotNull @Length(min = 1, max = 32) String etherflowRecurringCharge,
        @NotNull @Length(min = 1, max = 32) String etherwayRecurringCharge,
        @Length(max = 500) String taskQuoteAcceptNotes) {
        this.deliveryType = deliveryType;
        this.recurringCharge = recurringCharge;
        this.nonRecurringCharge = nonRecurringCharge;
        this.etherflowRecurringCharge = etherflowRecurringCharge;
        this.etherwayRecurringCharge = etherwayRecurringCharge;
        this.taskQuoteAcceptNotes = taskQuoteAcceptNotes;
    }

    @NotNull
    public String getDeliveryType() {
        return deliveryType;
    }

    @NotNull
    @Length(min = 1, max = 32)
    public String getRecurringCharge() {
        return recurringCharge;
    }

    @NotNull
    @Length(min = 1, max = 32)
    public String getNonRecurringCharge() {
        return nonRecurringCharge;
    }

    @NotNull
    @Length(min = 1, max = 32)
    public String getEtherflowRecurringCharge() {
        return etherflowRecurringCharge;
    }

    @NotNull
    @Length(min = 1, max = 32)
    public String getEtherwayRecurringCharge() {
        return etherwayRecurringCharge;
    }

    @Nullable
    @Length(max = 500)
    public String getTaskQuoteAcceptNotes() {
        return taskQuoteAcceptNotes;
    }

    @Override
    public String toString() {
        return "QuoteItemAcceptTask{" +
            "deliveryType='" + deliveryType + '\'' +
            ", recurringCharge='" + recurringCharge + '\'' +
            ", nonRecurringCharge='" + nonRecurringCharge + '\'' +
            ", etherflowRecurringCharge='" + etherflowRecurringCharge + '\'' +
            ", etherwayRecurringCharge='" + etherwayRecurringCharge + '\'' +
            ", taskQuoteAcceptNotes='" + taskQuoteAcceptNotes + '\'' +
            '}';
    }
}
