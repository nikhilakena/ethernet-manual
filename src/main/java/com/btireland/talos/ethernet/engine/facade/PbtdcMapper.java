package com.btireland.talos.ethernet.engine.facade;

import com.btireland.talos.ethernet.engine.domain.*;
import com.btireland.talos.ethernet.engine.dto.*;
import com.btireland.talos.ethernet.engine.util.CircuitTypes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PbtdcMapper {

    @Mapping(target = "orderManagerContact", source="orderManager" )
    PbtdcDTO toPbtdcDTO(Pbtdc pbtdc);

    @Mapping(target="circuitType", source="access" ,qualifiedByName = "getEtherwayCircuitType")
    AccessDTO toAccessDTO(Access access);

    @Mapping(target="circuitType", source="logicalLink",qualifiedByName = "getEtherflowCircuitType")
    LogicalLinkDTO toLogicalLinkDTO(LogicalLink logicalLink);


    @Mapping(target="orderId", source="order.wagOrderId")
    @Mapping(target="talosOrderId", source="order.id")
    @Mapping(target = "statusNotes", source = "notes")
    @Mapping(target="pbtdc", source="order")
    @Mapping(target="interventionDetails", ignore = true)
    OrdersDTO toOrderDTO(Pbtdc order);

    Pbtdc toPbtdc(Orders order);

    RejectionDetailsDTO toRejectionDetailsDTO(RejectionDetails rejectionDetails);

    @Named("getEtherwayCircuitType")
    public static String getEtherwayCircuitType(Access access) {
        return access != null ? CircuitTypes.BT_ETHERWAY.name() : null;

    }
    @Named("getEtherflowCircuitType")
    public static String getEtherflowCircuitType(LogicalLink logicalLink) {
        return logicalLink != null ? CircuitTypes.BT_ETHERFLOW.name() : null;

    }

    InterventionDetailsDTO toInterventionDetailsDTO(InterventionDetails interventionDetails);

}
