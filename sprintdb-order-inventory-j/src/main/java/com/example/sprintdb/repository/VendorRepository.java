package com.example.sprintdb.repository;

import com.example.sprintdb.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Long> {}