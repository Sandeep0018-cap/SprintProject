package com.example.sprintdb.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases", indexes = {
        @Index(name = "idx_purchase_customer", columnList = "customer_id"),
        @Index(name = "idx_purchase_payment_mode", columnList = "paymentMode")
})
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchase_id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PaymentMode paymentMode;

    @Column(length = 80)
    private String transactionId;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, length = 50)
    private String createdByUsername;

    public Long getId() { return purchase_id; }
    public Long getPurchase_id() {
		return purchase_id;
	}
	public void setPurchase_id(Long purchase_id) {
		this.purchase_id = purchase_id;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public PaymentMode getPaymentMode() { return paymentMode; }
    public void setPaymentMode(PaymentMode paymentMode) { this.paymentMode = paymentMode; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getCreatedByUsername() { return createdByUsername; }
    public void setCreatedByUsername(String createdByUsername) { this.createdByUsername = createdByUsername; }
}
