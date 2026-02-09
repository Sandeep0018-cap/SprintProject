package com.example.sprintdb.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "restock_requests",
       indexes = {
           @Index(name = "idx_rr_product", columnList = "product_id"),
           @Index(name = "idx_rr_vendor", columnList = "vendor_id"),
           @Index(name = "idx_rr_status", columnList = "status")
       })
public class RestockRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restock_id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @NotNull @Min(1)
    @Column(nullable = false)
    private Integer requestedQty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private RestockStatus status = RestockStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, length = 50)
    private String createdByUsername;

    public Long getId() { return restock_id; }
    public Long getRestock_id() {
		return restock_id;
	}
	public void setRestock_id(Long restock_id) {
		this.restock_id = restock_id;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
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