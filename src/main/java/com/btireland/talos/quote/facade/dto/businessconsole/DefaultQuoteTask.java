package com.btireland.talos.quote.facade.dto.businessconsole;

import com.btireland.talos.quote.facade.base.enumeration.businessconsole.QuoteActions;
import org.springframework.hateoas.InputType;
import javax.annotation.Nullable;

public class DefaultQuoteTask {

    @InputType("dynamicRadio")
    private QuoteActions quoteAction;

    public DefaultQuoteTask() {
        //For Json Deserialization
    }

    public DefaultQuoteTask(@Nullable QuoteActions quoteAction) {
        this.quoteAction = quoteAction;
    }

    @Nullable
    public QuoteActions getQuoteAction() {
        return quoteAction;
    }

    @Override
    public String toString() {
        return "DefaultQuoteTask{" +
                "quoteAction=" + quoteAction +
                '}';
    }
}
