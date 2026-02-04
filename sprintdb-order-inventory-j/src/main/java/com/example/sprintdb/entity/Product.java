package com.example.sprintdb.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "products",
       uniqueConstraints = @UniqueConstraint(columnNames = "skuId"),
       indexes = {
           @Index(name = "idx_products_category", columnList = "category_id"),
           @Index(name = "idx_products_brand", columnList = "brand")
       })
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 60)
    @Column(nullable = false, length = 60)
    private String skuId;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String name;

    @NotBlank
    @Size(max = 80)
    @Column(nullable = false, length = 80)
    private String brand;

    @NotNull
    @Column(nullable = false, precision = 12, scale = 2)
    private Double price;

    @Min(0)
    @Column(nullable = false)
    private Integer stockQty;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Long getId() { return id; }
    public String getSkuId() { return skuId; }
    public void setSkuId(String skuId) { this.skuId = skuId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getStockQty() { return stockQty; }
    public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
