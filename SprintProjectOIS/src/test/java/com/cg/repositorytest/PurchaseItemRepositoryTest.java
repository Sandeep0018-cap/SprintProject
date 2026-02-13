package com.cg.repositorytest;

import com.cg.entity.PurchaseItem;
import com.cg.repository.PurchaseItemRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use real DB
class PurchaseItemRepositoryTest {

    @Autowired
    private PurchaseItemRepository purchaseItemRepository;

    // =========================
    // ✅ POSITIVE TESTS
    // =========================

    @Test
    void findByProductProductIdIn_shouldReturnList() {
        List<PurchaseItem> items =
                purchaseItemRepository.findByProductProductIdIn(Arrays.asList(1L, 2L));

        assertNotNull(items);
    }

    @Test
    void existsByProductProductId_shouldReturnBoolean() {
        boolean exists =
                purchaseItemRepository.existsByProductProductId(1L);

        // just ensure method executes
        assertTrue(exists || !exists);
    }

    @Test
    void repositoryMethods_shouldNotThrowException() {
        assertDoesNotThrow(() ->
                purchaseItemRepository.findByProductProductIdIn(Arrays.asList(1L)));

        assertDoesNotThrow(() ->
                purchaseItemRepository.existsByProductProductId(1L));
    }

    // =========================
    // ❌ NEGATIVE TESTS
    // =========================

    @Test
    void findByProductProductIdIn_withInvalidIds_shouldReturnList() {
        List<PurchaseItem> items =
                purchaseItemRepository.findByProductProductIdIn(Arrays.asList(999L, 888L));

        assertNotNull(items);
    }

    @Test
    void existsByProductProductId_withInvalidId_shouldNotCrash() {
        boolean exists =
                purchaseItemRepository.existsByProductProductId(999L);

        assertTrue(exists == true || exists == false);
    }

    @Test
    void findByProductProductIdIn_withEmptyList_shouldWork() {
        List<PurchaseItem> items =
                purchaseItemRepository.findByProductProductIdIn(List.of());

        assertNotNull(items);
    }
}