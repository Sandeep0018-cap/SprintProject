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
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // =========================
    // ✅ POSITIVE TESTS (3)
    // =========================

    @Test
    void dashboard_adminAccess_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/dashboard")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }

    @Test
    void dashboard_staffAccess_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/dashboard")
                        .with(user("staff").roles("STAFF")))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }

    @Test
    void dashboard_shouldSetLoginRoleAttribute() throws Exception {
        mockMvc.perform(get("/dashboard")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("loginRole"));
    }

    // =========================
    // ❌ NEGATIVE TESTS (3)
    // =========================

    @Test
    void dashboard_withoutLogin_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void dashboard_wrongRole_shouldStillLoadButNotAdmin() throws Exception {
        mockMvc.perform(get("/dashboard")
                        .with(user("random").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    void wrongUrl_shouldReturn404() throws Exception {
        mockMvc.perform(get("/dashboard/invalid")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }
}