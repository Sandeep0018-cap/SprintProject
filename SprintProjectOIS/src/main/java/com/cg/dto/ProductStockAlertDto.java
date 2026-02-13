package com.cg.dto;

public class ProductStockAlertDto { // DTO designed for dashboard notifications and inventory warnings

    private Long productId; // Reference to the unique product record
    private String brand; // Brand associated with the low-stock item
    private String name; // Display name of the product requiring attention
    private Integer availableQty; // Net inventory count after subtracting total units sold
    private String severity; // Visual indicator (RED/ORANGE/GREEN) based on stock thresholds

    public ProductStockAlertDto() {} // Standard constructor for framework compatibility

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
