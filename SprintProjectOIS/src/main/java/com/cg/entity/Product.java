package com.cg.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(
    name = "products",
    uniqueConstraints = @UniqueConstraint(columnNames = "skuId"), // Prevents duplicate inventory identifiers
    indexes = {
        @Index(name = "idx_products_category", columnList = "category_id"), // Optimizes category-based filtering
        @Index(name = "idx_products_brand_name", columnList = "brand_name"), // Improves search performance for brand queries
        @Index(name = "idx_products_vendor", columnList = "vendor_id")      // Accelerates supplier-specific lookups
    }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id") 
    private Long productId; 

    @NotBlank
    @Column(nullable = false, length = 60) // Enforces unique Stock Keeping Unit constraints at schema level
    private String skuId;

    @NotBlank
    @Column(nullable = false, length = 200) // Supports long-form descriptive product titles
    private String name;

    @NotBlank
    @Column(name = "brand_name", nullable = false, length = 80) // Standardizes manufacturer name storage
    private String brand;

    @NotNull
    @Column(nullable = false, precision = 12, scale = 2) // High-precision storage for financial currency values
    private Double price;

    @Min(0)
    @Column(nullable = false) // Ensures inventory levels cannot drop below zero
    private Integer stockQty;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Lazy loading to optimize memory by fetching vendor only when needed
    @JoinColumn(name = "vendor_id", nullable = false) 
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Establishes mandatory link to the product classification
    @JoinColumn(name = "category_id", nullable = false) 
    private Category category;

    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSkuId() {
        return skuId;
    }
    
    public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

    public String getName() {
		return name;
	}
    
	public void setName(String name) {
		this.name = name;
	}
    
	public String getBrand() {
		return brand;
	}
    
	public void setBrand(String brand) {
		this.brand = brand;
	}
    
	public Double getPrice() {
		return price;
	}
    
	public void setPrice(Double price) {
		this.price = price;
	}
    
	public Integer getStockQty() {
		return stockQty;
	}
    
	public void setStockQty(Integer stockQty) {
		this.stockQty = stockQty;
	}
    
	public Vendor getVendor() {
		return vendor;
	}
    
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
}
