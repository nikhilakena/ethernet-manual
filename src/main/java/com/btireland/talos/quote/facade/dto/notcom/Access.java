package com.btireland.talos.quote.facade.dto.notcom;

import com.btireland.talos.ethernet.engine.enums.ActionFlag;

public class Access {

    private ActionFlag action;

    private String bandWidth;

    private String sla;

    private Site site;

    private String targetAccessSupplier;

    private String supplier;

    private String serviceClass;

    public Access() {
    }

    public Access(ActionFlag action, String bandWidth, String sla, Site site,
                  String targetAccessSupplier, String supplier) {
        this.action = action;
        this.bandWidth = bandWidth;
        this.sla = sla;
        this.site = site;
        this.targetAccessSupplier = targetAccessSupplier;
        this.supplier = supplier;
    }

    public Access(ActionFlag action, String bandWidth, String sla, Site site, String targetAccessSupplier,
                  String supplier, String serviceClass) {
        this.action = action;
        this.bandWidth = bandWidth;
        this.sla = sla;
        this.site = site;
        this.targetAccessSupplier = targetAccessSupplier;
        this.supplier = supplier;
        this.serviceClass = serviceClass;
    }

    public Access(ActionFlag action, Site site) {
        this.action = action;
        this.site = site;
    }

    public ActionFlag getAction() {
        return action;
    }

    public String getBandWidth() {
        return bandWidth;
    }

    public String getSla() {
        return sla;
    }

    public Site getSite() {
        return site;
    }

    public String getTargetAccessSupplier() {
        return targetAccessSupplier;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    @Override
    public String toString() {
        return "Access{" +
                "action=" + action +
                ", bandWidth='" + bandWidth + '\'' +
                ", sla='" + sla + '\'' +
                ", site=" + site +
                ", targetAccessSupplier='" + targetAccessSupplier + '\'' +
                ", supplier='" + supplier + '\'' +
                ", serviceClass='" + serviceClass + '\'' +
                '}';
    }
}
