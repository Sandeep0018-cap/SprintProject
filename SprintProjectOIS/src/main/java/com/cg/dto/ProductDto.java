package com.cg.dto;

public class ProductDto { // Data carrier for product management and catalog views

    private Long productId; // Unique database identifier
    private String skuId; // Unique Stock Keeping Unit for inventory tracking
    private String name; // Display name of the product
    private String brand; // Manufacturer or brand name
    private Double price; // Unit selling price
    private Integer stockQty; // Total units received into inventory

    private Long vendorId; // Mandatory reference to the primary supplier
    private Long categoryId; // Reference to the parent classification

    private Integer soldQty; // Analytical: Total units sold based on transactions
    private Integer availableQty; // Analytical: Current sellable stock (Stock - Sold)
    private Long lastPurchaseId; // Analytical: Reference to the most recent sale record

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

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

    public Long getVendorId() { return vendorId; }
    public void setVendorId(Long vendorId) { this.vendorId = vendorId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Integer getSoldQty() { return soldQty; }
    public void setSoldQty(Integer soldQty) { this.soldQty = soldQty; }

    public Integer getAvailableQty() { return availableQty; }
    public void setAvailableQty(Integer availableQty) { this.availableQty = availableQty; }

    public Long getLastPurchaseId() { return lastPurchaseId; }
    public void setLastPurchaseId(Long lastPurchaseId) { this.lastPurchaseId = lastPurchaseId; }
}
