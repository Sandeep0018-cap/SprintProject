package com.cg.controllertest;

import com.cg.controller.AdminCategoryController;
import com.cg.service.ICategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminCategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ICategoryService categoryService;

    @BeforeEach
    void setup() {
        AdminCategoryController controller = new AdminCategoryController(categoryService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // =========================
    // ✅ POSITIVE (3)
    // =========================

    @Test
    void list_shouldReturnView_andAddCategoriesToModel() throws Exception {
        when(categoryService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/categories"))
                .andExpect(model().attributeExists("categories"));

        verify(categoryService).findAll();
    }

    @Test
    void create_whenSuccess_shouldRedirect_andSetSuccessFlash() throws Exception {
        // create() returns CategoryDto, but we don’t need it for controller
        when(categoryService.create("Mobiles")).thenReturn(null);

        mockMvc.perform(post("/admin/categories")
                        .param("name", "Mobiles"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"))
                .andExpect(flash().attributeExists("msg"));

        verify(categoryService).create("Mobiles");
    }

    @Test
    void delete_whenSuccess_shouldRedirect_andSetSuccessFlash() throws Exception {
        doNothing().when(categoryService).delete(1L);

        mockMvc.perform(delete("/admin/categories/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"))
                .andExpect(flash().attributeExists("msg"));

        verify(categoryService).delete(1L);
    }

    // =========================
    // ❌ NEGATIVE (3)
    // =========================

    @Test
    void create_whenServiceThrows_shouldRedirect_andSetErrorFlash() throws Exception {
        when(categoryService.create("Mobiles")).thenThrow(new RuntimeException("boom"));

        mockMvc.perform(post("/admin/categories")
                        .param("name", "Mobiles"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"))
                .andExpect(flash().attributeExists("err"));

        verify(categoryService).create("Mobiles");
    }

    @Test
    void update_whenServiceThrows_shouldRedirect_andSetErrorFlash() throws Exception {
        doThrow(new RuntimeException("update failed"))
                .when(categoryService).update(5L, "NewName");

        mockMvc.perform(post("/admin/categories/5/edit")
                        .param("name", "NewName"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"))
                .andExpect(flash().attributeExists("err"));

        verify(categoryService).update(5L, "NewName");
    }

    @Test
    void delete_whenServiceThrows_shouldRedirect_andSetErrorFlash() throws Exception {
        doThrow(new RuntimeException("delete failed"))
                .when(categoryService).delete(99L);

        mockMvc.perform(delete("/admin/categories/delete/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"))
                .andExpect(flash().attributeExists("err"));

        verify(categoryService).delete(99L);
    }
}