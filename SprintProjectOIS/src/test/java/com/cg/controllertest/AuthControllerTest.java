package com.cg.controllertest;

import com.cg.controller.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        AuthController controller = new AuthController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // =========================
    // ✅ POSITIVE (3)
    // =========================

    @Test
    void login_shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    void login_shouldReturnStatus200() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void login_shouldNotRedirect() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    // =========================
    // ❌ NEGATIVE (3)
    // =========================

    @Test
    void wrongUrl_shouldReturn404() throws Exception {
        mockMvc.perform(get("/wrong"))
                .andExpect(status().isNotFound());
    }

    @Test
    void loginPost_shouldReturn405() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/login"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void login_shouldNotReturnWrongView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(view().name("auth/login"))
                .andExpect(view().name(org.hamcrest.Matchers.not("wrong/view")));
    }
}