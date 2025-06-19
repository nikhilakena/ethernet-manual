package com.btireland.talos.quote.facade.dto.notcom;

import com.btireland.talos.ethernet.engine.enums.ActionFlag;


public class LogicalLink {

    private ActionFlag action;

    private String bandWidth;

    private String profile;

    private String ipRange;

    public LogicalLink() {
    }

    public LogicalLink(ActionFlag action, String bandWidth,String profile,
                       String ipRange) {
        this.action = action;
        this.bandWidth = bandWidth;
        this.profile = profile;
        this.ipRange = ipRange;
    }

    public ActionFlag getAction() {
        return action;
    }

    public String getBandWidth() {
        return bandWidth;
    }

    public String getProfile() {
        return profile;
    }

    public String getIpRange() {
        return ipRange;
    }

    @Override
    public String toString() {
        return "LogicalLink{" +
                "action=" + action +
                ", bandWidth='" + bandWidth + '\'' +
                ", profile='" + profile + '\'' +
                ", ipRange='" + ipRange + '\'' +
                '}';
    }
}
