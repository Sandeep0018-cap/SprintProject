package com.cg.controllertest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // =========================
    // ✅ POSITIVE
    // =========================

    @Test
    void list_shouldReturnCustomersView() throws Exception {
        mockMvc.perform(get("/customers/list")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("customers"));
    }

    @Test
    void create_shouldRedirectToList() throws Exception {
        mockMvc.perform(post("/customers")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .param("name", "Test User")
                        .param("phone", "9876543210")
                        .param("city", "Hyderabad"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void list_shouldWorkMultipleTimes() throws Exception {
        mockMvc.perform(get("/customers/list")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/customers/list")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    // =========================
    // ❌ NEGATIVE
    // =========================

    @Test
    void accessWithoutLogin_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/customers/list"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void postWithoutCsrf_shouldFail() throws Exception {
        mockMvc.perform(post("/customers")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }

    @Test
    void wrongUrl_shouldReturn404() throws Exception {
        mockMvc.perform(get("/customers/invalid")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }
}