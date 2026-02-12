package com.cg.dto;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/* ===========================
       FORM DTO
       =========================== */
    public class RestockDto {
        private Long id;

        @NotNull(message = "Product is required")
        private Long productId;

        @NotNull(message = "Vendor cannot be null")
        private Long vendorId;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer requestedQty;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public Long getVendorId() { return vendorId; }
        public void setVendorId(Long vendorId) { this.vendorId = vendorId; }

        public Integer getRequestedQty() { return requestedQty; }
        public void setRequestedQty(Integer requestedQty) { this.requestedQty = requestedQty; }
    }