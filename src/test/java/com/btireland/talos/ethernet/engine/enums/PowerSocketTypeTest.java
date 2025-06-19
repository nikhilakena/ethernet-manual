package com.btireland.talos.ethernet.engine.enums;

import com.btireland.talos.core.common.test.tag.UnitTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class PowerSocketTypeTest {

    @Test
    @DisplayName("Get PowerSocketType enum KETTLE by passing key")
    void getPowerSocketTypeKettleFromString() {
        PowerSocketType expected = PowerSocketType.KETTLE;
        PowerSocketType actual = PowerSocketType.fromString("KETTLE");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Get PowerSocketType enum THREEPIN by passing key")
    void getPowerSocketTypeThreePinFromString() {
        PowerSocketType expected = PowerSocketType.THREEPIN;
        PowerSocketType actual = PowerSocketType.fromString("3-PIN");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Exception Scenario for get PowerSocketType enum by passing key")
    void getActionFlagFromStringExceptionScenario() {
        Assertions.assertThatThrownBy(() -> PowerSocketType.fromString("ZZZZZ"))
                .isInstanceOf(IllegalArgumentException.class);
    }


}