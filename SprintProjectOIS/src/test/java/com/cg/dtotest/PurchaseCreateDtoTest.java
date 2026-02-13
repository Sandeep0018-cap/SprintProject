package com.cg.dtotest;

import com.cg.dto.PurchaseCreateDto;
import com.cg.enums.PaymentMode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseCreateDtoTest {

    private static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // -------------------------
    // ✅ POSITIVE (3)
    // -------------------------

    @Test
    void validDto_withAllRequiredFields_shouldHaveNoViolations() {
        PurchaseCreateDto dto = validDto();
        Set<ConstraintViolation<PurchaseCreateDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    void validDto_quantityOne_shouldBeValid() {
        PurchaseCreateDto dto = validDto();
        dto.setQuantity(1); // boundary value
        Set<ConstraintViolation<PurchaseCreateDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validDto_transactionIdNull_shouldStillBeValid() {
        PurchaseCreateDto dto = validDto();
        dto.setTransactionId(null); // optional field
        Set<ConstraintViolation<PurchaseCreateDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    // -------------------------
    // ❌ NEGATIVE (3)
    // -------------------------

    @Test
    void invalidDto_customerIdNull_shouldHaveViolation() {
        PurchaseCreateDto dto = validDto();
        dto.setCustomerId(null);

        Set<ConstraintViolation<PurchaseCreateDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("customerId")));
    }

    @Test
    void invalidDto_quantityZero_shouldHaveMinViolation() {
        PurchaseCreateDto dto = validDto();
        dto.setQuantity(0); // violates @Min(1)

        Set<ConstraintViolation<PurchaseCreateDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("quantity")));
    }

    @Test
    void invalidDto_paymentModeNull_shouldHaveViolation() {
        PurchaseCreateDto dto = validDto();
        dto.setPaymentMode(null);

        Set<ConstraintViolation<PurchaseCreateDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("paymentMode")));
    }

    // helper
    private PurchaseCreateDto validDto() {
        PurchaseCreateDto dto = new PurchaseCreateDto();
        dto.setCustomerId(1L);
        dto.setProductId(10L);
        dto.setQuantity(2);

        // ⚠️ Pick any enum constant that exists in your PaymentMode enum
        dto.setPaymentMode(PaymentMode.CARD);

        dto.setTransactionId("TXN123");
        return dto;
    }
}