package com.example.sprintdb.dto;

import com.example.sprintdb.entity.PaymentMode;

public class CustomerRowDto {
    private Long id;
    private String name;
    private String phone;
    private String city;
    private String purchasedItem;   // name + brand
    private PaymentMode paymentMode;
    private String transactionId;
    private Long lastPurchaseId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPurchasedItem() { return purchasedItem; }
    public void setPurchasedItem(String purchasedItem) { this.purchasedItem = purchasedItem; }
    public PaymentMode getPaymentMode() { return paymentMode; }
    public void setPaymentMode(PaymentMode paymentMode) { this.paymentMode = paymentMode; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public Long getLastPurchaseId() { return lastPurchaseId; }
    public void setLastPurchaseId(Long lastPurchaseId) { this.lastPurchaseId = lastPurchaseId; }
}
