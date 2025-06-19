package com.btireland.talos.quote.facade.dto.notcom;


public class Location {

    private String id;

    private Address address;

    private String networkStatus;

    private String multiEircode;

    public Location() {
    }

    public Location(String id, Address address, String networkStatus, String multiEircode) {
        this.id = id;
        this.address = address;
        this.networkStatus = networkStatus;
        this.multiEircode = multiEircode;
    }

    public Location(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public String getNetworkStatus() {
        return networkStatus;
    }

    public String getMultiEircode() {
        return multiEircode;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ", address=" + address +
                ", networkStatus='" + networkStatus + '\'' +
                ", multiEircode='" + multiEircode + '\'' +
                '}';
    }
}
