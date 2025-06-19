package com.btireland.talos.ethernet.engine.enums;

import com.btireland.talos.core.common.test.tag.UnitTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class ActionFlagTest {

    @Test
    @DisplayName("Get Action Flag enum by passing key")
    void getActionFlagFromString() {
        ActionFlag expected = ActionFlag.P;
        ActionFlag actual = ActionFlag.fromString("P");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Exception Scenario for get Action Flag enum by passing key")
    void getActionFlagFromStringExceptionScenario() {
        Assertions.assertThatThrownBy(() -> ActionFlag.fromString("Z"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Get Action Flag enum L by passing key")
    void getActionFlagLFromString() {
        ActionFlag expected = ActionFlag.L;
        ActionFlag actual = ActionFlag.fromString("L");

        Assertions.assertThat(actual).isEqualTo(expected);
    }


}