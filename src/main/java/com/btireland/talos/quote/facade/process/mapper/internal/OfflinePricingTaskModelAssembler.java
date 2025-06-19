package com.btireland.talos.quote.facade.process.mapper.internal;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import com.btireland.talos.core.common.rest.exception.checked.TalosNotFoundException;
import com.btireland.talos.quote.facade.dto.businessconsole.OfflinePricingTaskResponse;
import com.btireland.talos.quote.facade.rest.OfflinePricingTaskController;
import com.btireland.talos.quote.facade.rest.QuoteTaskController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class OfflinePricingTaskModelAssembler implements RepresentationModelAssembler<OfflinePricingTaskResponse,
        OfflinePricingTaskResponse> {

    public static final Logger LOGGER = LoggerFactory.getLogger(OfflinePricingTaskModelAssembler.class);

    private static Link modelSelfLink(String id) throws TalosNotFoundException {
        Link self = linkTo(methodOn(QuoteTaskController.class).getQuoteDetails(id)).withSelfRel();
        self = self.andAffordance(afford(methodOn(OfflinePricingTaskController.class).updateQuoteActions(null)))
                .andAffordance(afford(methodOn(OfflinePricingTaskController.class).acceptQuote(id, null)))
                .andAffordance(afford(methodOn(OfflinePricingTaskController.class).rejectQuote(id, null)))
                .andAffordance(afford(methodOn(OfflinePricingTaskController.class).noBidQuote(id, null)));

        return self;
    }

    @Override
    public OfflinePricingTaskResponse toModel(OfflinePricingTaskResponse offlinePricingTask) {

        try {
            return offlinePricingTask.add(modelSelfLink(offlinePricingTask.getTask().getTaskId()));
        } catch (TalosNotFoundException e) {
            LOGGER.error("Not Found exception thrown due to {} with error code {}", e.getErrorMessage(),
                         e.getErrorCode());
        }
        return offlinePricingTask;
    }
}
