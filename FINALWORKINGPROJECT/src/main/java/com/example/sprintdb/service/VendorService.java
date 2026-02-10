package com.example.sprintdb.service;

import com.example.sprintdb.dto.VendorDto;

import java.util.List;

public interface VendorService {
    List<VendorDto> findAll();
    VendorDto create(String name, String contactName, String phone, String email);
    void delete(Long id);
}