package com.renthub.controller;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.renthub.entity.User;
import com.renthub.entity.Property;
import com.renthub.repository.PropertyRepository;
import com.renthub.repository.UserRepository;


@RestController
@RequestMapping("/api/properties")
public class PropertyController {
	@Autowired
    private PropertyRepository propertyRepository;
	@Autowired
	private UserRepository userRepository;


    @PostMapping("/add")
    public Property addProperty(@RequestBody Property property) {
        return propertyRepository.save(property);
    }

    @GetMapping
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }
    
    @PostMapping("/add/{ownerId}")
    public Property addProperty(@PathVariable Long ownerId, @RequestBody Property property) {

        User owner = userRepository.findById(ownerId).orElse(null);
        property.setOwner(owner);
        
        property.setStatus("ACTIVE");

        return propertyRepository.save(property);
    }
     
    @GetMapping("/owner/{ownerId}")
    public List<Property> getOwnerProperties(@PathVariable Long ownerId) {
        return propertyRepository.findByOwnerId(ownerId);
    }

    @GetMapping("/city/{city}")
    public List<Property> getByCity(@PathVariable String city) {
        return propertyRepository.findByCity(city);
    }

    @GetMapping("/type/{type}")
    public List<Property> getByType(@PathVariable String type) {
        return propertyRepository.findByType(type);
    }

    @GetMapping("/rent/{maxRent}")
    public List<Property> getByMaxRent(@PathVariable double maxRent) {
        return propertyRepository.findByRentLessThanEqual(maxRent);
    }
    
    @PostMapping("/addWithImage/{ownerId}")
    public Property addPropertyWithImage(
            @PathVariable Long ownerId,
            @RequestParam("image") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam("city") String city,
            @RequestParam("area") String area,
            @RequestParam("rent") double rent,
            @RequestParam("furnished") String furnished,
            @RequestParam("gender") String gender,
            @RequestParam("description") String description
    ) throws IOException {

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get("uploads");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        Property property = new Property();
        property.setTitle(title);
        property.setType(type);
        property.setCity(city);
        property.setArea(area);
        property.setRent(rent);
        property.setFurnished(furnished);
        property.setGender(gender);
        property.setDescription(description);
        property.setImageName(fileName);

        User owner = userRepository.findById(ownerId).orElse(null);
        property.setOwner(owner);

        return propertyRepository.save(property);
    }
    
    @PutMapping("/update/{id}")
    public Property updateProperty(@PathVariable Long id, @RequestBody Property updatedProperty) {

        Property property = propertyRepository.findById(id).orElse(null);

        if (property == null) {
            return null;
        }

        property.setTitle(updatedProperty.getTitle());
        property.setType(updatedProperty.getType());
        property.setCity(updatedProperty.getCity());
        property.setArea(updatedProperty.getArea());
        property.setRent(updatedProperty.getRent());
        property.setFurnished(updatedProperty.getFurnished());
        property.setGender(updatedProperty.getGender());
        property.setDescription(updatedProperty.getDescription());

        return propertyRepository.save(property);
    }
    
    @GetMapping("/{id}")
    public Property getPropertyById(@PathVariable Long id) {
        return propertyRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteProperty(@PathVariable Long id) {
        propertyRepository.deleteById(id);
        return "Property deleted";
    }


    @GetMapping("/filter")
    public List<Property> filterProperties(
            @RequestParam(defaultValue = "") String city,
            @RequestParam(defaultValue = "") String type,
            @RequestParam(defaultValue = "") String furnished,
            @RequestParam(defaultValue = "999999999") double maxRent
    ) {
        return propertyRepository
            .findByCityContainingIgnoreCaseAndTypeContainingIgnoreCaseAndFurnishedContainingIgnoreCaseAndRentLessThanEqual(
                city, type, furnished, maxRent);
    }
    
    
}
