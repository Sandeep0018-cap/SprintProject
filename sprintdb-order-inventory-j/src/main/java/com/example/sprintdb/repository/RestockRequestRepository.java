package com.example.sprintdb.repository;

import com.example.sprintdb.entity.RestockRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestockRequestRepository extends JpaRepository<RestockRequest, Long> {}