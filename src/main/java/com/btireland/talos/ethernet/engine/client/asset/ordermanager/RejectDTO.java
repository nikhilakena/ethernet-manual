package com.btireland.talos.ethernet.engine.client.asset.ordermanager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RejectDTO {

   private int rejectCode;
   private String rejectReason;
}
