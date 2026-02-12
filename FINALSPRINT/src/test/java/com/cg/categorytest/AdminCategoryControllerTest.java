package com.cg.categorytest;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.cg.controller.AdminCategoryController;
import com.cg.dto.CategoryDto; // Changed from Entity to Dto
import com.cg.service.CategoryService;

@ExtendWith(MockitoExtension.class)
public class AdminCategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private AdminCategoryController adminCategoryController;

    @BeforeEach
    void setUp() {
        // Resolve view names to prevent Circular View Path errors
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(adminCategoryController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void list_shouldReturnCategoryListPage() throws Exception {
        // Use CategoryDto to match the Service interface
        List<CategoryDto> dtos = new ArrayList<>();
        dtos.add(mock(CategoryDto.class));

        when(categoryService.findAll()).thenReturn(dtos);

        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-categories"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    void editForm_shouldReturnEditPage() throws Exception {
        CategoryDto dtoMock = mock(CategoryDto.class);

        when(categoryService.findById(1L)).thenReturn(dtoMock);

        mockMvc.perform(get("/admin/categories/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-category-edit"))
                .andExpect(model().attributeExists("category"));
    }

    @Test
    void update_shouldRedirectOnSuccess() throws Exception {
        mockMvc.perform(post("/admin/categories/1/edit")
                        .param("name", "UpdatedName"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"))
                .andExpect(flash().attribute("msg", "Category updated successfully"));

        verify(categoryService).update(eq(1L), anyString());
    }

    @Test
    void create_shouldRedirectOnSuccess() throws Exception {
        mockMvc.perform(post("/admin/categories")
                        .param("name", "NewCategory"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"))
                .andExpect(flash().attribute("msg", "Category added successfully"));

        verify(categoryService).create("NewCategory");
    }

    @Test
    void delete_shouldHandleException() throws Exception {
        // Correctly handle the void delete method
        doThrow(new RuntimeException("Delete failed")).when(categoryService).delete(anyLong());

        mockMvc.perform(post("/admin/categories/5/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"))
                .andExpect(flash().attribute("err", "Delete failed"));
    }

    @Test
    void update_shouldHandleException() throws Exception {
        // Use anyString() to match the name parameter
        doThrow(new RuntimeException("Update failed"))
                .when(categoryService).update(eq(1L), anyString());

        mockMvc.perform(post("/admin/categories/1/edit")
                        .param("name", "BadName"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"))
                .andExpect(flash().attribute("err", "Update failed"));
    }
}
