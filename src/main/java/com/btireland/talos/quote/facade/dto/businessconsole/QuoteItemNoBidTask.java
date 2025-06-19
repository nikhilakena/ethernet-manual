package com.btireland.talos.quote.facade.dto.businessconsole;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.InputType;

public class QuoteItemNoBidTask {

    @InputType("textarea")
    private String notes;

    QuoteItemNoBidTask() {
        //For Json Serialization / Deserialization
    }

    public QuoteItemNoBidTask(
        @NotNull @Length(min = 1, max = 500) String notes) {
        this.notes = notes;
    }

    @NotNull
    @Length(min = 1, max = 500)
    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return "QuoteItemNoBidTask{" +
            "notes='" + notes + '\'' +
            '}';
    }
}
