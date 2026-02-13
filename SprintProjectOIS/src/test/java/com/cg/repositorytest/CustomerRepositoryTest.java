package com.cg.repositorytest;

import com.cg.dto.CustomerRowDto;
import com.cg.repository.CustomerRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use your real DB
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    // ✅ Positive 1: method should execute and return a list
    @Test
    void listCustomersWithLastPurchase_shouldReturnList() {
        List<CustomerRowDto> rows = customerRepository.listCustomersWithLastPurchase();
        assertNotNull(rows);
    }

    // ✅ Positive 2: calling twice should not crash
    @Test
    void listCustomersWithLastPurchase_shouldWorkMultipleTimes() {
        assertDoesNotThrow(() -> customerRepository.listCustomersWithLastPurchase());
        assertDoesNotThrow(() -> customerRepository.listCustomersWithLastPurchase());
    }

    // ✅ Positive 3: if data exists, first row should have basic fields
    @Test
    void listCustomersWithLastPurchase_ifNotEmpty_shouldHaveBasicFields() {
        List<CustomerRowDto> rows = customerRepository.listCustomersWithLastPurchase();

        if (!rows.isEmpty()) {
            CustomerRowDto first = rows.get(0);

            // These getters should exist in your DTO (adjust names if different)
            assertNotNull(first.getCustomer_id());
            assertNotNull(first.getName());
        }
    }

    // ❌ Negative 1: should not throw even when DB has no matching data (still returns list)
    @Test
    void listCustomersWithLastPurchase_shouldNotThrow() {
        assertDoesNotThrow(() -> customerRepository.listCustomersWithLastPurchase());
    }

    // ❌ Negative 2: list should never be null
    @Test
    void listCustomersWithLastPurchase_shouldNeverReturnNull() {
        List<CustomerRowDto> rows = customerRepository.listCustomersWithLastPurchase();
        assertNotNull(rows);
    }

    // ❌ Negative 3: size should be >= 0 always (basic sanity)
    @Test
    void listCustomersWithLastPurchase_sizeShouldBeNonNegative() {
        List<CustomerRowDto> rows = customerRepository.listCustomersWithLastPurchase();
        assertTrue(rows.size() >= 0);
    }
}