package com.cg.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "vendors", uniqueConstraints = @UniqueConstraint(columnNames = "name")) // Ensures each supplier has a unique trade name
public class Vendor {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Primary key for identifying unique suppliers
    private Long vendorId;

    @NotBlank 
    @Size(max = 150)
    @Column(nullable = false, length = 150) // Enforces mandatory company name at the schema level
    private String name;

    @Size(max = 150) // Stores the primary point of contact person
    private String contactName;

    @Size(max = 80) // Stores the supplier's contact telephone number
    private String phone;

    @Size(max = 180) // Stores the supplier's professional email address
    private String email;

    @ManyToMany // Establishes a many-to-many relationship with the catalog
    @JoinTable(
      name = "vendor_products", // Creates the intersection table for supplier-product mapping
      joinColumns = @JoinColumn(name = "vendor_id"), // Foreign key pointing to this vendor
      inverseJoinColumns = @JoinColumn(name = "product_id") // Foreign key pointing to the associated product
    )
    private Set<Product> products;

    public void setId(Long vendorId) { this.vendorId = vendorId; }

    public Long getVendorId() { return vendorId; }

    public void setVendorId(Long vendorId) { this.vendorId = vendorId; }

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
