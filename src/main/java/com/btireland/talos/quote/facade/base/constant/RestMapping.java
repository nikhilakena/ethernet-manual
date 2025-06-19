package com.btireland.talos.quote.facade.base.constant;

public class RestMapping {

    public static final String CREATE_QUOTE = "/api/v1/qbtdc/quote";
    public static final String EMAIL_BASE_PATH = "/api/v1/dc/email/qbtdc";
    public static final String QUOTE_MANAGER_EMAIL_PATH = "/api/v1/qbtdc/quote/mail/{groupId}";
    public static final String GROUP_PATH_VARIABLE = "groupId";
    public static final String SEARCH_QUOTE = "/api/v1/qbtdc/quote/search";
    public static final String GET_QUOTE_DETAILS = "/api/v1/qbtdc/quote/{quoteId}";
    public static final String UPDATE_QUOTE_NOTIFICATION = "/api/v1/qbtdc/quote/notification/{quoteGroupId}";
    public static final String OFFLINE_PRICING_BASE_PATH= "/api/v1/qbtdc/order";
    public static final String OFFLINE_PRICING_SEARCH_QUOTE_PATH= "/search";
    public static final String OFFLINE_PRICING_FETCH_ALL_QUOTE_PATH= "/";
    public static final String QUOTE_TASK_BASE_PATH= "/api/v1/qbtdc/task";
    public static final String QUOTE_TASK_CHANGE_ASSIGNEE_PATH= "/{taskId}";
    public static final String OFFLINE_PRICING_TASK_BASE_PATH = "/api/v1/qbtdc/offlinepricing/task";
    public static final String OFFLINE_PRICING_TASK_ACCEPT_PATH = "/{taskId}/accept";
    public static final String OFFLINE_PRICING_TASK_REJECT_PATH = "/{taskId}/reject";
    public static final String OFFLINE_PRICING_TASK_NO_BID_PATH = "/{taskId}/nobid";
    public static final String OFFLINE_PRICING_TASK_QUOTE_ACTION_PATH = "/actions";
    public static final String UPDATE_QUOTE = "/api/v1/qbtdc/quote";
    public static final String OFFLINE_PRICING_GET_QUOTE_PATH= "/{taskId}";
    public static final String QUOTE_ID_PATH_VARIABLE = "quoteId";
    public static final String OFFLINE_PRICING_NOTIFICATION_PATH= "/{orderNumber}/{oao}/notification";
    public static final String ORDER_NUMBER_PATH_VARIABLE = "orderNumber";
    public static final String ORDER_ID_PATH_VARIABLE = "groupId";
    public static final String GET_QUOTE_GROUP_DETAILS = "/api/v1/qbtdc/quote/details/{groupId}";
    public static final String OAO_PATH_VARIABLE = "oao";

    RestMapping() {
    }
}
