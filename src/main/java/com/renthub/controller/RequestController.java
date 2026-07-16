package com.renthub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.renthub.entity.Property;
import com.renthub.entity.RentalRequest;
import com.renthub.repository.PropertyRepository;
import com.renthub.repository.RentalRequestRepository;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class RequestController {

    @Autowired
    private RentalRequestRepository requestRepository;
    @Autowired
    private PropertyRepository propertyRepository;

    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody RentalRequest request) {
        // Check if request already exists
        boolean alreadyRequested = requestRepository.existsByPropertyIdAndTenantId(
            request.getPropertyId(), request.getTenantId());
        
        if (alreadyRequested) {
            return ResponseEntity.badRequest().body("You have already requested this property!");
        }

        request.setStatus("PENDING");
        return ResponseEntity.ok(requestRepository.save(request));
    }
        @GetMapping("/owner/{ownerId}")
        public List<RentalRequest> getRequestsForOwner(@PathVariable Long ownerId) {
            return requestRepository.findByOwnerId(ownerId);
        }

        // Tenant views their own requests
        @GetMapping("/tenant/{tenantId}")
        public List<RentalRequest> getRequestsForTenant(@PathVariable Long tenantId) {
            return requestRepository.findByTenantId(tenantId);
        }

        // Owner approves a request
        @PutMapping("/{id}/approve")
        public RentalRequest approveRequest(@PathVariable Long id) {
            RentalRequest request = requestRepository.findById(id).orElse(null);
            if (request != null) {
                request.setStatus("APPROVED");
                requestRepository.save(request);
                
                System.out.println("Looking for property ID: " + request.getPropertyId());

                Property property = propertyRepository.findById(request.getPropertyId()).orElse(null);
                
                System.out.println("Property found: " + property);
                if (property != null) {
                    property.setStatus("BOOKED");
                    propertyRepository.save(property);
                }

                return request;
            }
            return null;
        }

        // Owner rejects a request
        @PutMapping("/{id}/reject")
        public RentalRequest rejectRequest(@PathVariable Long id) {
            RentalRequest request = requestRepository.findById(id).orElse(null);
            if (request != null) {
                request.setStatus("REJECTED");
                return requestRepository.save(request);
            }
            return null;    
    }
    
}