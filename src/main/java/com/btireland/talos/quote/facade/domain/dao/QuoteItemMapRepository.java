package com.btireland.talos.quote.facade.domain.dao;

import com.btireland.talos.ethernet.engine.repository.JpaRsqlRepository;
import com.btireland.talos.quote.facade.domain.entity.QuoteItemMapEntity;

import java.util.Optional;

public interface QuoteItemMapRepository extends JpaRsqlRepository<QuoteItemMapEntity, Long> {

    /**
     * Method to get quote Item map by quote id.
     *
     * @param quoteId the quote id
     * @return the optional
     */
    Optional<QuoteItemMapEntity> findByQuoteId(Long quoteId);

}
