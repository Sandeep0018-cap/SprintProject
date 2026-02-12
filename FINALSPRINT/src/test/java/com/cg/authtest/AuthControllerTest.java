package com.cg.authtest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.cg.controller.AuthController;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        // Adding a ViewResolver is critical to prevent "Circular View Path" errors
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/"); // Standard prefix
        viewResolver.setSuffix(".jsp");            // Standard suffix

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void login_shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void root_shouldRedirectToDashboard() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    void controllerLoadsSuccessfully() {
        assertNotNull(authController);
    }

    @Test
    void invalidUrl_shouldReturnNotFound() throws Exception {
        // In standalone mode, only mapped paths in AuthController exist
        mockMvc.perform(get("/invalid-url"))
                .andExpect(status().isNotFound());
    }

    @Test
    void login_withPostMethod_shouldReturnMethodNotAllowed() throws Exception {
        mockMvc.perform(post("/login"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void root_withPostMethod_shouldReturnMethodNotAllowed() throws Exception {
        mockMvc.perform(post("/"))
                .andExpect(status().isMethodNotAllowed());
    }
}
