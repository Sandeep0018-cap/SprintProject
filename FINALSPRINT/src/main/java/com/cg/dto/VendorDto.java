package com.cg.dto;

public class VendorDto {
    private Long vendorId;
    private String name;
    private String contactName;
    private String phone;
    private String email;

    public VendorDto() {}

    public VendorDto(Long vendorId, String name, String contactName, String phone, String email) {
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