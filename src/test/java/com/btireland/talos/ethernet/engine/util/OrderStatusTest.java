package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.core.common.test.tag.UnitTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@UnitTest
public class OrderStatusTest {


    @DisplayName("Should return correct Talos Order Status")
    @ParameterizedTest(name = "{index} => {2}: siebelOrderStatus={0}, talosOrderStatus={1}")
    @CsvSource({
            "Network Planning, Network Planning, Received Status is Network Planning",
            "Architecture Planning, Network Planning, Received Status is Architecture Planning",
            "G9 Voice Build, Network Planning, Received status is G9 Voice Build",
            "Hardware Install, Hardware Install, Received status is Hardware Install",
            "Test & Turnup, Test & Turnup, Received status is Test & Turnup",
            "Any Other Status, , Received status is Any Other Status"
    })
    void getMessageNameByNotificationCode(String siebelOrderStatus, String talosOrderStatus, String name) {
        String expected = talosOrderStatus;
        String actual = OrderStatus.getTalosOrderStatusForSiebelOrderStatus(siebelOrderStatus);
        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @DisplayName("Should return internalNotificationType")
    @ParameterizedTest(name = "{index} => {2}: siebelOrderStatus={0}, talosOrderStatus={1}")
    @CsvSource({
            "Network Planning, false, Internal Notification Type is false",
            "Architecture Planning, false, Internal Notification Type is false",
            "G9 Voice Build, false, Internal Notification Type is false",
            "Hardware Install, false, Internal Notification Type is false",
            "Test & Turnup, false, Internal Notification Type is false",
            "Any Other Status, true, Internal Notification Type is true"
    })
    void getMessageNameByNotificationCode(String siebelOrderStatus, Boolean internalNotificationType, String name) {
        Boolean expected = internalNotificationType;
        Boolean actual = OrderStatus.getInternalNotificationTypeForSiebelOrderStatus(siebelOrderStatus);
        Assertions.assertThat(actual).isEqualTo(expected);

    }
}
