package com.cg.service.impl;

import com.cg.dto.RestockDto;

import com.cg.entity.Product;

import com.cg.entity.RestockRequest;

import com.cg.entity.Vendor;

import com.cg.enums.RestockStatus;

import com.cg.repository.ProductRepository;

import com.cg.repository.RestockRequestRepository;

import com.cg.repository.VendorRepository;
import com.cg.service.IRestockService;

import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Service;

import java.util.Collections;

import java.util.List;

@Service

public class RestockServiceImpl implements IRestockService {

	private final VendorRepository vendorRepository;
	private final ProductRepository productRepository;
	private final RestockRequestRepository restockRequestRepository;

	public RestockServiceImpl(VendorRepository vendorRepository,
			ProductRepository productRepository,
			RestockRequestRepository restockRequestRepository) {
		this.vendorRepository = vendorRepository;
		this.productRepository = productRepository;
		this.restockRequestRepository = restockRequestRepository;

	}

	@Override
	public List<Vendor> getAllVendors() {
		return vendorRepository.findAll();
	}

	@Override
	public List<Product> getProductsByVendor(Long vendorId) {
		return vendorId != null ?
				productRepository.findAllProductsByVendorId(vendorId) : Collections.emptyList();
	}

	@Override
	public Product getProduct(Long id) {
		return productRepository.findById(id).orElse(null);
	}

	@Override
	public Vendor getVendor(Long id) {
		return vendorRepository.findById(id).orElse(null);
	}
	
	@Override
	public boolean productBelongsToVendor(Long vendorId, Long productId) {
		return vendorRepository.existsByVendorIdAndProducts_ProductId(vendorId, productId);
	}

	@Override
	public RestockRequest getRestockById(Long id) {
		return restockRequestRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Restock not found: " + id));
	}

	@Override
	public void createRestockRequest(RestockDto dto, Product product, Vendor vendor, Authentication auth) {
		RestockRequest request = new RestockRequest();
		request.setProduct(product);
		request.setVendor(vendor);
		request.setRequestedQty(dto.getRequestedQty());
		request.setStatus(RestockStatus.PENDING);
		request.setCreatedByUsername(auth != null ? auth.getName() : "system");
		restockRequestRepository.save(request);
	}

	@Override
	public void updateRestockRequest(RestockDto dto, RestockRequest request, Product product, Vendor vendor) {
		request.setProduct(product);
		request.setVendor(vendor);
		request.setRequestedQty(dto.getRequestedQty());
		restockRequestRepository.save(request);
	}

	@Override
	public void deleteRestockRequest(RestockRequest request) {
		restockRequestRepository.delete(request);
	}

}
