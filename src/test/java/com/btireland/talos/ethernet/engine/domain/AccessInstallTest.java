package com.btireland.talos.ethernet.engine.domain;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.util.OrderFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@UnitTest
public class AccessInstallTest {

    @Test
    @DisplayName("Returns Main Site contact for a pbtdc order")
    void getMainContact() {
        AccessInstall accessInstall = OrderFactory.orderWithAccessInstall().getCustomerAccess().getAccessInstall();
        Contact expected = Contact.builder().firstName("xyz").role("Main").build();
        Contact actual = accessInstall.getMainSiteContact();
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("test getMainSiteContact() returns null when uninitialized")
    void testGetMainSiteContactReturnsNull(){
        var accessInstall = AccessInstall.builder().build();
        Assertions.assertThat(accessInstall.getLandlordContact()).isNull();
    }

    @Test
    @DisplayName("Returns Landlord contact for a pbtdc order")
    void getLandlordContact() {
        AccessInstall accessInstall = OrderFactory.orderWithAccessInstall().getCustomerAccess().getAccessInstall();
        Contact expected = Contact.builder().firstName("xyz").role("Landlord").build();
        Contact actual = accessInstall.getLandlordContact();
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("test getLandlordContact() returns null when uninitialized")
    void testGetLandlordContactContactReturnsNull(){
        var accessInstall = AccessInstall.builder().build();
        Assertions.assertThat(accessInstall.getLandlordContact()).isNull();
    }

    @Test
    @DisplayName("Returns Secondary contact for a pbtdc order")
    void getSecondaryContact() {
        AccessInstall accessInstall = OrderFactory.orderWithAccessInstall().getCustomerAccess().getAccessInstall();
        Contact expected = Contact.builder().firstName("xyz").role("Secondary").build();
        Contact actual = accessInstall.getSecondarySiteContact();
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("test getSecondarySiteContact() returns null when uninitialized")
    void testGetSecondarySiteContactReturnsNull(){
        var accessInstall = AccessInstall.builder().build();
        Assertions.assertThat(accessInstall.getSecondarySiteContact()).isNull();
    }

    @Test
    @DisplayName("Returns Building Manager contact for a pbtdc order")
    void getBuildingManagerContact() {
        AccessInstall accessInstall = OrderFactory.orderWithAccessInstall().getCustomerAccess().getAccessInstall();
        Contact expected = Contact.builder().firstName("xyz").role("Building Manager").build();
        Contact actual = accessInstall.getBuildingManagerContact();
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("test getBuildingManagerContact() returns null when uninitialized")
    void testGetBuildingManagerContactReturnsNull(){
        var accessInstall = AccessInstall.builder().build();
        Assertions.assertThat(accessInstall.getBuildingManagerContact()).isNull();
    }

}
