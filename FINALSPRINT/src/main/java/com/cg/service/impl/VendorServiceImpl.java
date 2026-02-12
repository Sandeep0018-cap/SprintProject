package com.cg.service.impl;

import com.cg.dto.VendorDto;
import com.cg.entity.Vendor;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repository.VendorRepository;
import com.cg.service.VendorService;

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
    @Override
    public VendorDto findById(Long id) {
        Vendor v = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        return new VendorDto(v.getVendorId(), v.getName(), v.getContactName(), v.getPhone(), v.getEmail());
    }

    @Override
    @Transactional
    public VendorDto update(Long id, String name, String contactName, String phone, String email) {

        if (phone != null && !phone.matches("\\d{10}")) {
            throw new BadRequestException("Phone must be exactly 10 digits.");
        }

        Vendor v = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        v.setName(name);
        v.setContactName(contactName);
        v.setPhone(phone);
        v.setEmail(email);

        vendorRepository.save(v);

        return new VendorDto(v.getVendorId(), v.getName(), v.getContactName(), v.getPhone(), v.getEmail());
    }
}