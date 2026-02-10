package com.example.sprintdb.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customer_id;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String name;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String phone;

    @NotBlank
    @Size(max = 80)
    @Column(nullable = false, length = 80)
    private String city;

    // Denormalized fields to satisfy "customers table should also have purchased item name/brand"
    @Size(max = 200)
    @Column(length = 200)
    private String lastPurchasedProductName;

    @Size(max = 80)
    @Column(length = 80)
    private String lastPurchasedBrand;

    public Long getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
	}
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
}
