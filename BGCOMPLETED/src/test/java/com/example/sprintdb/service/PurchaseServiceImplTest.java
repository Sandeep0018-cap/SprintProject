package com.example.sprintdb.service;

import com.example.sprintdb.dto.PurchaseCreateDto;
import com.example.sprintdb.entity.*;
import com.example.sprintdb.exception.BadRequestException;
import com.example.sprintdb.repository.*;
import com.example.sprintdb.service.impl.PurchaseServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PurchaseServiceImplTest {

    @Test
    void onlinePaymentRequiresTransactionId() {
        PurchaseRepository purchaseRepo = mock(PurchaseRepository.class);
        PurchaseItemRepository itemRepo = mock(PurchaseItemRepository.class);
        CustomerRepository customerRepo = mock(CustomerRepository.class);
        ProductRepository productRepo = mock(ProductRepository.class);

        PurchaseServiceImpl svc = new PurchaseServiceImpl(purchaseRepo, itemRepo, customerRepo, productRepo);

        Customer customer = new Customer();
        Product product = mock(Product.class);
        when(product.getPrice()).thenReturn(100.0);
        when(product.getName()).thenReturn("Keyboard");
        when(product.getBrand()).thenReturn("Dell");

        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepo.findById(2L)).thenReturn(Optional.of(product));
        when(purchaseRepo.save(any(Purchase.class))).thenAnswer(inv -> {
            Purchase p = inv.getArgument(0);
            // simulate saved id
            Purchase spy = spy(p);
            when(spy.getId()).thenReturn(99L);
            return spy;
        });

        PurchaseCreateDto dto = new PurchaseCreateDto();
        dto.setCustomerId(1L);
        dto.setProductId(2L);
        dto.setQuantity(1);
        dto.setPaymentMode(PaymentMode.ONLINE);
        dto.setTransactionId("  "); // invalid

        assertThrows(BadRequestException.class, () -> svc.createPurchase(dto, "staff"));
    }
}
