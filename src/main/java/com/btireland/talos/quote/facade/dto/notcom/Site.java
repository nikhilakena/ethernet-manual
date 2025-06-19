package com.btireland.talos.quote.facade.dto.notcom;

public class Site {

    private Location location;

    public Site() {
    }

    public Site(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Site{" +
                ", location=" + location +
                '}';
    }
}
