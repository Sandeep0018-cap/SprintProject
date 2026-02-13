package com.cg.controllertest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // =========================
    // ✅ POSITIVE (3)
    // =========================

    @Test
    void orders_withoutStatus_shouldReturnAll() throws Exception {
        mockMvc.perform(get("/orders")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("statuses"))
                .andExpect(model().attribute("selectedStatus", "ALL"));
    }

    @Test
    void orders_withStatus_shouldFilter() throws Exception {
        mockMvc.perform(get("/orders")
                        .with(user("admin").roles("ADMIN"))
                        .param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attribute("selectedStatus", "PENDING"));
    }

    @Test
    void orders_shouldWorkMultipleTimes() throws Exception {
        mockMvc.perform(get("/orders")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/orders")
                        .with(user("admin").roles("ADMIN"))
                        .param("status", "DELIVERED"))
                .andExpect(status().isOk());
    }

    // =========================
    // ❌ NEGATIVE (3)
    // =========================

    @Test
    void orders_withoutLogin_shouldRedirect() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void wrongUrl_shouldReturn404() throws Exception {
        mockMvc.perform(get("/orders/invalid")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }
}