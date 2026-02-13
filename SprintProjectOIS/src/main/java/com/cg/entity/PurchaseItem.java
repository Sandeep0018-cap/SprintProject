package com.cg.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "purchase_items",
       indexes = {
           @Index(name = "idx_pi_purchase", columnList = "purchase_id"), // Optimizes joins with the parent purchase record
           @Index(name = "idx_pi_product", columnList = "product_id")    // Optimizes product-based sales reporting queries
       })
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Primary key for specific line items within a sale
    private Long purchaseitem_id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false) // Links this entry to its parent transaction
    private Purchase purchase;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false) // Links this entry to the specific item sold
    private Product product;

    @Min(1)
    @Column(nullable = false) // Prevents recording transactions with zero or negative volume
    private Integer quantity;

    @Column(nullable = false, precision = 12, scale = 2) // Captures the exact price per unit at the time of sale
    private Double unitPrice;

    public Long getId() { return purchaseitem_id; }

    public Long getPurchaseitem_id() { return purchaseitem_id; }

    public void setPurchaseitem_id(Long purchaseitem_id) { this.purchaseitem_id = purchaseitem_id; }

    public Purchase getPurchase() { return purchase; }

    public void setPurchase(Purchase purchase) { this.purchase = purchase; }

    public Product getProduct() { return product; }

    public void setProduct(Product product) { this.product = product; }

    public Integer getQuantity() { return quantity; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getUnitPrice() { return unitPrice; }

    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
}
