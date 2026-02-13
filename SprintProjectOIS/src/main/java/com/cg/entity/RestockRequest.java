package com.cg.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.cg.enums.RestockStatus;
import java.time.LocalDateTime;

@Entity
@Table(name = "restock_requests",
       indexes = {
           @Index(name = "idx_rr_product", columnList = "product_id"), // Optimizes stock replenishment history for items
           @Index(name = "idx_rr_vendor", columnList = "vendor_id"),   // Accelerates supplier-specific request lookups
           @Index(name = "idx_rr_status", columnList = "status")       // Supports fast dashboard filtering for pending actions
       })
public class RestockRequest {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Primary key for the inventory procurement process
    private Long restock_id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false) // Links the request to the specific item needing replenishment
    private Product product;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false) // Links the request to the designated supplier
    private Vendor vendor;

    @NotNull 
    @Min(1)
    @Column(nullable = false) // Enforces a positive integer for the requested inventory volume
    private Integer requestedQty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12) // Tracks the lifecycle state of the request (e.g., PENDING, COMPLETED)
    private RestockStatus status = RestockStatus.PENDING;

    @Column(nullable = false) // Automatically captures the request submission timestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, length = 50) // Identifies the staff member responsible for initiating the restock
    private String createdByUsername;

    public Long getId() { return restock_id; } // Generic alias for ID-based lookups

    public Long getRestock_id() { return restock_id; }

    public void setRestock_id(Long restock_id) { this.restock_id = restock_id; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Product getProduct() { return product; }

    public void setProduct(Product product) { this.product = product; }

    public Vendor getVendor() { return vendor; }

    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public Integer getRequestedQty() { return requestedQty; }

    public void setRequestedQty(Integer requestedQty) { this.requestedQty = requestedQty; }

    public RestockStatus getStatus() { return status; }

    public void setStatus(RestockStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public String getCreatedByUsername() { return createdByUsername; }

    public void setCreatedByUsername(String createdByUsername) { this.createdByUsername = createdByUsername; }
}
