package com.btireland.talos.quote.facade.dto.notcom;


public class Address {

    private String fullAddress;

    public Address() {
    }

    public Address(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    @Override
    public String toString() {
        return "Address{" +
                "fullAddress='" + fullAddress + '\'' +
                '}';
    }
}
