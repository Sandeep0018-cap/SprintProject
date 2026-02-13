package com.cg.servicetest;

import com.cg.dto.CategoryDto;
import com.cg.entity.Category;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repository.CategoryRepository;
import com.cg.service.impl.CategoryServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category laptops;

    @BeforeEach
    void setup() {
        laptops = new Category();
        laptops.setCategoryId(1L);
        laptops.setName("Laptops");
    }

    // =========================
    // ✅ POSITIVE (3)
    // =========================

    @Test
    void findAll_shouldReturnDtos() {
        when(categoryRepository.findAll()).thenReturn(List.of(laptops));

        List<CategoryDto> out = categoryService.findAll();

        assertEquals(1, out.size());
        assertEquals(1L, out.get(0).getCategoryId());
        assertEquals("Laptops", out.get(0).getName());
        verify(categoryRepository).findAll();
    }

    @Test
    void create_shouldSaveAndReturnDto_whenNameIsNew() {
        String name = "Mobiles";

        when(categoryRepository.findByNameIgnoreCase(name)).thenReturn(Optional.empty());

        Category saved = new Category();
        saved.setCategoryId(2L);
        saved.setName(name);

        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        CategoryDto out = categoryService.create(name);

        assertNotNull(out);
        assertEquals(2L, out.getCategoryId());
        assertEquals("Mobiles", out.getName());
        verify(categoryRepository).findByNameIgnoreCase(name);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void delete_shouldDelete_whenCategoryExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(laptops));

        assertDoesNotThrow(() -> categoryService.delete(1L));

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).delete(laptops);
    }

    // =========================
    // ❌ NEGATIVE (3)
    // =========================

    @Test
    void create_shouldThrowBadRequest_whenDuplicateNameExists() {
        String name = "Laptops";
        when(categoryRepository.findByNameIgnoreCase(name)).thenReturn(Optional.of(laptops));

        assertThrows(BadRequestException.class, () -> categoryService.create(name));

        verify(categoryRepository).findByNameIgnoreCase(name);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void delete_shouldThrowNotFound_whenCategoryMissing() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.delete(99L));

        verify(categoryRepository).findById(99L);
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    void findAll_shouldReturnEmptyList_whenRepoEmpty() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        List<CategoryDto> out = categoryService.findAll();

        assertNotNull(out);
        assertTrue(out.isEmpty());
        verify(categoryRepository).findAll();
    }
}