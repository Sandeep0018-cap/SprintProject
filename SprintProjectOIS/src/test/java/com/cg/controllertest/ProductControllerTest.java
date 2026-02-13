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
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // =========================
    // ✅ POSITIVE TESTS
    // =========================

    @Test
    void newProduct_shouldReturnForm() throws Exception {
        mockMvc.perform(get("/products/new")
                        .param("categoryId", "1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("products/product-form"))
                .andExpect(model().attributeExists("categoryId"))
                .andExpect(model().attributeExists("vendors"));
    }

    @Test
    void newProduct_multipleCalls_shouldWork() throws Exception {
        mockMvc.perform(get("/products/new")
                        .param("categoryId", "1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/products/new")
                        .param("categoryId", "1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void newProduct_shouldContainProductAttribute() throws Exception {
        mockMvc.perform(get("/products/new")
                        .param("categoryId", "1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(model().attributeExists("product"));
    }

    // =========================
    // ❌ NEGATIVE TESTS
    // =========================

    @Test
    void newProduct_withoutLogin_shouldRedirect() throws Exception {
        mockMvc.perform(get("/products/new")
                        .param("categoryId", "1"))
                .andExpect(status().is3xxRedirection());
    }

}