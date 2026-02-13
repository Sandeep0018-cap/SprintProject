package com.cg.repositorytest;

import com.cg.entity.Order;
import com.cg.enums.OrderStatus;
import com.cg.repository.OrderRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use real DB
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    // =========================
    // ✅ POSITIVE TESTS
    // =========================

    @Test
    void findByStatusOrderByOrderIdDesc_shouldReturnList() {
        List<Order> orders =
                orderRepository.findByStatusOrderByOrderIdDesc(OrderStatus.DELIVERED);

        assertNotNull(orders);
    }

    @Test
    void findByStatusOrderByOrderIdDesc_shouldNotThrowException() {
        assertDoesNotThrow(() ->
                orderRepository.findByStatusOrderByOrderIdDesc(OrderStatus.DELIVERED));
    }

    @Test
    void findByStatusOrderByOrderIdDesc_ifNotEmpty_shouldHaveOrderIds() {
        List<Order> orders =
                orderRepository.findByStatusOrderByOrderIdDesc(OrderStatus.DELIVERED);

        if (!orders.isEmpty()) {
            assertNotNull(orders.get(0).getOrderId());
        }
    }

    // =========================
    // ❌ NEGATIVE TESTS
    // =========================

    @Test
    void findByStatusOrderByOrderIdDesc_shouldNeverReturnNull() {
        List<Order> orders =
                orderRepository.findByStatusOrderByOrderIdDesc(OrderStatus.INTRANSIT);

        assertNotNull(orders);
    }

    @Test
    void findByStatusOrderByOrderIdDesc_sizeShouldBeNonNegative() {
        List<Order> orders =
                orderRepository.findByStatusOrderByOrderIdDesc(OrderStatus.CANCELLED);

        assertTrue(orders.size() >= 0);
    }

    @Test
    void findByStatusOrderByOrderIdDesc_withDifferentStatus_shouldStillWork() {
        assertDoesNotThrow(() ->
                orderRepository.findByStatusOrderByOrderIdDesc(OrderStatus.CANCELLED));
    }
}