package com.example.sprintdb.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "purchase_items", indexes = { @Index(name = "idx_pi_purchase", columnList = "purchase_id"),
		@Index(name = "idx_pi_product", columnList = "product_id") })
public class PurchaseItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_id", nullable = false)
	private Purchase purchase;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_purchase_items_product"))
	private Product product;

	@Min(1)
	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false, precision = 12, scale = 2)
	private Double unitPrice;

	public Long getId() {
		return id;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
}
