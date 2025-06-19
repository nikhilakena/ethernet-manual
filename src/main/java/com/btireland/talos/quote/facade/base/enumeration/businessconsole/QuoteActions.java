package com.btireland.talos.quote.facade.base.enumeration.businessconsole;

public enum QuoteActions {
    ACCEPT("Accept Quote", "acceptQuote"),
    REJECT("Reject Quote", "rejectQuote"),
    NO_BID("No Bid Quote", "noBidQuote");

    private final String prompt;
    private final String value;

    QuoteActions(String prompt, String value) {
        this.prompt = prompt;
        this.value = value;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getValue() {
        return value;
    }
}
