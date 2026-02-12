package com.cg.service;

import java.util.List;

import com.cg.dto.VendorDto;

public interface VendorService {
    List<VendorDto> findAll();
    VendorDto create(String name, String contactName, String phone, String email);
    void delete(Long id);
    VendorDto findById(Long id);
    VendorDto update(Long id, String name, String contactName, String phone, String email);
}