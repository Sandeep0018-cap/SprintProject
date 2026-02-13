package com.cg.controllertest;

import com.cg.controller.RestockController;
import com.cg.dto.RestockDto;
import com.cg.entity.Product;
import com.cg.entity.RestockRequest;
import com.cg.entity.Vendor;
import com.cg.enums.RestockStatus;
import com.cg.service.IRestockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestockController.class)
class RestockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IRestockService restockService;

    @Test
    @WithMockUser(username = "staff_user")
    void testForm_InitializesCorrectly() throws Exception {
        mockMvc.perform(get("/staff/restock/new").param("vendorId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("staff/restock-form"))
                .andExpect(model().attributeExists("vendors", "products", "req", "isEdit"))
                .andExpect(model().attribute("selectedVendorId", 1L));
    }

    @Test
    @WithMockUser(username = "staff_user")
    void testCreate_Success() throws Exception {
        // Setup mocks for validation and resolution
        when(restockService.productBelongsToVendor(anyLong(), anyLong())).thenReturn(true);
        when(restockService.getProduct(anyLong())).thenReturn(new Product());
        when(restockService.getVendor(anyLong())).thenReturn(new Vendor());

        mockMvc.perform(post("/staff/restock")
                .with(csrf()) // Required for Spring Security POST requests
                .param("vendorId", "1")
                .param("productId", "10")
                .param("requestedQty", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))
                .andExpect(flash().attribute("msg", "Restock request submitted!"));

        verify(restockService, times(1)).createRestockRequest(any(), any(), any(), any());
    }

    @Test
    @WithMockUser(username = "staff")
    void testDelete_ForbiddenWhenNotPending() throws Exception {
        RestockRequest nonPendingReq = new RestockRequest();
        // Assuming your entity/service setup results in COMPLETED status
        // You'd ideally use reflection or a constructor to set the status for the mock
        // For this example, let's assume getRestockById returns a COMPLETED one:
        RestockRequest mockReq = mock(RestockRequest.class);
        when(mockReq.getStatus()).thenReturn(RestockStatus.CANCELLED);
        when(restockService.getRestockById(1L)).thenReturn(mockReq);

        mockMvc.perform(delete("/staff/restock/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))
                .andExpect(flash().attribute("error", "Only PENDING requests can be deleted."));

        verify(restockService, never()).deleteRestockRequest(any());
    }

    @Test
    @WithMockUser(username = "staff_user")
    void testCreate_ValidationError_MismatchedVendor() throws Exception {
        // Mock mismatch logic
        when(restockService.productBelongsToVendor(1L, 10L)).thenReturn(false);

        mockMvc.perform(post("/staff/restock")
                .with(csrf())
                .param("vendorId", "1")
                .param("productId", "10")
                .param("requestedQty", "5"))
                .andExpect(status().isOk())
                .andExpect(view().name("staff/restock-form"))
                .andExpect(model().hasErrors());
    }
}
