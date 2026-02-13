package com.cg.dto;

public class VendorDto { // Data object for transferring supplier and contact information

    private Long vendorId; // Unique identifier for the vendor entity
    private String name; // Official trade or company name
    private String contactName; // Primary point of contact person at the company
    private String phone; // Direct contact number for the supplier
    private String email; // Professional email address for procurement communication

    public VendorDto() {} // Mandatory default constructor for bean instantiation

    public VendorDto(Long vendorId, String name, String contactName, String phone, String email) { // Constructor for batch data mapping
        this.vendorId = vendorId;
        this.name = name;
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
