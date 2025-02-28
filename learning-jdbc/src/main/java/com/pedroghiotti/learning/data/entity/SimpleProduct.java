package com.pedroghiotti.learning.data.entity;

import java.math.BigDecimal;
import java.util.UUID;

public class SimpleProduct {
    private UUID productId;
    private String name;
    private BigDecimal price;
    private UUID vendorId;
    private String vendorName;
    private String vendorContact;
    private String vendorPhoneNumber;
    private String vendorEmail;
    private String vendorAddress;

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public UUID getVendorId() {
        return vendorId;
    }

    public void setVendorId(UUID vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorContact() {
        return vendorContact;
    }

    public void setVendorContact(String vendorContact) {
        this.vendorContact = vendorContact;
    }

    public String getVendorPhoneNumber() {
        return vendorPhoneNumber;
    }

    public void setVendorPhoneNumber(String vendorPhoneNumber) {
        this.vendorPhoneNumber = vendorPhoneNumber;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    @Override
    public String toString() {
        return "SimpleProduct{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", vendorId=" + vendorId +
                ", vendorName='" + vendorName + '\'' +
                ", vendorContact='" + vendorContact + '\'' +
                ", vendorPhoneNumber='" + vendorPhoneNumber + '\'' +
                ", vendorEmail='" + vendorEmail + '\'' +
                ", vendorAddress='" + vendorAddress + '\'' +
                '}';
    }
}
