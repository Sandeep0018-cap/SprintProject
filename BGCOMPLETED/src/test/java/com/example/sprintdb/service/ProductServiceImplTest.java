package com.example.sprintdb.service;

import com.example.sprintdb.dto.ProductDto;
import com.example.sprintdb.entity.*;
import com.example.sprintdb.repository.*;
import com.example.sprintdb.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @Test
    void soldQtyIsDerivedUsingStreams() {
        ProductRepository productRepo = mock(ProductRepository.class);
        CategoryRepository catRepo = mock(CategoryRepository.class);
        PurchaseItemRepository itemRepo = mock(PurchaseItemRepository.class);

        ProductServiceImpl svc = new ProductServiceImpl(productRepo, catRepo, itemRepo);

        Category cat = new Category();
        // id not needed for this test

        Product p1 = new Product();
        // set via reflection workaround not necessary; only getters used in mapping
        // We'll mock IDs by subclassing with Mockito spy
        Product sp1 = Mockito.spy(p1);
        when(sp1.getId()).thenReturn(10L);
        when(sp1.getSkuId()).thenReturn("SKU-10");
        when(sp1.getName()).thenReturn("Mouse");
        when(sp1.getBrand()).thenReturn("Dell");
        when(sp1.getPrice()).thenReturn(499.0);
        when(sp1.getStockQty()).thenReturn(10);

        Product sp2 = Mockito.spy(new Product());
        when(sp2.getId()).thenReturn(20L);
        when(sp2.getSkuId()).thenReturn("SKU-20");
        when(sp2.getName()).thenReturn("Mouse");
        when(sp2.getBrand()).thenReturn("HP");
        when(sp2.getPrice()).thenReturn(599.0);
        when(sp2.getStockQty()).thenReturn(5);

        when(productRepo.findByCategoryIdOrderByBrandAscNameAsc(1L)).thenReturn(Arrays.asList(sp1, sp2));

        Purchase purchase = new Purchase();
        Purchase spyPurchase = Mockito.spy(purchase);
        when(spyPurchase.getId()).thenReturn(100L);

        PurchaseItem i1 = Mockito.spy(new PurchaseItem());
        when(i1.getProduct()).thenReturn(sp1);
        when(i1.getQuantity()).thenReturn(3);
        when(i1.getPurchase()).thenReturn(spyPurchase);

        PurchaseItem i2 = Mockito.spy(new PurchaseItem());
        when(i2.getProduct()).thenReturn(sp1);
        when(i2.getQuantity()).thenReturn(2);
        when(i2.getPurchase()).thenReturn(spyPurchase);

        PurchaseItem i3 = Mockito.spy(new PurchaseItem());
        when(i3.getProduct()).thenReturn(sp2);
        when(i3.getQuantity()).thenReturn(1);
        when(i3.getPurchase()).thenReturn(spyPurchase);

        when(itemRepo.findByProductIdIn(Arrays.asList(10L, 20L))).thenReturn(Arrays.asList(i1, i2, i3));

        List<ProductDto> out = svc.findByCategoryWithDerivedStats(1L);

        ProductDto d1 = out.stream().filter(d -> d.getId().equals(10L)).findFirst().orElseThrow(AssertionError::new);
        assertEquals(5, d1.getSoldQty().intValue());
        assertEquals(5, d1.getAvailableQty().intValue());
        assertEquals(100L, d1.getLastPurchaseId().longValue());

        ProductDto d2 = out.stream().filter(d -> d.getId().equals(20L)).findFirst().orElseThrow(AssertionError::new);
        assertEquals(1, d2.getSoldQty().intValue());
        assertEquals(4, d2.getAvailableQty().intValue());
    }
}
