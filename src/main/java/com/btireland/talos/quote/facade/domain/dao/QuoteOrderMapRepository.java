package com.btireland.talos.quote.facade.domain.dao;

import com.btireland.talos.ethernet.engine.repository.JpaRsqlRepository;
import com.btireland.talos.quote.facade.base.enumeration.internal.QuoteOrderMapStatus;
import com.btireland.talos.quote.facade.domain.entity.QuoteOrderMapEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface QuoteOrderMapRepository extends JpaRsqlRepository<QuoteOrderMapEntity, Long> {

     /**
      * Method for retrieving QuoteOrderMapEntity by Order Number and Supplier
      * @param orderNumber Order number of the Order
      * @param supplier Supplier of hte Order
      * @return QuoteOrderMapEntity
      */
     Optional<QuoteOrderMapEntity> findByOrderNumberAndSupplierAndStatusIn(String orderNumber, String supplier, List<QuoteOrderMapStatus> status);

     /**
      * Method for retrieving QuoteOrderMapEntity by Order Number
      * @param orderNumber Order number of the Order
      * @return List of QuoteOrderMapEntity
      */
     List<QuoteOrderMapEntity> findByOrderNumber(String orderNumber);

     /**
      * Method for retrieving all quote order map matching quotegroup ids of a page.
      *
      * @param groupIdList the groupd id list
      * @param pageable     the pageable
      * @return the page
      */
     Page<QuoteOrderMapEntity> findByQuoteGroupIdIn(List<Long> groupIdList, Pageable pageable);


     /**
      * Method for retrieving QuoteOrderMapEntity by quoteGroupId and orderStatusMap not in
      * @param quoteGroupId Quote GroupId of the Order
      * @param quoteOrderMapStatuses List of QuoteOrderMapStatus
      * @return QuoteOrderMapEntity
      */
     Optional<QuoteOrderMapEntity> findByQuoteGroupIdAndStatusNotIn(Long quoteGroupId, List<QuoteOrderMapStatus> quoteOrderMapStatuses);

     /**
      * Find by order number and supplier.
      *
      * @param orderNumber the order number
      * @param oao         the oao
      * @return QuoteOrderMapEntity
      */
     Optional<QuoteOrderMapEntity> findByOrderNumberAndSupplier(String orderNumber,String oao);
}
