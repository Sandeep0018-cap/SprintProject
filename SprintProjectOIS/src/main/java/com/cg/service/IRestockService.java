package com.cg.service;

import com.cg.dto.RestockDto;
import com.cg.entity.Product;
import com.cg.entity.RestockRequest;
import com.cg.entity.Vendor;
import org.springframework.security.core.Authentication;
import java.util.List;

public interface IRestockService {

	List<Vendor> getAllVendors();

	List<Product> getProductsByVendor(Long vendorId);

	Product getProduct(Long id);

	Vendor getVendor(Long id);

	boolean productBelongsToVendor(Long vendorId, Long productId);

	RestockRequest getRestockById(Long id);

	void createRestockRequest(RestockDto dto, Product product, Vendor vendor, Authentication auth);

	void updateRestockRequest(RestockDto dto, RestockRequest request, Product product, Vendor vendor);

	void deleteRestockRequest(RestockRequest request);
}
