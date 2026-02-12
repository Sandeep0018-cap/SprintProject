package com.cg.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "vendors", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Vendor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vendorId;

    @NotBlank @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String name;

    @Size(max = 150)
    private String contactName;

    @Size(max = 80)
    private String phone;

    @Size(max = 180)
    private String email;

    // --- NEW: link vendors to the products they supply ---
    @ManyToMany

@JoinTable(
  name = "vendor_products",
  joinColumns = @JoinColumn(name = "vendor_id"),
  inverseJoinColumns = @JoinColumn(name = "product_id")
)
private Set<Product> products;


    public void setId(Long vendorId) {
		this.vendorId = vendorId;
	}
    public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Product> getProducts() { return products; }
    public void setProducts(Set<Product> products) { this.products = products; }
}