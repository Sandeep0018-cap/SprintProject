package com.example.sprintdb.service.impl;

import com.example.sprintdb.dto.VendorDto;
import com.example.sprintdb.entity.Vendor;
import com.example.sprintdb.exception.BadRequestException;
import com.example.sprintdb.exception.ResourceNotFoundException;
import com.example.sprintdb.repository.VendorRepository;
import com.example.sprintdb.service.VendorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public List<VendorDto> findAll() {
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
    @Transactional
    public VendorDto create(String name, String contactName, String phone, String email) {
        if (vendorRepository.findByNameIgnoreCase(name).isPresent()) {
            throw new BadRequestException("Vendor already exists: " + name);
        }
        
        Vendor v = new Vendor();
        v.setName(name);
        v.setContactName(contactName);
        v.setPhone(phone);
        v.setEmail(email);
        
        Vendor saved = vendorRepository.save(v);
        return new VendorDto(saved.getVendorId(), saved.getName(), 
                saved.getContactName(), saved.getPhone(), saved.getEmail());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Vendor v = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found: " + id));
        vendorRepository.delete(v);
    }
}