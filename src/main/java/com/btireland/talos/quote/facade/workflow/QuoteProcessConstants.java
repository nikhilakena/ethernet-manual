package com.btireland.talos.quote.facade.workflow;

public final class QuoteProcessConstants {

    private QuoteProcessConstants() {
    }

    public static final String PROC_DEF_KEY = "CreateQuoteOfflinePricingOrderProcess";

    public static final String START_EVENT_OFFLINE_QUOTE_PROCESS = "StartEvent_OfflineQuoteProcess";
    public static final String START_EVENT_QUOTE_DELAYED_COMPLETION = "StartEvent_QuoteDelayedCompletion";

    public static final String END_EVENT_OFFLINE_QUOTE_PROCESS = "EndEvent_OfflineQuoteProcess";
    public static final String END_EVENT_OFFLINE_QUOTE = "EndEvent_OfflineQuote";
    public static final String END_EVENT_QUOTE_DELAYED_COMPLETION = "EndEvent_QuoteDelayedCompletion";

    public static final String ACT_ID_GET_REJECTED_QUOTES = "Activity_GetRejectedQuotes";
    public static final String ACT_ID_QUOTE_OFFLINE_PRICING_USER_TASK = "Activity_QuoteOffinePricingUserTask";
    public static final String ACT_ID_SEND_QUOTE_COMPLETE_NOTIFICATION = "Activity_SendQuoteCompleteNotification";
    public static final String ACT_ID_GENERATE_QUOTE_PROCESS = "Activity_GenerateQuoteOfflineProcess#multiInstanceBody";
    public static final String ACT_ID_SEND_QUOTE_DELAYED_COMPLETION = "Activity_SendQuoteDelayedCompletion";

    public static final String VAR_NAME_QUOTE_ID_LIST= "quoteIdList";
    public static final String VAR_NAME_QUOTE_ID= "quoteId";

    public static final String MSG_NAME_QUOTE_DELAYED_COMPLETION = "Msg_SupplierDelayed";

}
