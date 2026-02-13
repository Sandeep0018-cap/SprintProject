package com.cg.servicetest;

import com.cg.dto.PurchaseCreateDto;

import com.cg.entity.Customer;
import com.cg.entity.Product;
import com.cg.entity.Purchase;
import com.cg.enums.PaymentMode;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repository.CustomerRepository;
import com.cg.repository.ProductRepository;
import com.cg.repository.PurchaseItemRepository;
import com.cg.repository.PurchaseRepository;
import com.cg.service.impl.PurchaseServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseServiceImplTest {

    private PurchaseRepository purchaseRepository;
    private PurchaseItemRepository purchaseItemRepository;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;

    private PurchaseServiceImpl service;

    @BeforeEach
    void setup() {
        purchaseRepository = mock(PurchaseRepository.class);
        purchaseItemRepository = mock(PurchaseItemRepository.class);
        customerRepository = mock(CustomerRepository.class);
        productRepository = mock(ProductRepository.class);

        service = new PurchaseServiceImpl(
                purchaseRepository,
                purchaseItemRepository,
                customerRepository,
                productRepository
        );
    }

    // =========================
    // ✅ POSITIVE TESTS
    // =========================

    @Test
    void createPurchase_shouldSavePurchase_whenValidData() {

        PurchaseCreateDto dto = new PurchaseCreateDto();
        dto.setCustomerId(1L);
        dto.setProductId(2L);
        dto.setQuantity(3);
        dto.setPaymentMode(PaymentMode.CARD);
        dto.setTransactionId("TXN123");

        Customer customer = new Customer();
        Product product = new Product();
        product.setPrice(100.0);
        product.setName("Laptop");
        product.setBrand("Dell");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(purchaseRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Purchase result = service.createPurchase(dto, "admin");

        assertNotNull(result);
        verify(purchaseRepository).save(any());
        verify(purchaseItemRepository).save(any());
        verify(customerRepository).save(customer);
    }

    @Test
    void createPurchase_codPayment_shouldClearTransactionId() {

        PurchaseCreateDto dto = new PurchaseCreateDto();
        dto.setCustomerId(1L);
        dto.setProductId(2L);
        dto.setQuantity(1);
        dto.setPaymentMode(PaymentMode.COD);
        dto.setTransactionId("SHOULD_BE_REMOVED");

        Customer customer = new Customer();
        Product product = new Product();
        product.setPrice(10.0);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(purchaseRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.createPurchase(dto, "admin");

        assertNull(dto.getTransactionId());
    }

    @Test
    void paymentModeCounts_shouldReturnCounts() {
        Purchase p1 = new Purchase();
        p1.setPaymentMode(PaymentMode.CARD);

        Purchase p2 = new Purchase();
        p2.setPaymentMode(PaymentMode.CARD);

        Purchase p3 = new Purchase();
        p3.setPaymentMode(PaymentMode.COD);

        when(purchaseRepository.findAll()).thenReturn(List.of(p1, p2, p3));

        Map<String, Long> result = service.paymentModeCounts();

        assertEquals(2L, result.get("CARD"));
        assertEquals(1L, result.get("COD"));
    }

    // =========================
    // ❌ NEGATIVE TESTS
    // =========================

    @Test
    void createPurchase_shouldThrow_whenCustomerNotFound() {
        PurchaseCreateDto dto = new PurchaseCreateDto();
        dto.setCustomerId(1L);
        dto.setProductId(2L);

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.createPurchase(dto, "admin"));
    }

    @Test
    void createPurchase_shouldThrow_whenProductNotFound() {
        PurchaseCreateDto dto = new PurchaseCreateDto();
        dto.setCustomerId(1L);
        dto.setProductId(2L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.createPurchase(dto, "admin"));
    }

    @Test
    void createPurchase_cardWithoutTransaction_shouldThrow() {
        PurchaseCreateDto dto = new PurchaseCreateDto();
        dto.setCustomerId(1L);
        dto.setProductId(2L);
        dto.setPaymentMode(PaymentMode.CARD);
        dto.setTransactionId(null);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
        when(productRepository.findById(2L)).thenReturn(Optional.of(new Product()));

        assertThrows(BadRequestException.class,
                () -> service.createPurchase(dto, "admin"));
    }
}