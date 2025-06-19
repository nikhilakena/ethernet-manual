package com.btireland.talos.quote.facade.dto.businessconsole;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class QuoteItemTask {

  @Schema(description = "Id of the quote", required = true, type = "integer", format = "int64")
  private Long quoteItemId;

  @Schema(description = "Task Id for the Camunda Task", example = "0064b171-c3d6-11ed-ad08-0a580a8301ae",
      required = true, type = "string")
  private String taskId;

  @Schema(description = "Assignee for the task", example = "ea1f739d-0824-42eb-8207-6bbde86db4ad",
      required = true, type = "string")
  private String assignee;

  @Schema(description = "Eircode for A end site", example = "D17XD78", required = true, type = "string")
  private String eircode;

  @Schema(description = "Product for the Quote", example = "WIC", allowableValues = {"WIC",
      "WEC"}, required = true, type = "string")
  private String product;

  @Schema(description = "ConnectionType for the Quote", example = "ETHERWAY_STANDARD", allowableValues = {"ETHERWAY_STANDARD", "ETHERWAY_DIVERSE", "ETHERWAY_DIVERSE_PLUS"}, required = true, type = "string")
  private String connectionType;

  @Schema(description = "Term of the Quote Item", example = "1", allowableValues = {"2", "3","4","5"}, required = true,
      type = "string")
  private String term;

  @Schema(description = "IP Range", example = "31", allowableValues = {"31","29","28"}, type = "string")
  private String ipRange;

  @Schema(description = "Action", example = "P", allowableValues = {"PROVIDE", "MODIFY", "CEASE"}, required = true,
      type = "string")
  private String aendAction;

  @Schema(description = "A End Bandwidth", example = "30", required = true, type = "string")
  private String aendBandwidth;

  @Schema(description = "Quote item Sla", example = "STANDARD", allowableValues = {"STANDARD", "ENHANCED", "PREMIUM"
  }, required = true, type = "string")
  private String aendSla;

  @Schema(description = "Delivery", example = "ON-NET", allowableValues = {"ON_NET", "OFF_NET"}, required = true,
      type = "string")
  private String delivery;

  @Schema(description = "Handover Node for B end site", example = "BLANCHARDSTOWN_BP", required = true, type =
      "string")
  private String handoff;

  @Schema(description = "Logical Bandwidth", example = "30", required = true, type = "string")
  private String logicalBandwidth;

  @Schema(description = "Logical Profile", example = "PRIMARY", allowableValues = {"PRIMARY", "PREMIUM_5",
      "PREMIUM_10", "PREMIUM_50", "PREMIUM_100", "PREMIUM_100"}, required = true, type = "string")
  private String profile;

  @Schema(description = "Reject Reason for the quote", example = "Eircode not allowed", required = true, type = "string")
  private String rejectReason;

    @Schema(description = "Group for the quote", example = "G1", type = "string")
    private String group;

  public QuoteItemTask() {
  }

  @NotNull
  public Long getQuoteItemId() {
    return quoteItemId;
  }

  @NotBlank
  public String getTaskId() {
    return taskId;
  }

  @Nullable
  public String getAssignee() {
    return assignee;
  }

  @NotBlank
  public String getEircode() {
    return eircode;
  }

  @NotBlank
  public String getProduct() {
    return product;
  }

  @NotBlank
  public String getConnectionType() {
        return connectionType;
    }

  @NotBlank
  public String getTerm() {
    return term;
  }

  @NotBlank
  public String getIpRange() {
    return ipRange;
  }

  @NotBlank
  public String getAendAction() {
    return aendAction;
  }

  @NotBlank
  public String getAendBandwidth() {
    return aendBandwidth;
  }

  @NotBlank
  public String getAendSla() {
    return aendSla;
  }

  @NotBlank
  public String getDelivery() {
    return delivery;
  }

  @NotBlank
  public String getHandoff() {
    return handoff;
  }

  @NotBlank
  public String getLogicalBandwidth() {
    return logicalBandwidth;
  }

  @NotBlank
  public String getProfile() {
    return profile;
  }

  @Nullable
  public String getRejectReason() {
    return rejectReason;
  }

    @Nullable
    public String getGroup() {
        return group;
    }

  private QuoteItemTask(QuoteItemTaskBuilder builder) {
    quoteItemId = builder.quoteItemId;
    taskId = builder.taskId;
    assignee = builder.assignee;
    eircode = builder.eircode;
    product = builder.product;
    connectionType = builder.connectionType;
    term = builder.term;
    ipRange = builder.ipRange;
    aendAction = builder.aendAction;
    aendBandwidth = builder.aendBandwidth;
    aendSla = builder.aendSla;
    delivery = builder.delivery;
    handoff = builder.handoff;
    logicalBandwidth = builder.logicalBandwidth;
    profile = builder.profile;
    rejectReason = builder.rejectReason;
    group = builder.group;
  }

public static final class QuoteItemTaskBuilder {

    private Long quoteItemId;
    private String taskId;
    private String assignee;
    private String eircode;
    private String product;
    private String connectionType;
    private String term;
    private String ipRange;
    private String aendAction;
    private String aendBandwidth;
    private String aendSla;
    private String delivery;
    private String handoff;
    private String logicalBandwidth;
    private String profile;
    private String rejectReason;
    private String group;

    private QuoteItemTaskBuilder() {
    }

    public static QuoteItemTaskBuilder newQuoteItemTaskBuilder() {
      return new QuoteItemTaskBuilder();
    }

    public QuoteItemTaskBuilder withQuoteItemId(Long quoteItemId) {
      this.quoteItemId = quoteItemId;
      return this;
    }

    public QuoteItemTaskBuilder withTaskId(String taskId) {
      this.taskId = taskId;
      return this;
    }

    public QuoteItemTaskBuilder withAssignee(String assignee) {
      this.assignee = assignee;
      return this;
    }

    public QuoteItemTaskBuilder withEircode(String eircode) {
      this.eircode = eircode;
      return this;
    }

    public QuoteItemTaskBuilder withProduct(String product) {
      this.product = product;
      return this;
    }

    public QuoteItemTaskBuilder withConnectionType(String connectionType) {
        this.connectionType = connectionType;
        return this;
    }

    public QuoteItemTaskBuilder withTerm(String term) {
      this.term = term;
      return this;
    }

    public QuoteItemTaskBuilder withIpRange(String term) {
      this.ipRange = term;
      return this;
    }

    public QuoteItemTaskBuilder withAendAction(String aendAction) {
      this.aendAction = aendAction;
      return this;
    }

    public QuoteItemTaskBuilder withAendBandwidth(String aendBandwidth) {
      this.aendBandwidth = aendBandwidth;
      return this;
    }

    public QuoteItemTaskBuilder withAendSla(String aendSla) {
      this.aendSla = aendSla;
      return this;
    }

    public QuoteItemTaskBuilder withDelivery(String delivery) {
      this.delivery = delivery;
      return this;
    }

    public QuoteItemTaskBuilder withHandoff(String handoff) {
      this.handoff = handoff;
      return this;
    }

    public QuoteItemTaskBuilder withLogicalBandwidth(String logicalBandwidth) {
      this.logicalBandwidth = logicalBandwidth;
      return this;
    }

    public QuoteItemTaskBuilder withProfile(String profile) {
      this.profile = profile;
      return this;
    }

    public QuoteItemTaskBuilder withRejectReason(String rejectReason) {
      this.rejectReason = rejectReason;
      return this;
    }


    public QuoteItemTaskBuilder withGroup(String group) {
        this.group = group;
        return this;
    }

    public QuoteItemTask build() {
      return new QuoteItemTask(this);
    }
  }

  @Override
  public String toString() {
    return "QuoteItemTask{" +
        "quoteItemId=" + quoteItemId +
        ", taskId='" + taskId + '\'' +
        ", assignee='" + assignee + '\'' +
        ", eircode='" + eircode + '\'' +
        ", product='" + product + '\'' +
        ", connectionType='" + connectionType + '\'' +
        ", term='" + term + '\'' +
        ", ipRange='" + ipRange + '\'' +
        ", aendAction='" + aendAction + '\'' +
        ", aendBandwidth='" + aendBandwidth + '\'' +
        ", aendSla='" + aendSla + '\'' +
        ", delivery='" + delivery + '\'' +
        ", handoff='" + handoff + '\'' +
        ", logicalBandwidth='" + logicalBandwidth + '\'' +
        ", profile='" + profile + '\'' +
        ", rejectReason='" + rejectReason + '\'' +
        ", group='" + group + '\'' +
        '}';
  }
}
