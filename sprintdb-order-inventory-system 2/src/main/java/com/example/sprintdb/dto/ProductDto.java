package com.example.sprintdb.dto;

public class ProductDto {
    private Long id;
    private String skuId;
    private String name;
    private String brand;
    private Double price;
    private Integer stockQty;

    // derived
    private Integer soldQty;
    private Integer availableQty;
    private Long lastPurchaseId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public Integer getSoldQty() { return soldQty; }
    public void setSoldQty(Integer soldQty) { this.soldQty = soldQty; }
    public Integer getAvailableQty() { return availableQty; }
    public void setAvailableQty(Integer availableQty) { this.availableQty = availableQty; }
    public Long getLastPurchaseId() { return lastPurchaseId; }
    public void setLastPurchaseId(Long lastPurchaseId) { this.lastPurchaseId = lastPurchaseId; }
}
