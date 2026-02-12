package com.cg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.RestockRequest;

public interface RestockRequestRepository extends JpaRepository<RestockRequest, Long> {}