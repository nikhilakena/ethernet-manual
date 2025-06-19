package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.core.common.test.tag.UnitTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@UnitTest
public class NotificationTypesTest {


    @Test
    @DisplayName("When notification code is given camunda message name is returned")
    void getMessageNameByNotificationCode() {
        String expected = "SupplierUndeliverable";
        String actual = NotificationTypes.getMessageNameByNotificationCode("U");
        Assertions.assertThat(actual).isEqualTo(expected);

    }
}
