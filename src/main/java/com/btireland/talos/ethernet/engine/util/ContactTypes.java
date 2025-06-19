package com.btireland.talos.ethernet.engine.util;

public enum ContactTypes {
    MAIN_CONTACT("Main"),
    SECONDARY_CONTACT("Secondary"),
    LEAD_DELIVERY_MANAGER("Lead Delivery Manager"),
    ORDER_MANAGER("Order Manager"),
    LANDLORD("Landlord"),
    BUILDING_MANAGER("Building Manager");

    private String contactOwner;

    ContactTypes(String contactOwner) {
        this.contactOwner = contactOwner;
    }

    public String getContactOwner() {
        return contactOwner;
    }

}
