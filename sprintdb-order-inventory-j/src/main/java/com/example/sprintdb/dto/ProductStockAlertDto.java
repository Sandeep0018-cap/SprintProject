package com.example.sprintdb.dto;

public class ProductStockAlertDto {
    private Long productId;
    private String brand;
    private String name;
    private Integer availableQty; // computed: stock - sold
    private String severity;      // RED / ORANGE / GREEN

    public ProductStockAlertDto() {}

    public ProductStockAlertDto(Long productId, String brand, String name, Integer availableQty, String severity) {
        this.productId = productId;
        this.brand = brand;
        this.name = name;
        this.availableQty = availableQty;
        this.severity = severity;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getAvailableQty() { return availableQty; }
    public void setAvailableQty(Integer availableQty) { this.availableQty = availableQty; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
}