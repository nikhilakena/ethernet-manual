package com.btireland.talos.ethernet.engine.client.asset.notcom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersDTO {

    private Long orderId;

    private Long talosOrderId;

    private String oao;

    private String dataContract;

    private String originatorCode;

    private String resellerTransactionId;

    private String resellerOrderRequestDateTime;

    private String operatorName;

    private String operatorCode;

    private String orderNumber;

    private String createdAt;

    private String serviceType;

    private String serviceAddressEirCode;

    private String serviceLocationAddressId;

    private String locationDescription;

    private String orderType;

    private String installationType;

    private Boolean reconnection;

    private Integer egressGroup;

    private String technicianNotes;

    private String pcn;

    private String internalTrackingOrderReference;

    private String supplierOrderId;

    private String accountNumber;

    private String dueCompletionDate;

    private String mode;

    private String lastNotificationType;

    private String uniqueId;

    private String wasset;

    private String productGroup;

    private String orderStatus;

    private String statusNotes;

    private String cancelOrderId;

    private BigInteger notificationXmlId;

    private BigInteger supplierNotificationId;

}
