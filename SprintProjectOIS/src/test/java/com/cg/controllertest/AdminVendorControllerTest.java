package com.cg.controllertest;

import com.cg.controller.AdminVendorController;
import com.cg.service.IVendorService;
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
class AdminVendorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IVendorService vendorService;

    @BeforeEach
    void setup() {
        AdminVendorController controller = new AdminVendorController(vendorService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // =========================
    // ✅ POSITIVE (3)
    // =========================

    @Test
    void list_shouldReturnView_andAddVendorsToModel() throws Exception {
        when(vendorService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/vendors"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/vendors"))
                .andExpect(model().attributeExists("vendors"));

        verify(vendorService).findAll();
    }

    @Test
    void create_whenSuccess_shouldRedirect_andSetSuccessFlash() throws Exception {
        // we don’t care what create returns (controller ignores it)
        when(vendorService.create(anyString(), any(), any(), any())).thenReturn(null);

        mockMvc.perform(post("/admin/vendors")
                        .param("name", "ABC Vendor")
                        .param("contactName", "John")
                        .param("phone", "9999999999")
                        .param("email", "a@b.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/vendors"))
                .andExpect(flash().attributeExists("msg"));

        verify(vendorService).create("ABC Vendor", "John", "9999999999", "a@b.com");
    }

    @Test
    void delete_whenSuccess_shouldRedirect_andSetSuccessFlash() throws Exception {
        doNothing().when(vendorService).delete(1L);

        mockMvc.perform(delete("/admin/vendors/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/vendors"))
                .andExpect(flash().attributeExists("msg"));

        verify(vendorService).delete(1L);
    }

    // =========================
    // ❌ NEGATIVE (3)
    // =========================

    @Test
    void create_whenServiceThrows_shouldRedirect_andSetErrorFlash() throws Exception {
        when(vendorService.create(anyString(), any(), any(), any()))
                .thenThrow(new RuntimeException("Creation failed"));

        mockMvc.perform(post("/admin/vendors")
                        .param("name", "ABC Vendor"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/vendors"))
                .andExpect(flash().attributeExists("err"));

        verify(vendorService).create("ABC Vendor", null, null, null);
    }

    @Test
    void update_whenServiceThrows_shouldRedirect_andSetErrorFlash() throws Exception {
        doThrow(new RuntimeException("Update failed"))
                .when(vendorService).update(eq(5L), anyString(), any(), any(), any());

        mockMvc.perform(post("/admin/vendors/5/edit")
                        .param("name", "New Vendor")
                        .param("contactName", "Sam"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/vendors"))
                .andExpect(flash().attributeExists("err"));

        verify(vendorService).update(5L, "New Vendor", "Sam", null, null);
    }

    @Test
    void delete_whenServiceThrows_shouldRedirect_andSetErrorFlash() throws Exception {
        doThrow(new RuntimeException("Delete failed"))
                .when(vendorService).delete(99L);

        mockMvc.perform(delete("/admin/vendors/delete/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/vendors"))
                .andExpect(flash().attributeExists("err"));

        verify(vendorService).delete(99L);
    }
}