package com.btireland.talos.ethernet.engine.facade;

import com.btireland.talos.ethernet.engine.domain.InterventionDetails;
import com.btireland.talos.ethernet.engine.domain.Orders;
import com.btireland.talos.ethernet.engine.dto.InterventionDetailsDTO;
import com.btireland.talos.ethernet.engine.dto.OrdersDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target="orderId", source="order.wagOrderId")
    @Mapping(target="talosOrderId", source="order.id")
    @Mapping(target = "statusNotes", source = "notes")
    @Mapping(target="interventionDetails", ignore = true)

    OrdersDTO toOrderDTO(Orders order);
    InterventionDetailsDTO toInterventionDetailsDTO(InterventionDetails interventionDetails);
}
