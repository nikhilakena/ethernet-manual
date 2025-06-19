package com.btireland.talos.quote.facade.dto.quotemanager.response.email;

import com.btireland.talos.quote.facade.base.enumeration.internal.ProfileType;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

public class QuoteEmailLogicalResponse {

  private Long ipRange;

  private Float bandwidth;

  private ProfileType profile;

  private QuoteEmailLogicalResponse(){
    //For Json Deserialization
  }

  public QuoteEmailLogicalResponse(@Nullable Long ipRange, @Nullable Float bandwidth, @NotNull ProfileType profile) {
    this.ipRange = ipRange;
    this.bandwidth = bandwidth;
    this.profile = profile;
  }

  @Nullable
  public Long getIpRange() {
    return ipRange;
  }

  @Nullable
  public Float getBandwidth() {
    return bandwidth;
  }

  @NotNull
  public ProfileType getProfile() {
    return profile;
  }

  @Override
  public String toString() {
    return "LogicalResponse{" +
        "ipRange=" + ipRange +
        ", bandwidth=" + bandwidth +
        ", profile=" + profile +
        '}';
  }
}
