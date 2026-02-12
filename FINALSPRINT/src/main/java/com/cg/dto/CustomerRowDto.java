package com.cg.dto;

import com.cg.enums.PaymentMode;

public class CustomerRowDto {

    private Long customer_id;
    private String name;
    private String phone;
    private String city;
    private String lastPurchasedProductName;
    private String lastPurchasedBrand;
    private PaymentMode paymentMode; // keep enum

    public CustomerRowDto() {}

    public CustomerRowDto(Long customer_id,
                          String name,
                          String phone,
                          String city,
                          String lastPurchasedProductName,
                          String lastPurchasedBrand,
                          PaymentMode paymentMode) {
        this.customer_id = customer_id;
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.lastPurchasedProductName = lastPurchasedProductName;
        this.lastPurchasedBrand = lastPurchasedBrand;
        this.paymentMode = paymentMode;
    }

    public Long getCustomer_id() { return customer_id; }
    public void setCustomer_id(Long customer_id) { this.customer_id = customer_id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getLastPurchasedProductName() { return lastPurchasedProductName; }
    public void setLastPurchasedProductName(String lastPurchasedProductName) { this.lastPurchasedProductName = lastPurchasedProductName; }

    public String getLastPurchasedBrand() { return lastPurchasedBrand; }
    public void setLastPurchasedBrand(String lastPurchasedBrand) { this.lastPurchasedBrand = lastPurchasedBrand; }

    public PaymentMode getPaymentMode() { return paymentMode; }
    public void setPaymentMode(PaymentMode paymentMode) { this.paymentMode = paymentMode; }

    @Override
    public String toString() {
        return "CustomerRowDto{" +
               "customer_id=" + customer_id +
               ", name='" + name + '\'' +
               ", phone='" + phone + '\'' +
               ", city='" + city + '\'' +
               ", lastPurchasedProductName='" + lastPurchasedProductName + '\'' +
               ", lastPurchasedBrand='" + lastPurchasedBrand + '\'' +
               ", paymentMode=" + (paymentMode != null ? paymentMode.name() : null) +
               '}';
    }
}
