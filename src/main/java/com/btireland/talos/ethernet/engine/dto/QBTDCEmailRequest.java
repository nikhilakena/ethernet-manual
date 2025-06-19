package com.btireland.talos.ethernet.engine.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Schema(description = "Request to send qbtdc email")
public class QBTDCEmailRequest {

    @Schema(description = "QBTDC Order Number", required = true, example = "BT-QBTCDC-1234", type = "string")
    private String orderNumber;

    @Schema(description = "narrative to be used in the email", example = "xyz", type = "string")
    private String narrative;

    @Schema(description = "List of email recipients", required = true, type = "array")
    private List<String> emailRecipients;

    @Schema(description = "QBTDC Sync Journey to be decided ", required = true, type = "boolean")
    private boolean sync;

    @Schema(description = "QBTDC Supplier Name", example = "sky", required = true, type = "string")
    private  String supplier;

    public QBTDCEmailRequest() {
    }

    public QBTDCEmailRequest(@NotNull String orderNumber, @Nullable String narrative, @NotEmpty List<String> emailRecipients, @NotNull boolean sync, @NotNull String supplier) {
        this.orderNumber = orderNumber;
        this.narrative = narrative;
        this.emailRecipients = emailRecipients;
        this.sync=sync;
        this.supplier=supplier;
    }

    public QBTDCEmailRequest(@NotBlank String orderNumber,@NotEmpty List<String> emailRecipients, boolean sync,
                             @NotBlank String supplier) {
        this.orderNumber = orderNumber;
        this.emailRecipients = emailRecipients;
        this.sync = sync;
        this.supplier = supplier;
    }

    @NotNull
    public String getOrderNumber() {
        return orderNumber;
    }

    @Nullable
    public String getNarrative() {
        return narrative;
    }

    @NotEmpty
    @Size(min= 1, max= 5)
    public List<@Size(min = 1, max = 40) String> getEmailRecipients() {
        return emailRecipients;
    }

    @NotNull
    public boolean isSync(){
        return sync;
    }

    @NotNull
    public String getSupplier() { return  supplier; }

    @Override
    public String toString() {
        return "QBTDCEmailRequest{" +
            "orderNumber='" + orderNumber + '\'' +
            ", narrative='" + narrative + '\'' +
            ", emailRecipients=" + emailRecipients +
            ", sync=" + sync +
            ", supplier='" + supplier + '\'' +
            '}';
    }
}
