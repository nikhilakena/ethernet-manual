package com.btireland.talos.ethernet.engine.domain;

import com.btireland.talos.ethernet.engine.util.ContactTypes;
import com.btireland.talos.quote.facade.base.enumeration.internal.ConnectionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "pbtdcs")
@PrimaryKeyJoinColumn(name="order_id")
public class Pbtdc extends Orders {
    private static final DateTimeFormatter DASH_DAY_FIRST_TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static final DateTimeFormatter DASH_DAY_FIRST_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_access_id")
    private Access customerAccess;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wholesaler_access_id")
    private Access wholesalerAccess;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    @JoinColumn(name = "logical_link_id", referencedColumnName = "id")
    private LogicalLink logicalLink;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    @JoinColumn(name = "quote_item_id", referencedColumnName = "quote_item_id")
    private Quote quote;

    @Embedded
    CustomerDelay customerDelay;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "pbtdc")
    private List<CustomerInternalDetail> customerInternalDetails;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    @JoinColumn(name = "pbtdc_business_status_id", referencedColumnName = "id")
    private PBTDCBusinessStatus  businessStatus;

    private Contact lookupContactByType(ContactTypes contactType){
        return getContacts() != null ? getContacts().stream().filter(contact->contact.getRole().equalsIgnoreCase(contactType.getContactOwner()))
                .findFirst().orElse(null) : null;
    }

    public Contact getLeadDeliveryManager(){
        return lookupContactByType(ContactTypes.LEAD_DELIVERY_MANAGER);
    }

    public Contact getOrderManager(){
        return lookupContactByType(ContactTypes.ORDER_MANAGER);
    }

    public void addContact(Contact orderManager){
        List<Contact> contacts = getContacts() != null ? getContacts() : new ArrayList<>();
        contacts.add(orderManager);
        setContacts(contacts);
    }


    /* The ..asString() methods are included here to assist with the reporting logic.
       they look up the inner objects and guarantee to always return either a value or an empty string.
       This avoids the reporting logic having to do a lot of null checks etc.
       The StringUtils.defaultString utility method does the null check internally.
     */
    public String getOrderManagerNameAsString(){
        StringBuilder orderManagerNameSB = new StringBuilder();
        Contact orderManager = this.getOrderManager();
        if(orderManager != null){
            orderManagerNameSB.append(StringUtils.defaultString(orderManager.getFirstName()))
                    .append(" ")
                    .append(StringUtils.defaultString(orderManager.getLastName()));
        }

        return orderManagerNameSB.toString();
    }

    public String getOrderManagerEmailAsString(){
        Contact orderManager = this.getOrderManager();
        if(orderManager != null){
            return StringUtils.defaultString(orderManager.getEmail());
        }else{
            return "";
        }
    }

    public String getLeadDeliveryManagerAsString(){
        StringBuilder leadDeliveryManagerSB = new StringBuilder();
        Contact leadDeliveryManager = this.getLeadDeliveryManager();
        if(leadDeliveryManager != null){
            leadDeliveryManagerSB.append(StringUtils.defaultString(leadDeliveryManager.getFirstName()))
                .append(" ")
                .append(StringUtils.defaultString(leadDeliveryManager.getLastName()))
                .append(" ")
                .append(StringUtils.defaultString(leadDeliveryManager.getNumber()))
                .append(" ")
                .append(StringUtils.defaultString(leadDeliveryManager.getEmail()));
        }
        return leadDeliveryManagerSB.toString();
    }

    public String getCustomerAccessSiteNameAsString(){
        if(this.getCustomerAccess() != null &&
                this.getCustomerAccess().getSite() != null){
            return StringUtils.defaultString(this.getCustomerAccess().getSite().getName());
        }else{
            return "";
        }
    }

    public String getConnectionTypeAsString(){
        if (ConnectionType.ETHERWAY_STANDARD.getValue().equalsIgnoreCase(this.getConnectionType())) {
            return ConnectionType.ETHERWAY_STANDARD.getPrompt();
        } else if (ConnectionType.ETHERWAY_DIVERSE.getValue().equalsIgnoreCase(this.getConnectionType())) {
            return ConnectionType.ETHERWAY_DIVERSE.getPrompt();
        } else if (ConnectionType.ETHERWAY_DIVERSE_PLUS.getValue().equalsIgnoreCase(this.getConnectionType())) {
            return ConnectionType.ETHERWAY_DIVERSE_PLUS.getPrompt();
        } else {
            return "Unknown : " + this.getConnectionType();
        }
    }


    public String getProductAsString(){
        if(this.getServiceClass() != null){
            if(this.getServiceClass().equalsIgnoreCase("WEC")){
                return "BT Etherflow";
            }else if (this.getServiceClass().equalsIgnoreCase("WIC")){
                return "BT Etherflow IP";
            }else{
                return "Unknown : " + this.getServiceClass();
            }
        }else{
            return "";
        }
    }
    public String getLogicalLinkBandwidthAsString(){
        if(this.getLogicalLink() != null){
            return StringUtils.defaultString(this.getLogicalLink().getBandWidth());
        }else{
            return "";
        }
    }

    public String getAccessSpeedAsString(){
        if(this.getQuote() != null && this.getQuote().getAendBandwidth() != null){
            return StringUtils.defaultString(this.getQuote().getAendBandwidth());
        }else if (this.customerAccess != null){
            return StringUtils.defaultString(this.customerAccess.getBandWidth());
        } else {
            return "";
        }
    }

    public String getPortSpeedAsString(){
        if (this.getLogicalLink() != null && StringUtils.isNotEmpty(this.getLogicalLink().getBandWidth())) {
            return StringUtils.defaultString(this.getLogicalLink().getBandWidth());
        } else if (this.getQuote() != null) {
            return StringUtils.defaultString(this.getQuote().getLogicalBandwidth());
        } else {
            return "";
        }
    }

    public String getLogicalLinkCircuitReferenceAsString(){
        if(this.getLogicalLink() != null) {
            return StringUtils.defaultString(this.getLogicalLink().getCircuitReference());
        }else{
            return "";
        }
    }

    public String getLogicalLinkVlanAsString(){
        if(this.getLogicalLink() != null) {
            return StringUtils.defaultString(this.getLogicalLink().getVlan());
        }else{
            return "";
        }
    }

    public String getCustomerAccessTerminatingEquipmentPortSettingAsString() {
        if(this.getCustomerAccess() != null &&
                this.getCustomerAccess().getTerminatingEquipment() != null) {
            return StringUtils.defaultString(this.getCustomerAccess().getTerminatingEquipment().getPortSetting());
        }else{
            return "";
        }
    }

    public String getCustomerAccessTerminatingEquipmentPortSpeedAsString() {
        if(this.getCustomerAccess() != null &&
                this.getCustomerAccess().getTerminatingEquipment() != null) {
            return StringUtils.defaultString(this.getCustomerAccess().getTerminatingEquipment().getPortSpeed());
        }else{
            return "";
        }
    }

    public String getCustomerAccessTerminatingEquipmentPresentationAsString() {
        if(this.getCustomerAccess() != null &&
                this.getCustomerAccess().getTerminatingEquipment() != null) {
            return StringUtils.defaultString(this.getCustomerAccess().getTerminatingEquipment().getPresentation());
        }else{
            return "";
        }
    }

    public String getCustomerAccessCircuitReferenceAsString() {
        if(this.getCustomerAccess() != null) {
            return StringUtils.defaultString(this.getCustomerAccess().getCircuitReference());
        }else{
            return "";
        }
    }
    public String getWholesaleAccessTerminatingEquipmentPresentationAsString() {
        if(this.getWholesalerAccess() != null &&
                this.getWholesalerAccess().getTerminatingEquipment() != null) {
            return StringUtils.defaultString(this.getWholesalerAccess().getTerminatingEquipment().getPresentation());
        }else{
            return "";
        }
    }

    public String getWholesalerAccessCircuitReferenceAsString() {
        if(this.getWholesalerAccess() != null) {
            return StringUtils.defaultString(this.getWholesalerAccess().getCircuitReference());
        }else{
            return "";
        }
    }

    public String getBusinessStatusDeliveryOnTrackAsString(){
        if(this.getBusinessStatus() != null &&
                this.getBusinessStatus().getDeliveryOnTrack() != null) {
            return (this.getBusinessStatus().getDeliveryOnTrack() == true) ? "Yes" : "No";
        }else{
            return "";
        }
    }

    public String getBusinessStatusOrderEntryValidationStatusAsString(){
        if(this.getBusinessStatus() != null) {
            return StringUtils.defaultString(this.getBusinessStatus().getOrderEntryAndValidationStatus());
        }else{
            return "";
        }
    }

    public String getBusinessStatusPlanningStatusAsString(){
        if(this.getBusinessStatus() != null) {
            return StringUtils.defaultString(this.getBusinessStatus().getPlanningStatus());
        }else{
            return "";
        }
    }

    public String getBusinessStatusAccessInstallationAsString(){
        if(this.getBusinessStatus() != null) {
            return StringUtils.defaultString(this.getBusinessStatus().getAccessInstallation());
        }else{
            return "";
        }
    }

    public String getBusinessStatusTestingCpeInstallationAsString(){
        if(this.getBusinessStatus() != null) {
            return StringUtils.defaultString(this.getBusinessStatus().getTestingCpeInstallation());
        }else{
            return "";
        }
    }

    public String getBusinessStatusServiceCompleteAndOperationalAsString(){
        if(this.getBusinessStatus() != null) {
            return StringUtils.defaultString(this.getBusinessStatus().getServiceCompleteAndOperational());
        }else{
            return "";
        }
    }

    public String getSupplierOrderSiebelNumberAsString(){
        if(this.getSupplierOrder() != null) {
            return StringUtils.defaultString(this.getSupplierOrder().getSiebelNumber());
        }else{
            return "";
        }
    }

    public String getCustomerInternalDetailsAsString(){
        StringBuilder customerInternalDetailsStringBuilder = new StringBuilder();
        if(this.getCustomerInternalDetails() != null){
            for(var detail : this.getCustomerInternalDetails()) {
                customerInternalDetailsStringBuilder.append(StringUtils.defaultString(detail.getName()))
                .append(" : ")
                .append(StringUtils.defaultString(detail.getValue()))
                .append("\n");
            }
        }
        return customerInternalDetailsStringBuilder.toString();
    }

    public String getOperatorNameAsString(){
        StringBuilder operatorNameBuilder = new StringBuilder();
        operatorNameBuilder.append(StringUtils.defaultString(this.getOperatorName()));

        if(this.getOperatorCode() != null) {
            operatorNameBuilder.append(" / ")
                .append(this.getOperatorCode());
        }
        return operatorNameBuilder.toString();
    }

    // the address is taken from the completion data if available, otherwise from the quote.

    public String getCustomerSiteDetailsAsString(){
        StringBuilder preCompletionAddressSB = new StringBuilder();

        if(this.getCustomerAccess() != null &&
                this.getCustomerAccess().getSite() != null &&
                this.getCustomerAccess().getSite().getLocation() != null &&
                this.getCustomerAccess().getSite().getLocation().getAddress() != null) {
            Location customerAccessLocation = this.getCustomerAccess().getSite().getLocation();
            Address customerAccessLocationAddress = customerAccessLocation.getAddress();

            if (customerAccessLocationAddress.getLocationLine1() != null) {
                // Address Line 1 is present, indicates a Completion received so use these details.
                preCompletionAddressSB.append(StringUtils.defaultString(customerAccessLocationAddress.getLocationLine1())).append('\n')
                    .append(StringUtils.defaultString(customerAccessLocationAddress.getLocationLine2())).append('\n')
                    .append(StringUtils.defaultString(customerAccessLocationAddress.getLocationLine3())).append('\n')
                    .append(StringUtils.defaultString(customerAccessLocationAddress.getCity())).append('\n')
                    .append(StringUtils.defaultString(customerAccessLocationAddress.getCounty())).append('\n');
            } else {
                // fall back on the quote/quote completion
                preCompletionAddressSB.append(StringUtils.defaultString(customerAccessLocation.getAddress().getFullAddress())).append('\n');
            }
            preCompletionAddressSB.append(StringUtils.defaultString(customerAccessLocation.getId()));
        }
        return preCompletionAddressSB.toString();
    }

    public String getCustomerContactDetailsAsString(){
        StringBuilder customerSiteContactSB = new StringBuilder();
        if(this.getCustomerAccess()!=null && this.getCustomerAccess().getAccessInstall()!=null && this.getCustomerAccess().getAccessInstall().getMainSiteContact() != null ) {
                customerSiteContactSB.append(this.getCustomerAccess().getAccessInstall().getMainSiteContact().formatToString());
                customerSiteContactSB.append('\n');
        }

        return customerSiteContactSB.toString();
    }

    public String getWholesaleSiteDetailsAsString(Map<String,String> handOverMap){
        StringBuilder wholesaleSiteAddress=new StringBuilder("");
        if(this.getWholesalerAccess() != null &&
            this.getWholesalerAccess().getSite() != null &&
            this.getWholesalerAccess().getSite().getLocation() != null){
            String handoverDescription = handOverMap.get(this.getWholesalerAccess().getSite().getLocation().getId());
            handoverDescription = handoverDescription!=null? handoverDescription : this.getWholesalerAccess().getSite().getLocation().getId();
            wholesaleSiteAddress.append(StringUtils.defaultString(handoverDescription));
            Address address = this.getWholesalerAccess().getSite().getLocation().getAddress();
            if ( address!= null) {
                // Address is present, indicates a Completion received so use these details.
                wholesaleSiteAddress.append('\n').append(StringUtils.defaultString(address.getLocationLine1())).append('\n')
                        .append(StringUtils.defaultString(address.getLocationLine2())).append('\n')
                        .append(StringUtils.defaultString(address.getLocationLine3())).append('\n')
                        .append(StringUtils.defaultString(address.getCity())).append('\n')
                        .append(StringUtils.defaultString(address.getCounty()));
            }
            return wholesaleSiteAddress.toString();
        }else{
            return "";
        }
    }

    public String getWholesaleContactDetailsAsString(){
        StringBuilder wholesaleSiteContactSB = new StringBuilder();
        if(this.getWholesalerAccess()!=null && this.getWholesalerAccess().getAccessInstall()!=null && this.getWholesalerAccess().getAccessInstall().getMainSiteContact() != null) {

                wholesaleSiteContactSB.append(this.getWholesalerAccess().getAccessInstall().getMainSiteContact().formatToString());
                wholesaleSiteContactSB.append('\n');

        }
        return wholesaleSiteContactSB.toString();
    }

    private String getDateTimeAsString(LocalDateTime dateTime){
        return dateTime != null ? dateTime.format(DASH_DAY_FIRST_TIMESTAMP_FORMAT) : "";
    }

    private String getDateAsString(LocalDateTime date){
        return date != null ? getDateAsString(date.toLocalDate()): "";
    }
    private String getDateAsString(LocalDate date){
        return date != null ? date.format(DASH_DAY_FIRST_FORMAT) : "";
    }

    public String dateReceivedByDeliveryAsString(){
        return getDateAsString(this.getDateReceivedByDelivery());
    }

    public String getEstimatedCompletionDateAsString(){
        return getDateAsString(this.getEstimatedCompletionDate());
    }

    public String getDueCompletionDateAsString(){
        return getDateAsString(this.getDueCompletionDate());
    }

    public String getDeliveryDateAsString(){
        if(this.getBusinessStatus() != null) {
            return getDateAsString(this.getBusinessStatus().getDeliveryDate());
        }else{
            return "";
        }
    }

    public String getNotesReceivedDateAsString(){
        return getDateTimeAsString(this.getNotesReceivedDate());
    }

    public String getLogicalLinkGlanIdAsString() {
        if(this.logicalLink != null) {
            return StringUtils.defaultString(this.logicalLink.getGlanId());
        }
        else {
            return "";
        }
    }

    public String getCustomerAccessGlanIdAsString() {
        if(this.customerAccess != null) {
            return StringUtils.defaultString(this.customerAccess.getGlanId());
        }
        else {
            return "";
        }
    }

    public String getWholesalerAccessGlanIdAsString() {
        if(this.wholesalerAccess != null) {
            return StringUtils.defaultString(this.wholesalerAccess.getGlanId());
        }
        else {
            return "";
        }
    }

    public String getOaoAsString(){
        return StringUtils.defaultString(this.getOao());
    }
}
