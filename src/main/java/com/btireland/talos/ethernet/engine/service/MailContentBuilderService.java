package com.btireland.talos.ethernet.engine.service;

import com.btireland.talos.ethernet.engine.dto.ContactDTO;
import com.btireland.talos.ethernet.engine.dto.EmailDTO;
import com.btireland.talos.ethernet.engine.dto.OrdersDTO;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Service
public class MailContentBuilderService {

    private TemplateEngine templateEngine;

    public MailContentBuilderService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    private static final Map<String, String> deliveryTypesMap;
    private static final Map<String, String> recurringFrequencyMap;
    private static final Map<String, String> cosTypesMap;

    static {
        deliveryTypesMap = new HashMap<>();
        deliveryTypesMap.put("OFF-NET", "3rd Party Supplier");
        deliveryTypesMap.put("ON-NET", "BT On Net");

        recurringFrequencyMap = new HashMap<>();
        recurringFrequencyMap.put("MONTHLY", "Monthly" );
        recurringFrequencyMap.put("ANNUAL", "Annual" );

        cosTypesMap = new HashMap<>();
        cosTypesMap.put("PREMIUM_5" ,"Premium 5 (5% AF)");
        cosTypesMap.put("PREMIUM_10" ,"Premium 10 (10% AF)" );
        cosTypesMap.put("PREMIUM_50" ,"Premium 50 (50% AF)" );
        cosTypesMap.put("PREMIUM_100" ,"Premium 100 (100% AF)" );
        cosTypesMap.put("PREMIUM_EXPRESS" ,"Premium Express (100% EF)" );
        cosTypesMap.put("PRIMARY" ,"Primary (0% AF)" );
    }

    public EmailDTO build(OrdersDTO order) {
        applyUserFriendlyMappingsToOrder(order);

        Context context = new Context();
        context.setVariable("order", order);

        ContactDTO landlordContact = order.getPbtdc().getCustomerAccess().getAccessInstall().getLandlord();
        ContactDTO buildingManagerContact = order.getPbtdc().getCustomerAccess().getAccessInstall().getBuildingManager();

        context.setVariable("landlordContact", landlordContact);
        context.setVariable("buildingManagerContact", buildingManagerContact);

        String emailSubject = "Order - " + order.getOao() + " - " + order.getProductDesc() + " - " + order.getOrderNumber();
        String emailText = templateEngine.process("pbtdc-mail-template", context);

        return EmailDTO.builder().emailSubject(emailSubject).emailText(emailText).build();
    }

    private void applyUserFriendlyMappingsToOrder(OrdersDTO order){
        //If there is no bandwidth mentioned then we have to show 'Existing'
        if(order.getPbtdc().getCustomerAccess().getBandWidth() == null) {
            order.getPbtdc().getCustomerAccess().setBandWidth("Existing");
        }

        // Mapping the user friendly logical link profile class
        if(order.getPbtdc().getLogicalLink() != null) {
            String cos = cosTypesMap.get(order.getPbtdc().getLogicalLink().getProfile());
            order.getPbtdc().getLogicalLink().setProfile(cos);
        }

        //Update Recurring frequency to user-friendly format
        String recurringFrequency = recurringFrequencyMap.get(order.getPbtdc().getQuote().getRecurringFrequency());
        order.getPbtdc().getQuote().setRecurringFrequency(recurringFrequency);

        //Update Delivery Type to user-friendly format
        String deliveryType = deliveryTypesMap.get(order.getPbtdc().getQuote().getAendTargetAccessSupplier());
        order.getPbtdc().getQuote().setAendTargetAccessSupplier(deliveryType);

    }

}