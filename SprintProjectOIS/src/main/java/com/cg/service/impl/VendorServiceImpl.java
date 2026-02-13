package com.cg.service.impl;

import com.cg.dto.VendorDto;
import com.cg.entity.Vendor;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repository.VendorRepository;
import com.cg.service.IVendorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service // Implements core business logic for supplier and vendor relationship management
public class VendorServiceImpl implements IVendorService {

    private final VendorRepository vendorRepository;

    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public List<VendorDto> findAll() {
        // Converts the full list of vendor entities into data transfer objects
        return vendorRepository.findAll().stream()
                .map(v -> new VendorDto(
                        v.getVendorId(),
                        v.getName(),
                        v.getContactName(),
                        v.getPhone(),
                        v.getEmail()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional // Ensures the creation process is atomic and unique name constraints are respected
    public VendorDto create(String name, String contactName, String phone, String email) {
        // Enforces case-insensitive uniqueness for vendor names
        if (vendorRepository.findByNameIgnoreCase(name).isPresent()) {
            throw new BadRequestException("Vendor already exists: " + name);
        }
        
        Vendor v = new Vendor();
        v.setName(name);
        v.setContactName(contactName);
        v.setPhone(phone);
        v.setEmail(email);
        
        Vendor saved = vendorRepository.save(v);
        return mapToDto(saved); // Helper method for consistent DTO conversion
    }

    @Override
    @Transactional // Guarantees that the deletion is finalized in the database
    public void delete(Long id) {
        Vendor v = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found: " + id));
        vendorRepository.delete(v);
    }

    @Override
    public VendorDto findById(Long id) {
        // Fetches a specific supplier and throws an exception if the ID is invalid
        Vendor v = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        return mapToDto(v);
    }

    @Override
    @Transactional // Synchronizes updated contact details to the persistence layer
    public VendorDto update(Long id, String name, String contactName, String phone, String email) {
        // Regex validation to ensure the telephone number follows a specific format
        if (phone != null && !phone.matches("\\d{10}")) {
            throw new BadRequestException("Phone must be exactly 10 digits.");
        }

        Vendor v = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        v.setName(name);
        v.setContactName(contactName);
        v.setPhone(phone);
        v.setEmail(email);

        return mapToDto(vendorRepository.save(v));
    }

    private VendorDto mapToDto(Vendor v) { // Internal utility to standardize entity-to-DTO transformation
        return new VendorDto(v.getVendorId(), v.getName(), v.getContactName(), v.getPhone(), v.getEmail());
    }
}
