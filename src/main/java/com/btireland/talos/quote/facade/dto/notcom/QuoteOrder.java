package com.btireland.talos.quote.facade.dto.notcom;

import java.util.List;


public class QuoteOrder {

    private String recurringFrequency;

    private List<QuoteItem> quoteItems;

    public QuoteOrder() {
    }

    public QuoteOrder(String recurringFrequency, List<QuoteItem> quoteItems) {
        this.recurringFrequency = recurringFrequency;
        this.quoteItems = quoteItems;
    }

    public String getRecurringFrequency() {
        return recurringFrequency;
    }

    public List<QuoteItem> getQuoteItems() {
        return quoteItems;
    }

    @Override
    public String toString() {
        return "QuoteOrder{" +
                "recurringFrequency='" + recurringFrequency + '\'' +
                ", quoteItems=" + quoteItems +
                '}';
    }
}
