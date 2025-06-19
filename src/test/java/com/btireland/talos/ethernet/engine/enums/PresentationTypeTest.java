package com.btireland.talos.ethernet.engine.enums;

import com.btireland.talos.core.common.test.tag.UnitTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class PresentationTypeTest {

    @Test
    @DisplayName("Get PresentationType enum LC850 by passing key")
    void getPresentationTypeLC850FromString() {
        PresentationType expected = PresentationType.LC850;
        PresentationType actual = PresentationType.fromString("LC850");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Get PresentationType enum RJ45 by passing key")
    void getPresentationTypeRJ45FromString() {
        PresentationType expected = PresentationType.RJ45;
        PresentationType actual = PresentationType.fromString("RJ45");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Get PresentationType enum LC1310 by passing key")
    void getPresentationTypeLC1310FromString() {
        PresentationType expected = PresentationType.LC1310;
        PresentationType actual = PresentationType.fromString("LC1310");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Exception Scenario for get PresentationType enum by passing key")
    void getPresentationTypeFromStringExceptionScenario() {
        Assertions.assertThatThrownBy(() -> PresentationType.fromString("ZZZZZ"))
                .isInstanceOf(IllegalArgumentException.class);
    }


}