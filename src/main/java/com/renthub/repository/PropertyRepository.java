package com.renthub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renthub.entity.Property;

public interface PropertyRepository extends JpaRepository<Property, Long>{
 
	List<Property> findByCity(String city);

	List<Property> findByType(String type);

	List<Property> findByRentLessThanEqual(double rent);
	List<Property> findByOwnerId(Long ownerId); 


List<Property> findByCityContainingIgnoreCaseAndTypeContainingIgnoreCaseAndFurnishedContainingIgnoreCaseAndRentLessThanEqual(
  String city, String type, String furnished, double rent);
}