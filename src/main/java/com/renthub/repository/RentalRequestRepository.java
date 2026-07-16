package com.renthub.repository;

import com.renthub.entity.RentalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {

    List<RentalRequest> findByOwnerId(Long ownerId);

    List<RentalRequest> findByTenantId(Long tenantId);
    
    boolean existsByPropertyIdAndTenantId(Long propertyId, Long tenantId);
}