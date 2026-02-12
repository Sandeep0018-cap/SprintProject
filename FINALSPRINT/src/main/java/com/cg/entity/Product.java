package com.cg.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(
    name = "products",
    uniqueConstraints = @UniqueConstraint(columnNames = "skuId"),
    indexes = {
        @Index(name = "idx_products_category", columnList = "category_id"),
        @Index(name = "idx_products_brand_name", columnList = "brand_name"),
        @Index(name = "idx_products_vendor", columnList = "vendor_id")
    }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")   // DB column
    private Long productId;        // FIXED Java field

    @NotBlank
    @Column(nullable = false, length = 60)
    private String skuId;

    @NotBlank
    @Column(nullable = false, length = 200)
    private String name;

    @NotBlank
    @Column(name = "brand_name", nullable = false, length = 80)
    private String brand;

    @NotNull
    @Column(nullable = false, precision = 12, scale = 2)
    private Double price;

    @Min(0)
    @Column(nullable = false)
    private Integer stockQty;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // ---------- Getters & Setters ----------

    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSkuId() {
        return skuId;
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
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
}