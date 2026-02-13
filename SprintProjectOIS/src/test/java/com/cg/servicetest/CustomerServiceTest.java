package com.cg.servicetest;
 
import com.cg.dto.CustomerRowDto;

import com.cg.entity.Customer;

import com.cg.repository.CustomerRepository;

import com.cg.repository.PurchaseItemRepository;

import com.cg.repository.PurchaseRepository;
import com.cg.service.impl.CustomerServiceImpl;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
 
import org.mockito.InjectMocks;

import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
 
import java.util.Collections;

import java.util.List;
 
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
 
/**

* Unit tests for CustomerServiceImpl.

* Focuses only on service logic and repository interactions.

*/

@ExtendWith(MockitoExtension.class)

class CustomerServiceImplTest {
 
    @Mock

    private CustomerRepository customerRepository;
 
    // Not used by current methods, but present in service constructor

    @Mock

    private PurchaseRepository purchaseRepository;
 
    @Mock

    private PurchaseItemRepository purchaseItemRepository;
 
    @InjectMocks

    private CustomerServiceImpl customerService;
 
    private Customer sampleCustomer;
 
    @BeforeEach

    void setUp() {

        sampleCustomer = new Customer();

        sampleCustomer.setCustomer_id(1L);

        sampleCustomer.setName("John Doe");

        sampleCustomer.setPhone("9999999999");

        sampleCustomer.setCity("Hyderabad");

        sampleCustomer.setLastPurchasedProductName("iPhone 14");

        sampleCustomer.setLastPurchasedBrand("Apple");

    }
 
    // ---------------------------------------------------------------------

    // POSITIVE TESTS (3)

    // ---------------------------------------------------------------------
 
    /**

     * create(): should call repository.save and return the saved entity.

     */

    @Test

    void create_shouldSaveAndReturnCustomer() {

        when(customerRepository.save(sampleCustomer)).thenReturn(sampleCustomer);
 
        Customer saved = customerService.create(sampleCustomer);
 
        assertNotNull(saved);

        assertEquals(1L, saved.getCustomer_id());

        assertEquals("John Doe", saved.getName());

        verify(customerRepository, times(1)).save(sampleCustomer);

        // Ensure other repos are not used here

        verifyNoInteractions(purchaseRepository, purchaseItemRepository);

    }
 
    /**

     * listCustomersWithLastPurchase(): should return list from repository as-is.

     */

    @Test

    void listCustomersWithLastPurchase_shouldReturnList() {

        CustomerRowDto row = new CustomerRowDto(

                1L, "John Doe", "9999999999", "Hyderabad",

                "iPhone 14", "Apple", null , null

        );

        when(customerRepository.listCustomersWithLastPurchase())

                .thenReturn(List.of(row));
 
        List<CustomerRowDto> result = customerService.listCustomersWithLastPurchase();
 
        assertNotNull(result);

        assertEquals(1, result.size());

        assertEquals("John Doe", result.get(0).getName());

        verify(customerRepository, times(1)).listCustomersWithLastPurchase();

    }
 
    /**

     * listCustomersWithLastPurchase(): should handle empty result (still valid).

     */

    @Test

    void listCustomersWithLastPurchase_whenEmpty_shouldReturnEmptyList() {

        when(customerRepository.listCustomersWithLastPurchase())

                .thenReturn(Collections.emptyList());
 
        List<CustomerRowDto> result = customerService.listCustomersWithLastPurchase();
 
        assertNotNull(result);

        assertTrue(result.isEmpty());

        verify(customerRepository, times(1)).listCustomersWithLastPurchase();

    }
 
    // ---------------------------------------------------------------------

    // NEGATIVE TESTS (3)

    // ---------------------------------------------------------------------
 
    /**

     * create(): if repository throws (e.g., DB issue), service should propagate the exception.

     */

    @Test

    void create_whenRepositoryThrows_shouldPropagate() {

        when(customerRepository.save(sampleCustomer))

                .thenThrow(new RuntimeException("DB down"));
 
        RuntimeException ex = assertThrows(RuntimeException.class,

                () -> customerService.create(sampleCustomer));
 
        assertTrue(ex.getMessage().contains("DB down"));

        verify(customerRepository, times(1)).save(sampleCustomer);

    }
 
    /**

     * listCustomersWithLastPurchase(): if repository throws, service should propagate.

     */

    @Test

    void listCustomersWithLastPurchase_whenRepoThrows_shouldPropagate() {

        when(customerRepository.listCustomersWithLastPurchase())

                .thenThrow(new IllegalStateException("Native query failed"));
 
        IllegalStateException ex = assertThrows(IllegalStateException.class,

                () -> customerService.listCustomersWithLastPurchase());
 
        assertTrue(ex.getMessage().contains("Native query failed"));

        verify(customerRepository, times(1)).listCustomersWithLastPurchase();

    }
 
    /**

     * create(): if null is passed, expect NullPointerException (current service does not guard null).

     * This documents existing behavior clearly for academic explanation.

     */

    @Test

    void create_whenRepositoryRejectsNull_shouldPropagate() {

        when(customerRepository.save(null))

                .thenThrow(new NullPointerException("Customer is null"));
 
        assertThrows(NullPointerException.class,

                () -> customerService.create(null));
 
        verify(customerRepository).save(null);

    }

}
 