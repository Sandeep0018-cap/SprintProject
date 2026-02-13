package com.cg.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.cg.enums.PaymentMode;

public class PurchaseCreateDto { // Data wrapper for capturing new sales transactions from the UI

    @NotNull // Ensures a customer is associated with the sale
    private Long customerId;

    @NotNull // Ensures a specific item is selected for the purchase
    private Long productId;

    @NotNull // Validates that the field is not empty
    @Min(1)  // Enforces a minimum purchase volume of at least one unit
    private Integer quantity;

    @NotNull // Requires a valid selection from the PaymentMode enumeration
    private PaymentMode paymentMode;

    private String transactionId; // Optional reference for digital or bank-based payments

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public PaymentMode getPaymentMode() { return paymentMode; }
    public void setPaymentMode(PaymentMode paymentMode) { this.paymentMode = paymentMode; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}
