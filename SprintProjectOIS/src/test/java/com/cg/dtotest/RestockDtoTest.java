package com.cg.dtotest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.cg.dto.RestockDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RestockDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // -------------------------
    // ✅ POSITIVE (3)
    // -------------------------

    @Test
    void validDto_shouldHaveNoViolations() {
        RestockDto dto = validDto();
        Set<ConstraintViolation<RestockDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validDto_quantityOne_shouldBeValid() {
        RestockDto dto = validDto();
        dto.setRequestedQty(1); // boundary value
        Set<ConstraintViolation<RestockDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validDto_idNull_shouldStillBeValid() {
        RestockDto dto = validDto();
        dto.setId(null); // optional field
        Set<ConstraintViolation<RestockDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    // -------------------------
    // ❌ NEGATIVE (3)
    // -------------------------

    @Test
    void invalidDto_productIdNull_shouldHaveViolation() {
        RestockDto dto = validDto();
        dto.setProductId(null);

        Set<ConstraintViolation<RestockDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("productId")));
    }

    @Test
    void invalidDto_vendorIdNull_shouldHaveViolation() {
        RestockDto dto = validDto();
        dto.setVendorId(null);

        Set<ConstraintViolation<RestockDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("vendorId")));
    }

    @Test
    void invalidDto_quantityZero_shouldFailMinValidation() {
        RestockDto dto = validDto();
        dto.setRequestedQty(0); // violates @Min(1)

        Set<ConstraintViolation<RestockDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("requestedQty")));
    }

    // -------------------------
    // helper method
    // -------------------------
    private RestockDto validDto() {
        RestockDto dto = new RestockDto();
        dto.setId(1L);
        dto.setProductId(100L);
        dto.setVendorId(200L);
        dto.setRequestedQty(5);
        return dto;
    }
}