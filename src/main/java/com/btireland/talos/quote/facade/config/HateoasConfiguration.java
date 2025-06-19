package com.btireland.talos.quote.facade.config;

import static com.btireland.talos.quote.facade.base.constant.HalFormsConstants.DELIVERY_TYPE;
import static com.btireland.talos.quote.facade.base.constant.HalFormsConstants.QUOTE_ACTION;
import static com.btireland.talos.quote.facade.base.constant.HalFormsConstants.REJECT_CODE;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL_FORMS;
import com.btireland.talos.ethernet.engine.dto.OptionsTypeDTO;
import com.btireland.talos.quote.facade.base.enumeration.businessconsole.QuoteActions;
import com.btireland.talos.quote.facade.base.enumeration.internal.NetworkType;
import com.btireland.talos.quote.facade.base.enumeration.internal.OfflinePricingQuoteRejectCode;
import com.btireland.talos.quote.facade.dto.businessconsole.DefaultQuoteTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemAcceptTask;
import com.btireland.talos.quote.facade.dto.businessconsole.QuoteItemRejectTask;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.mediatype.hal.forms.HalFormsConfiguration;
import org.springframework.hateoas.mediatype.hal.forms.HalFormsOptions;
import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnProperty(name = "application.feature-flags.quote-facade-enabled", havingValue = "true", matchIfMissing =
        false)
@EnableHypermediaSupport(type = {HAL_FORMS})
public class HateoasConfiguration {

    @Bean
    HalFormsConfiguration halFormsConfiguration() {
        HalFormsConfiguration configuration = new HalFormsConfiguration();
        return configuration.withOptions(QuoteItemAcceptTask.class, DELIVERY_TYPE, metadata ->
                HalFormsOptions.inline(Arrays.stream(NetworkType.values())
                                               .map(deliveryType -> OptionsTypeDTO.builder()
                                                       .prompt(deliveryType.getPrompt())
                                                       .value(deliveryType.getValue())
                                                       .build())
                                               .collect(Collectors.toList())))
                .withOptions(DefaultQuoteTask.class, QUOTE_ACTION, metadata ->
                        HalFormsOptions.inline(Arrays.stream(QuoteActions.values())
                                                       .map(quoteAction -> OptionsTypeDTO.builder()
                                                               .prompt(quoteAction.getPrompt())
                                                               .value(quoteAction.getValue())
                                                               .build())
                                                       .collect(Collectors.toList())))
                .withOptions(QuoteItemRejectTask.class, REJECT_CODE, metadata ->
                        HalFormsOptions.inline(Arrays.stream(OfflinePricingQuoteRejectCode.values())
                                                       .map(rejection -> OptionsTypeDTO.builder()
                                                               .prompt(rejection.getRejectReason())
                                                               .value(rejection.getRejectCode())
                                                               .build())
                                                       .collect(Collectors.toList())));

    }
}
