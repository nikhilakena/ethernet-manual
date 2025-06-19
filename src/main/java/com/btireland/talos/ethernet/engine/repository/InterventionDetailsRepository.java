package com.btireland.talos.ethernet.engine.repository;

import com.btireland.talos.ethernet.engine.domain.InterventionDetails;
import com.btireland.talos.ethernet.engine.util.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterventionDetailsRepository extends  JpaRsqlRepository<InterventionDetails,Long>{
    @Query( value="SELECT intDet FROM Orders orders LEFT JOIN orders.interventionDetails intDet WHERE orders.id = intDet.orders.id AND intDet.id= :id"  )
    InterventionDetails findByInterventionId(Long id);

    @Query( value="SELECT intDet FROM Orders orders LEFT JOIN orders.interventionDetails intDet WHERE orders.wagOrderId = :orderId AND intDet.status = :status")
    List<InterventionDetails> findByOrderIdAndStatus(@Param("orderId") Long orderId, @Param("status") Status status);

    @Query( value="SELECT COUNT(1) FROM Orders orders LEFT JOIN orders.interventionDetails intDet WHERE intDet.status = :status")
    long countByStatus(@Param("status") Status status);
}
