package com.btireland.talos.ethernet.engine.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum OrderStatus {
    NETWORK_PLANNING("Network Planning" ,"Network Planning", false, "planning" ),
    ARCHITECTURE_PLANNING("Architecture Planning" ,"Network Planning",false, "planning" ),
    G9_VOICE_BUILD("G9 Voice Build" ,"Network Planning",false, "planning" ),
    HARDWARE_INSTALL("Hardware Install" ,"Hardware Install", false, "testing_cpe_installation" ),
    TEST_AND_TURNUP("Test & Turnup" ,"Test & Turnup", false, "testing_cpe_installation" ),
    FIBRE_SURVEY("Fibre Survey", "Fibre Survey", true, "planning"),
    FIBRE_SURVEY_B_END("Fibre Survey-B End","Fibre Survey-B End",true,"planning"),
    WORK_PACKAGE_GENERATION("Work Package Generation","Work Package Generation",true,"planning"),
    WORK_PACKAGE_GENERATION_B_END("Work Package Generation B-End","Work Package Generation B-End",true,"planning"),
    INTERCONNECT_TECH_INFO("Interconnect Tech Info","Interconnect Tech Info",true,"planning"),
    INTERCONNECT_TECH_INFO_B_END("Interconnect Tech Info-B End","Interconnect Tech Info-B End",true,"planning"),
    GLOBAL_ORDER_ALLOCATION("Global Order Allocation","Global Order Allocation",true,"planning"),
    ETHERFLOW_PENDING_DELIVERY("Etherflow Pending Delivery","Etherflow Pending Delivery",true,"access_installation"),
    PTT_OLO_COMPLETE("PTT/OLO Complete","PTT/OLO Complete",true,"access_installation"),
    ROUTER_CONFIGURATION("Router Configuration","Router Configuration",true,"access_installation"),
    JUMPER_AND_PATCHES("Jumper And Patches","Jumper And Patches",true,"access_installation"),
    JUMPER_AND_PATCHES_2("Jumper And Patches 2","Jumper And Patches 2",true,"access_installation"),
    JUMPER_AND_PATCHES_3("Jumper And Patches 3","Jumper And Patches 3",true,"access_installation"),
    JUMPER_AND_PATCHES_4("Jumper And Patches 4","Jumper And Patches 4",true,"access_installation"),
    JUMPER_AND_PATCHES_5("Jumper And Patches 5","Jumper And Patches 5",true,"access_installation"),
    EXTERNAL_JUMPER_AND_PATCHES("External Jumper And Patches","External Jumper And Patches",true,"access_installation"),
    GIS_APPROVED("GIS Approved","GIS Approved",true,"access_installation"),
    LAYER_3_PLANNING_B_END("Layer 3 Planning - B End","Layer 3 Planning - B End",true,"access_installation"),
    LAYER_3_PLANNING("Layer 3 Planning","Layer 3 Planning",true,"access_installation"),
    INFRASTRUCTURE_BUILD("Infrastructure Build","Infrastructure Build",true,"access_installation"),
    HANDOVER_APPRVL_CONST_OFFICE("Handover Apprvl - Const Office","Handover Apprvl - Const Office",true,"access_installation"),
    PERMISSIONS_ADMIN("Permissions Admin","Permissions Admin",true,"access_installation"),
    PERMISSIONS("Permissions","Permissions",true,"access_installation"),
    TRANSMISSION_EQUIPMENT_CONFIG("Transmission Equipment Config","Transmission Equipment Config",true,"access_installation"),
    CIRCUIT_TEST("Circuit Test","Circuit Test",true,"access_installation"),
    LEC_ORDER_CREATION("LEC Order Creation","LEC Order Creation",true,"planning"),
    LEC_ORDER_CREATION_B_END("LEC Order Creation-B End","LEC Order Creation-B End",true,"planning"),
    OLO_ORDER_SUBMISSION("OLO Order Submission","OLO Order Submission",true,"planning"),
    PENDING_OLO_COMPLETION("Pending OLO Completion","Pending OLO Completion",true,"access_installation"),
    SERVICE_SOFTWARE_BUILD("Service Software Build","Service Software Build",true,"testing_cpe_installation"),
    ARRANGE_CUSTOMER_APPOINTMENT("Arrange Customer Appointment","Arrange Customer Appointment",true,"testing_cpe_installation"),
    TRANSMISSION_NETWORK_ASSURANCE("Transmission Network Assurance","Transmission Network Assurance",true,"testing_cpe_installation"),
    FAULT_ISSUE("Fault Issue","Fault Issue",true,"testing_cpe_installation"),
    SWITCH("Switch","Switch",true,"testing_cpe_installation"),
    TRANSMISSION_SOFTWARE_BUILD("Transmission Software Build","Transmission Software Build",true,"testing_cpe_installation"),
    PLAN_AND_BUILD_ANALYSIS("Plan & Build Analysis","Plan & Build Analysis",false,"planning"),
    PLAN_AND_BUILD_IMPLEMENT("Plan & Build Implement","Plan & Build Implement",true,"testing_cpe_installation"),
    INTEGRITY_CHECK("Integrity Check","Integrity Check",true,"testing_cpe_installation");

    private final String siebelOrderStatus;
    private final String talosOrderStatus;
    private final boolean internalNotificationType;
    private final String businessStatusPhase;
    private static Map<String, String> orderStatusMap;

    OrderStatus(String siebelOrderStatus, String talosOrderStatus, Boolean internalNotificationType, String businessStatusPhase) {
        this.siebelOrderStatus = siebelOrderStatus;
        this.talosOrderStatus = talosOrderStatus;
        this.internalNotificationType = internalNotificationType;
        this.businessStatusPhase = businessStatusPhase;
    }


    public String getSiebelOrderStatus() {
        return siebelOrderStatus;
    }

    public String getTalosOrderStatus() {
        return talosOrderStatus;
    }

    public Boolean isInternalNotificationType() { return internalNotificationType;}

    public String getBusinessStatusPhase() {
        return businessStatusPhase;
    }

    private static void initializeMapping() {
        orderStatusMap = new HashMap<>();
        for (OrderStatus map : OrderStatus.values()) {
            orderStatusMap.put(map.getSiebelOrderStatus(), map.getTalosOrderStatus());
        }
    }

    public static String getTalosOrderStatusForSiebelOrderStatus(String orderStatus) {
        if (orderStatusMap == null) {
            initializeMapping();
        }
        if (orderStatusMap.containsKey(orderStatus)) {
            return orderStatusMap.get(orderStatus);
        }
        return null;
    }

    public static Set<String> getOrderStatusMapForBusinessStatusPhase(String bussinessStatusCategory) {
        Set<String> orderStatusSet = new HashSet<>();

        for (OrderStatus map : OrderStatus.values()) {
            if(map.businessStatusPhase.equalsIgnoreCase(bussinessStatusCategory)) {
                orderStatusSet.add(map.getSiebelOrderStatus());
            }
        }
        return orderStatusSet;
    }

    public static Boolean getInternalNotificationTypeForSiebelOrderStatus(String siebelOrderStatus) {
        for (OrderStatus map : OrderStatus.values()) {
            if(map.siebelOrderStatus.equalsIgnoreCase(siebelOrderStatus)) {
                return  map.internalNotificationType;
            }
        }
        return true;
    }

    public static String getBusinessStatusPhaseForTalosOrderStatus(String talosOrderStatus) {
        for (OrderStatus map : OrderStatus.values()) {
            if(map.talosOrderStatus.equalsIgnoreCase(talosOrderStatus)) {
                return map.businessStatusPhase;
            }
        }
        return null;
    }

}
