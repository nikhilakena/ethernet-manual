package com.btireland.talos.ethernet.engine.facade;

import com.btireland.talos.ethernet.engine.client.asset.ordermanager.OrdersDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.QbtdcsDTO;
import com.btireland.talos.ethernet.engine.client.asset.ordermanager.QuoteDTO;
import com.btireland.talos.ethernet.engine.domain.LogicalLink;
import com.btireland.talos.ethernet.engine.domain.QbtdcQuote;
import com.btireland.talos.ethernet.engine.domain.Quote;
import com.btireland.talos.ethernet.engine.dto.QuoteItemEmailDTO;
import com.btireland.talos.ethernet.engine.util.DateUtils;
import java.time.LocalDateTime;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface QBTDCOrderMapper {

    @Mapping(target="quoteItemId", source="quoteDTO.id")
    @Mapping(ignore = true, target = "changedAt")
    @Mapping(ignore = true, target = "createdAt")
    Quote toQuote(QuoteDTO quoteDTO, QbtdcsDTO qbtdcsDTO, OrdersDTO ordersDTO);

    @Mapping(target="id", source="quote.quoteItemId")
    @Mapping(ignore = true, target = "changedAt")
    @Mapping(ignore = true, target = "createdAt")
    QuoteDTO toQuoteDTO(Quote quote);

    @Mapping(target="quoteId", source="wagQuoteItemId")
    @Mapping(target="sla", source="customerAccess.sla")
    @Mapping(target="AEndEircode", source="customerAccess.site.location.id")
    @Mapping(target="AEndAddress", source="customerAccess.site.location.address.fullAddress")
    @Mapping(target="bandwidth", source="customerAccess.bandWidth")
    @Mapping(target="handoff", source="wholesalerAccess.site.location.id")
    @Mapping(target="logicalLinkBandwidth", source="logicalLink.bandWidth")
    @Mapping(target="logicalLinkProfile", source="logicalLink.profile")
    @Mapping(target ="AEndTargetAccessSupplier", source = "customerAccess.targetAccessSupplier")
    @Mapping(target = "ipRange", source = "logicalLink.ipRange")
    QuoteItemEmailDTO toQuoteItemEmailDTO(QbtdcQuote qbtdcQuote);

    @Named("getLogicalActionFlag")
    public static String getLogicalActionFlag(LogicalLink logicalLink) {
        return logicalLink != null ? logicalLink.getAction().name() : null;

    }

    @Named("getOfflineQuoted")
    public static String getOfflineQuoted(QbtdcQuote qbtdcQuote) {
        return Boolean.TRUE.equals(qbtdcQuote.getOfflineQuoted()) ? "Y" : "N";
    }

    @Named("formatDate")
    public static String formatDate(LocalDateTime date) {
        return DateUtils.btDateTimeToString(date);
    }

}
