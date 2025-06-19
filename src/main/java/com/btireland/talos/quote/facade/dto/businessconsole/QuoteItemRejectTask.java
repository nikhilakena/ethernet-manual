package com.btireland.talos.quote.facade.dto.businessconsole;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.InputType;

public class QuoteItemRejectTask {

    @InputType("dropdown")
    private String rejectCode;

    @InputType("textarea")
    private String notes;

    QuoteItemRejectTask() {
        //For Json Serialization / Deserialization
    }

    public QuoteItemRejectTask(@NotNull String rejectCode,
        @NotNull @Length(min = 1, max = 500) String notes) {
        this.rejectCode = rejectCode;
        this.notes = notes;
    }

    @NotNull
    public String getRejectCode() {
        return rejectCode;
    }

    @NotNull
    @Length(min = 1, max = 500)
    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return "QuoteItemRejectTask{" +
            "rejectCode='" + rejectCode + '\'' +
            ", notes='" + notes + '\'' +
            '}';
    }
}
