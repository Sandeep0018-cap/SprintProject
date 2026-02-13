package com.cg.exceptiontest;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.exception.BadRequestException;
import com.cg.exception.GlobalExceptionHandler;
import com.cg.exception.ResourceNotFoundException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new TestController())
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

    // =========================
    // ✅ POSITIVE TESTS
    // =========================

    @Test
    void shouldHandleResourceNotFound() throws Exception {
        mockMvc.perform(get("/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    @Test
    void shouldHandleBadRequest() throws Exception {
        mockMvc.perform(get("/badrequest"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));
    }

    // =========================
    // ❌ NEGATIVE / FALLBACK
    // =========================

    @Test
    void shouldHandleGenericExceptionWithErrorView() throws Exception {
        mockMvc.perform(get("/generic"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    // ----------------------------------
    // Dummy Controller for Testing
    // ----------------------------------

    @RestController
    static class TestController {

        @GetMapping("/notfound")
        public String notFound() {
            throw new ResourceNotFoundException("Not found");
        }

        @GetMapping("/badrequest")
        public String badRequest() {
            throw new BadRequestException("Bad request");
        }

        @GetMapping("/validation")
        public String validation() throws Exception {
            throw new org.springframework.web.bind.MethodArgumentNotValidException(null, null);
        }

        @GetMapping("/generic")
        public String generic() {
            throw new RuntimeException("Something broke");
        }
    }
}