package com.cg.configtest;

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
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    // =========================
    // ✅ POSITIVE TESTS
    // =========================

    // login page should be accessible to everyone
    @Test
    void loginPage_shouldBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    // admin user can access admin URL
    @Test
    void adminUser_shouldAccessAdminPage() throws Exception {
        mockMvc.perform(get("/dashboard")
                .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    // staff user can access staff URL
    @Test
    void staffUser_shouldAccessStaffPage() throws Exception {
        mockMvc.perform(get("/dashboard")
                .with(user("staff").roles("STAFF")))
                .andExpect(status().isOk());
    }

    // =========================
    // ❌ NEGATIVE TESTS
    // =========================

    // not logged in user cannot access admin
    @Test
    void anonymousUser_shouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    // staff cannot access admin
    @Test
    void staffUser_shouldNotAccessAdminPage() throws Exception {
        mockMvc.perform(get("/admin/dashboard")
                .with(user("staff").roles("STAFF")))
                .andExpect(status().isForbidden());
    }

    // admin cannot access staff (if not allowed)
    @Test
    void adminUser_shouldNotAccessStaffPage() throws Exception {
        mockMvc.perform(get("/staff/home")
                .with(user("admin").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }
}