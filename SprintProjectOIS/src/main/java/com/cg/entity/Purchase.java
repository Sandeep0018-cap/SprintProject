package com.cg.entity;

import javax.persistence.*;
import com.cg.enums.PaymentMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases") // Table for recording customer sales transactions
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchase_id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false) // Links the sale to a specific customer record
    private Customer customer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false) // Links the sale to the specific inventory item
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15) // Stores payment method as readable text
    private PaymentMode paymentMode;

    private String transactionId; // Optional reference for non-cash payments

    private LocalDateTime createdAt = LocalDateTime.now(); // Automatically timestamps the sale

    private String createdByUsername; // Tracks the staff member who processed the sale

    public String getBrand() { // Transient helper: retrieves brand from the associated product
        return (product != null) ? product.getBrand() : "N/A";
    }

    public String getProductName() { // Transient helper: retrieves product name from the association
        return (product != null) ? product.getName() : "N/A";
    }

    public Long getId() { return purchase_id; } // Alias getter for consistency with generic repositories

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
