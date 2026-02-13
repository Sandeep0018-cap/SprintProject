package com.cg.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RestockDto {
    private Long id;

    @NotNull(message = "Vendor required")
    private Long vendorId;

    @NotNull(message = "Product required")
    private Long productId;

    @Min(value = 1, message = "Requested quantity must be at least 1")
    private Integer requestedQty;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getRequestedQty() {
		return requestedQty;
	}

	public void setRequestedQty(Integer requestedQty) {
		this.requestedQty = requestedQty;
	}

    
    
}

