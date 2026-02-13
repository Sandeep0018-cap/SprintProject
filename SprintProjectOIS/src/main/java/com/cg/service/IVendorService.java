package com.cg.service;

import java.util.List;
import com.cg.dto.VendorDto;

public interface IVendorService { // Orchestrates business rules for supplier relationships and procurement contacts

    List<VendorDto> findAll(); // Retrieves the complete directory of registered suppliers

    // Initializes and persists a new vendor profile with comprehensive contact details
    VendorDto create(String name, String contactName, String phone, String email);

    void delete(Long id); // Removes a supplier record while ensuring no active dependency violations

    VendorDto findById(Long id); // Fetches a specific supplier's data for detailed viewing or processing

    // Synchronizes updated contact and organizational information for an existing vendor
    VendorDto update(Long id, String name, String contactName, String phone, String email);
}
