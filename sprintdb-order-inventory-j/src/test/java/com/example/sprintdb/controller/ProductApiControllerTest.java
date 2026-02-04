package com.example.sprintdb.controller;

import com.example.sprintdb.controller.api.ProductApiController;
import com.example.sprintdb.dto.ProductDto;
import com.example.sprintdb.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductApiControllerTest {

    @Test
    void returnsResponseEntityOk() {
        ProductService svc = mock(ProductService.class);
        when(svc.findByCategoryWithDerivedStats(1L)).thenReturn(Collections.emptyList());

        ProductApiController c = new ProductApiController(svc);
        ResponseEntity<?> res = c.byCategory(1L);

        assertEquals(200, res.getStatusCodeValue());
        verify(svc, times(1)).findByCategoryWithDerivedStats(1L);
    }
}
