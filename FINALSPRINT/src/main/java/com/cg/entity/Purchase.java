package com.cg.entity;

import javax.persistence.*;
import com.cg.enums.PaymentMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchase_id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Direct link to the Product entity
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private PaymentMode paymentMode;

    private String transactionId;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String createdByUsername;

    // Helper methods to pull data from the joined Product
    public String getBrand() {
        return product != null ? product.getBrand() : "N/A";
    }

    public String getProductName() {
        return product != null ? product.getName() : "N/A";
    }

    // Standard Getters & Setters
    public Long getId() { return purchase_id; }
    public Long getPurchase_id() { return purchase_id; }
    public void setPurchase_id(Long purchase_id) { this.purchase_id = purchase_id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public PaymentMode getPaymentMode() { return paymentMode; }
    public void setPaymentMode(PaymentMode paymentMode) { this.paymentMode = paymentMode; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getCreatedByUsername() { return createdByUsername; }
    public void setCreatedByUsername(String createdByUsername) { this.createdByUsername = createdByUsername; }
}
