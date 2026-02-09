package com.example.sprintdb.dto;

import com.example.sprintdb.entity.PaymentMode;

public class CustomerRowDto {
    private Long customerId;
    private String name;
    private String phone;
    private String city;

    // Optional (computed/filled in service)
    private String purchasedItem;

    // Fields often shown in table
    private String lastPurchasedProductName;
    private String lastPurchasedBrand;

    private PaymentMode paymentMode;
    private String transactionId;
    private Long lastPurchaseId;

    public CustomerRowDto() {}

    // ADD THIS CONSTRUCTOR for the repository projection
    public CustomerRowDto(Long customerId,
                          String name,
                          String phone,
                          String city,
                          String lastPurchasedProductName,
                          String lastPurchasedBrand) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.lastPurchasedProductName = lastPurchasedProductName;
        this.lastPurchasedBrand = lastPurchasedBrand;
    }

    public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPurchasedItem() { return purchasedItem; }
    public void setPurchasedItem(String purchasedItem) { this.purchasedItem = purchasedItem; }

    public String getLastPurchasedProductName() { return lastPurchasedProductName; }
    public void setLastPurchasedProductName(String lastPurchasedProductName) { this.lastPurchasedProductName = lastPurchasedProductName; }
    public String getLastPurchasedBrand() { return lastPurchasedBrand; }
    public void setLastPurchasedBrand(String lastPurchasedBrand) { this.lastPurchasedBrand = lastPurchasedBrand; }

    public PaymentMode getPaymentMode() { return paymentMode; }
    public void setPaymentMode(PaymentMode paymentMode) { this.paymentMode = paymentMode; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public Long getLastPurchaseId() { return lastPurchaseId; }
    public void setLastPurchaseId(Long lastPurchaseId) { this.lastPurchaseId = lastPurchaseId; }
}