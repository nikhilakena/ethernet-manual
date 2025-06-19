package com.btireland.talos.ethernet.engine.client.asset.notcom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notifications {

    private int id;

    private LocalDateTime changedAt;

    private LocalDateTime createdAt;

    private String source;

    private String type;

    private String reference;

    private long transactionId;

    private byte[] content;

}

