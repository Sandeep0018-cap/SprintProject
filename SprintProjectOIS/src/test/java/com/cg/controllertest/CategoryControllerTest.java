package com.cg.controllertest;

import com.cg.controller.CategoryController;
import com.cg.dto.CategoryDto;
import com.cg.dto.ProductDto;
import com.cg.service.ICategoryService;
import com.cg.service.IProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ICategoryService categoryService;

    @Mock
    private IProductService productService;

    @BeforeEach
    void setup() {
        CategoryController controller =
                new CategoryController(categoryService, productService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // =========================
    // ✅ POSITIVE (3)
    // =========================

    @Test
    void categories_shouldReturnView_andAddCategories() throws Exception {
        when(categoryService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("category/categories"))
                .andExpect(model().attributeExists("categories"));

        verify(categoryService).findAll();
    }

    @Test
    void categoryDetail_shouldReturnDetailView_andAddModelAttributes() throws Exception {

        Long id = 1L;

        List<ProductDto> products = List.of(new ProductDto());
        Map<String, List<ProductDto>> byBrand = new HashMap<>();
        byBrand.put("Dell", products);

        when(productService.findByCategoryWithDerivedStats(id)).thenReturn(products);
        when(productService.groupByBrand(products)).thenReturn(byBrand);

        CategoryDto dto = new CategoryDto(1L, "Laptops");
        when(categoryService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("category/category-detail"))
                .andExpect(model().attributeExists("categoryId"))
                .andExpect(model().attributeExists("categoryName"))
                .andExpect(model().attributeExists("productsByBrand"));

        verify(productService).findByCategoryWithDerivedStats(id);
        verify(productService).groupByBrand(products);
        verify(categoryService).findAll();
    }

    @Test
    void categoryDetail_whenCategoryNotFound_shouldUseDefaultName() throws Exception {

        Long id = 99L;

        when(productService.findByCategoryWithDerivedStats(id))
                .thenReturn(Collections.emptyList());

        when(productService.groupByBrand(any()))
                .thenReturn(Collections.emptyMap());

        when(categoryService.findAll())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/categories/99"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("categoryName", "Category"));
    }

    // =========================
    // ❌ NEGATIVE (3)
    // =========================

    @Test
    void wrongUrl_shouldReturn404() throws Exception {
        mockMvc.perform(get("/wrong"))
                .andExpect(status().isNotFound());
    }
}