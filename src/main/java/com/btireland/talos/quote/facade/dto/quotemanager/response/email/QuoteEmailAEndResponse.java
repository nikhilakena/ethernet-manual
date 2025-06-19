package com.btireland.talos.quote.facade.dto.quotemanager.response.email;

import com.btireland.talos.quote.facade.base.enumeration.internal.NetworkType;
import com.btireland.talos.quote.facade.base.enumeration.internal.SlaType;
import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;

public class QuoteEmailAEndResponse {

  private String eircode;

  private String address;

  private Float bandwidth;

  private NetworkType targetAccessSupplier;

  private SlaType sla;

  private QuoteEmailAEndResponse(){
    //For Json Deserialization
  }

  public QuoteEmailAEndResponse(@NotBlank String eircode, @Nullable String address, @Nullable Float bandwidth,
      @Nullable NetworkType targetAccessSupplier, @Nullable SlaType sla) {
    this.eircode = eircode;
    this.address = address;
    this.bandwidth = bandwidth;
    this.targetAccessSupplier = targetAccessSupplier;
    this.sla = sla;
  }

  @NotBlank
  public String getEircode() {
    return eircode;
  }

  @Nullable
  public String getAddress() {
    return address;
  }

  @Nullable
  public Float getBandwidth() {
    return bandwidth;
  }

  @Nullable
  public NetworkType getTargetAccessSupplier() {
    return targetAccessSupplier;
  }

  @Nullable
  public SlaType getSla() {
    return sla;
  }

  @Override
  public String toString() {
    return "AEndResponse{" +
        "eircode='" + eircode + '\'' +
        ", address='" + address + '\'' +
        ", bandwidth=" + bandwidth +
        ", targetAccessSupplier=" + targetAccessSupplier +
        ", sla=" + sla +
        '}';
  }
}
