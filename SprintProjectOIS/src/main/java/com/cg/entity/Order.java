package com.cg.entity;

import javax.persistence.*;
import com.cg.enums.OrderStatus;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders", indexes = @Index(name = "idx_orders_status", columnList = "status")) // Optimized for status-based dashboard filtering
public class Order {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Primary key for transactional records
    private Long orderId;

    @Column(nullable = false, length = 40, unique = true) // Ensures unique business reference for tracking
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12) // Stores status as a string for database readability
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false) // Captures the exact moment the transaction was created
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt; // Tracks subsequent state changes for auditing

    protected Order() {}

    public Order(String reference, OrderStatus status) { // Manual constructor for business logic instantiation
        this.reference = reference;
        this.status = (status == null) ? OrderStatus.PENDING : status;
        this.createdAt = LocalDateTime.now();
    }

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getReference() { return reference; }
    
    public void setReference(String reference) { this.reference = reference; }
    
    public OrderStatus getStatus() { return status; }
    
    public void setStatus(OrderStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
