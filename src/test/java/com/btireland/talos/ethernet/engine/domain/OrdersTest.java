package com.btireland.talos.ethernet.engine.domain;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.util.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@UnitTest
public class OrdersTest {


    @Test
    @DisplayName("When an order has an open intervention then it returns true")
    void getInterventionFlag() {
        Orders order = Orders.builder().interventionDetails(List.of(InterventionDetails.builder().id(1L).status(Status.OPEN).build())).build();
        Boolean actual = order.getInterventionFlag();
        Assertions.assertThat(actual).isTrue();

    }
}
